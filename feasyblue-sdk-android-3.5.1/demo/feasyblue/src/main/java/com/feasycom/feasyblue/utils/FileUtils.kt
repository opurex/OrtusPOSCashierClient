package com.feasycom.feasyblue.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import com.feasycom.common.utils.MsgLogger
import java.io.*

class FileUtils {
    companion object{

        private const val TAG = "FileUtils"
    }

    fun saveBitmap(context: Context, bitmap: Bitmap, bitName: String, isSuccess: ((success: Boolean) -> Unit)){
        val fileName = when(Build.BRAND){
            "xiaomi" -> {
                MsgLogger.e("saveBitmap => 小米")
                Environment.getExternalStorageDirectory().path + "/DCIM/Camera/" + bitName
            }
            "HUAWEI" -> {
                MsgLogger.e("saveBitmap => 华为")
                Environment.getExternalStorageDirectory().path + "/DCIM/Camera/" + bitName
            }
            "GOOGLE" ->{
                Environment.getExternalStorageDirectory().path +"/DCIM/"+bitName
            }
            else -> {
                MsgLogger.e("saveBitmap => ${Build.BRAND}")
                Environment.getExternalStorageDirectory().path +"/DCIM/"+bitName
            }
        }
        val file = File(fileName)
        if(file.exists()){
            isSuccess.invoke(true)
        }else {
            try {
                val out = FileOutputStream(file)
                if(bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)){
                    out.flush()
                    out.close()
                }
            }catch (e: FileNotFoundException) {
                isSuccess.invoke(false)
                e.printStackTrace();
            }
            catch (e: IOException) {
                isSuccess.invoke(false)
                e.printStackTrace();
            }
            isSuccess.invoke(true)
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://$fileName")))
        }
    }

    fun exportDataToTxt(data: List<String>, file: File): Boolean {
//        val directory = File(context.getExternalFilesDir(null), "ExportedData")
//        if (!directory.exists()) {
//            directory.mkdirs()
//        }
//        val file = File(directory, "$fileName.txt")
//        MsgLogger.e(TAG,"file => ${file.path}")
        return try {
            val fileWriter = FileWriter(file)
            data.forEach { line ->
                fileWriter.append(line)
                fileWriter.append("\n")
            }
            fileWriter.flush()
            fileWriter.close()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

}