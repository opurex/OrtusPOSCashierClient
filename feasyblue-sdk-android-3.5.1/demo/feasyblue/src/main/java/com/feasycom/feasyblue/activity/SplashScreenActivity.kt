package com.feasycom.feasyblue.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.CountDownTimer
import android.view.KeyEvent
import android.view.View
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.feasycom.common.utils.MsgLogger
import com.feasycom.feasyblue.App
import com.feasycom.feasyblue.R
import com.feasycom.feasyblue.dialog.FirstDialogFragment
import com.feasycom.feasyblue.utils.*
import com.feasycom.feasyblue.worker.DeviceWorker
import com.feasycom.network.DeviceNetwork
import com.feasycom.network.bean.Parameter
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.first_dialog.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@SuppressLint("SetTextI18n")
class SplashScreenActivity : BaseActivity() {

    lateinit var mFirstDialogFragment: FirstDialogFragment

    override fun getLayout() = R.layout.activity_splash

    private val countDownTimer: CountDownTimer by lazy {
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                toMain.text = "${millisUntilFinished / 1000}s | Close"
            }

            override fun onFinish() {
                countDownTimer.cancel()
                firstIn()
            }
        }
    }

    private fun clearPreference(){
        clearFilter()
        clearBatchFilter()
        clearFeed()
        clearTcp()
        clearPassword()
        clearDeveloperDebug()
    }

    private fun clearFilter() {
        putBoolean("rssiSwitch", false)
        putBoolean("nameSwitch", false)
        putInt("rssiValue", -100)
        putStr("nameValue", "")
    }

    private fun clearBatchFilter() {
        putBoolean("otaRssiSwitch", false)
        putBoolean("otaNameSwitch", false)
        putInt("otaRssiValue", -70)
        putStr("otaNameValue", "")
    }

    private fun clearFeed() {
        putStr("FEEDBACK_ADVICE", "")
        putStr("FEEDBACK_BUG", "")
        putStr("FEEDBACK_UI", "")
        putStr("FEEDBACK_COOPERATION", "")
    }

    private fun clearTcp() {
        putStr("tcpServer", "")
        putStr("tcpPort", "")
        putInt("selection", 0)
    }

    private fun clearPassword() {
        putBoolean("isPasswordPara",false)
        putBoolean("isPasswordEAdvance",false)
    }

    private fun clearDeveloperDebug() {
        putBoolean("facpSwitchOpen", true)
        putStr("customUuid", "")
        putBoolean("sdpServiceSwitchOpen", false)
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun initView() {
        clearPreference()
        val workManager = WorkManager.getInstance(App.context!!)
        val deviceWorker = OneTimeWorkRequest.Builder(DeviceWorker::class.java).build()
        workManager.beginWith(listOf(deviceWorker)).enqueue()
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.setDecorFitsSystemWindows(false)
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            }
        } catch (e: NoSuchMethodError) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
        countDownTimer.start()
        Glide.with(this@SplashScreenActivity).load(getStr("lanch").let {
            it.ifEmpty {
                R.drawable.load
            }
        }).into(lanch_img)
        downloadImage()
        toMain.setOnClickListener {
            countDownTimer.cancel()
            firstIn()
        }
    }

    private fun firstIn() {
        if (::mFirstDialogFragment.isInitialized) {
            return
        } else {
            val data = getBoolean("first", false)
            mFirstDialogFragment = FirstDialogFragment()
            if (!mFirstDialogFragment.isAdded) {
                if (!data) {
                    with(mFirstDialogFragment) {
                        onAgree = {
                            putBoolean("first", true)
                            dismiss()
                            jump2Main()
                        }
                        onRefuse = {
                            finish()
                        }
                        show(supportFragmentManager, "first")
                    }
                } else {
                    jump2Main()
                }
            }
        }
    }

    private fun jump2Main() {
        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
        finish()
    }

    private fun downloadImage() {
        MainScope().launch(Dispatchers.IO) {
            try {
                val parameter = Parameter("blue")
                val splash = DeviceNetwork.getLanch(parameter)
                if (splash.code == 200) {
                    val url = splash.data.image
                    val context: Context = applicationContext
                    val target: FutureTarget<File> = Glide.with(context).asFile().load(url).submit()
                    val imageFile = target.get()
                    MsgLogger.e("downloadImage: ${imageFile.path}")
                    putStr("lanch", imageFile.path)
                    withContext(Dispatchers.Main) {
                        Glide.with(this@SplashScreenActivity).load(imageFile.path).into(lanch_img)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            countDownTimer.cancel()
            firstIn()
            false
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }

}