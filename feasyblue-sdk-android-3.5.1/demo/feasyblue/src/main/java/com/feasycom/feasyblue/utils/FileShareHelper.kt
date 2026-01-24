package com.feasycom.feasyblue.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.feasycom.feasyblue.R
import java.io.File

object FileShareHelper {

    fun shareFile(context: Context, fileUri: Uri, mimeType: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = mimeType
        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            context.startActivity(Intent.createChooser(shareIntent, context.resources.getString(R.string.logcat_options_share)))
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtil.show(context, context.resources.getString(R.string.unable_to_share_file))
        }
    }

    fun getFileUri(context: Context, file: File): Uri {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            // 使用 FileProvider 获取文件 URI，适用于 Android 7.0 及以上版本
            androidx.core.content.FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } else {
            // 直接获取文件 URI，适用于 Android 7.0 以下版本
            Uri.fromFile(file)
        }
    }

}