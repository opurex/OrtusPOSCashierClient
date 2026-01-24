package com.feasycom.feasyblue.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.View.GONE
import android.webkit.*
import android.widget.LinearLayout
import androidx.core.content.FileProvider
import com.feasycom.common.utils.MsgLogger
import com.feasycom.feasyblue.R
import com.feasycom.feasyblue.utils.getBoolean
import com.feasycom.feasyblue.utils.putBoolean
import com.permissionx.guolindev.PermissionX
import kotlinx.android.synthetic.main.fragment_store.*
import kotlinx.android.synthetic.main.fragment_store.toolbarTitle
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class StoreFragment : BaseFragment(), View.OnClickListener {

    private var mUploadMessage: ValueCallback<Uri>? = null
    private var mUploadCallbackAboveL: ValueCallback<Array<Uri>>? = null
    private var imageUri: Uri? = null // 图片地址

    override fun getLayoutId() = R.layout.fragment_store

    override fun initView() {
        toolbarTitle.text = getString(R.string.store)
        initWebView()
        toolbarBack.setOnClickListener(this)
        toolbarRefresh.setOnClickListener(this)
    }

    private fun initWebView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            myWebView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    try {
                        if (url != mUrl) {
                            toolbarBack?.visibility = View.VISIBLE
                        } else {
                            toolbarBack?.visibility = GONE
                        }
                    } catch (e: IllegalStateException) {
                        e.printStackTrace()
                    }
                }
            }

            myWebView.webChromeClient = object : WebChromeClient() {
                // 相机、相册
                override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    mUploadCallbackAboveL = filePathCallback

                    val firstPermissions = requireContext().getBoolean("firstPermissions", false)
                    if (!firstPermissions) {
                        initPermission()
                    }
                    return true
                }

                override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                    MsgLogger.w(
                        TAG,
                        "onConsoleMessage => ${consoleMessage?.messageLevel()}  ${consoleMessage?.message()}"
                    )
                    return super.onConsoleMessage(consoleMessage)
                }

                override fun onPermissionRequest(request: PermissionRequest?) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        for (str in request?.resources!!) {
                            MsgLogger.d(TAG, "onPermissionRequest str => $str")
                        }
                        request?.grant(request.resources)
                    }
                }

                override fun onJsAlert(
                    view: WebView?, url: String?, message: String?, result: JsResult?
                ): Boolean {
                    MsgLogger.e(TAG,"onJsAlert message => $message")
                    result?.confirm()
                    return true
                }

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    try {
                        progressBar.progress = newProgress
                        if (newProgress == 100) {
                            progressBar?.visibility = LinearLayout.GONE
                        } else {
                            if (progressBar?.visibility == LinearLayout.GONE) {
                                progressBar?.visibility = LinearLayout.VISIBLE
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } else {
            progressBar?.visibility = GONE
        }
        with(myWebView.settings) {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            allowFileAccess = true // 设置允许访问文件数据
            setSupportZoom(true)
            builtInZoomControls = true
            cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            domStorageEnabled = true
            databaseEnabled = true
        }
        myWebView.loadUrl(mUrl)
        myWebView!!.setInitialScale(100)
    }

    /**
     * 调用相机/相册选择窗
     */
    private fun takePhoto() {
        val fileName = "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(Date())}.jpg"
        val imagePath =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)

        imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                requireContext(), "${requireContext().packageName}.fileprovider", imagePath
            )
        } else {
            Uri.fromFile(imagePath)
        }
        // 相册、相机选择窗
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        val photoPickerIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val chooserIntent = Intent.createChooser(photoPickerIntent, "选择上传方式")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(captureIntent))
        startActivityForResult(chooserIntent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            chooseAbove(resultCode, data)
        } else {
            chooseBelow(resultCode, data)
        }
    }

    /**
     * Android API < 21(Android 5.0)版本的回调处理
     */
    private fun chooseBelow(resultCode: Int, data: Intent?) {
        MsgLogger.e(TAG, "调用方法 chooseBelow")
        if (Activity.RESULT_OK == resultCode) {
            updatePhotos()

            if (data != null) {
                // 这里是针对文件路径处理
                val uri = data.data
                if (uri != null) {
                    MsgLogger.e(TAG, "系统里取到的图片 => $uri")
                    mUploadMessage?.onReceiveValue(uri)
                } else {
                    mUploadMessage?.onReceiveValue(null)
                }
            } else {
                // 以指定图像存储路径的方式调超相机，成功后返回data为空
                MsgLogger.e(TAG, "自已命名的图片 => ${imageUri.toString()}")
                mUploadMessage?.onReceiveValue(imageUri)
            }
        } else {
            mUploadMessage?.onReceiveValue(null)
        }
        mUploadMessage = null
    }

    /**
     * Android API >= 21(Android 5.0) 版本的回调处理
     */
    private fun chooseAbove(resultCode: Int, data: Intent?) {
        MsgLogger.e(TAG, "调用方法 chooseAbove => $data")
        if (Activity.RESULT_OK == resultCode) {
            updatePhotos()

            if (data != null) {
                // 这里是针对从文件中选图片的处理
                val results: Array<Uri>?
                val uriData: Uri? = data.data
                results = if (uriData != null) {
                    arrayOf(uriData)
                } else {
                    null
                }

                results?.let {
                    for (uri in it) {
                        MsgLogger.e(TAG, "系统里取到的图片 => $uri")
                    }
                    mUploadCallbackAboveL?.onReceiveValue(it)
                } ?: run {
                    mUploadCallbackAboveL?.onReceiveValue(null)
                }
            } else {
                MsgLogger.e(TAG, "自己命名的图片 => ${imageUri.toString()}")
                mUploadCallbackAboveL?.onReceiveValue(arrayOf(imageUri!!))
            }
        } else {
            mUploadCallbackAboveL?.onReceiveValue(null)
        }
        mUploadCallbackAboveL = null
    }

    // 发送广播进行更新相册
    private fun updatePhotos() {
        // 该广播即使多发（即选取照片成功时也发送）也没有关系，只是唤醒系统刷新媒体文件
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        intent.data = imageUri
        requireContext().sendBroadcast(intent)
    }

    private fun initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PermissionX.init(this).permissions(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
            ).explainReasonBeforeRequest().onExplainRequestReason { scope, deniedList, _ ->
                scope.showRequestReasonDialog(
                    deniedList, getString(R.string.permission_camera_record), getString(
                        R.string.ok
                    ), getString(R.string.close)
                )
            }.onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList, getString(R.string.to_setting_open_permission), getString(
                        R.string.ok
                    ), getString(R.string.close)
                )
            }.request { allGranted, _, _ ->
                if (allGranted) {
                    takePhoto()
                }
                requireContext().putBoolean("firstPermission", true)
            }
        } else {
            PermissionX.init(this).permissions(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
            ).explainReasonBeforeRequest().onExplainRequestReason { scope, deniedList, _ ->
                scope.showRequestReasonDialog(
                    deniedList, getString(R.string.permission_location_prompt), getString(
                        R.string.ok
                    ), getString(R.string.close)
                )
            }.onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList, getString(R.string.to_setting_open_permission), getString(
                        R.string.ok
                    ), getString(R.string.close)
                )
            }.request { allGranted, _, _ ->
                if (allGranted) {
                    takePhoto()
                }
                requireContext().putBoolean("firstPermission", true)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.toolbarBack -> {
                myWebView.goBack()
            }
            R.id.toolbarRefresh -> {
                myWebView.reload()
            }
        }
    }

    companion object {
        const val TAG = "StoreFragment"
        const val mUrl = "https://www.feasycom.com.cn/products/"
        fun newInstance(): StoreFragment {
            return StoreFragment()
        }
    }
}





























