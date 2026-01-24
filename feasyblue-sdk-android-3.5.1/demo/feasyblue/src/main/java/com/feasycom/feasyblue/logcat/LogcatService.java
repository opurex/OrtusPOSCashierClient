package com.feasycom.feasyblue.logcat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class LogcatService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                captureLogToFile();
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    private void captureLogToFile() {
        File directory = getExternalFilesDir("Logcat");
        if (!directory.isDirectory()) {
            directory.delete();
        }
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(directory, "Logcat.txt");
        if (!file.isFile()) {
            file.delete();
        }
        if (file.exists()) {
            file.delete();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            // 创建进程构建器，并设置命令
            ProcessBuilder processBuilder = new ProcessBuilder("logcat", "-v", "time");
            // 重定向错误流
            processBuilder.redirectErrorStream(true);
            // 启动进程
            Process process = processBuilder.start();

            // 创建文件输出流
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            // 将输入流复制到输出流
            copyInputStreamToFile(process.getInputStream(), fileOutputStream);
            // 关闭文件输出流
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyInputStreamToFile(InputStream inputStream, FileOutputStream outputFile) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputFile.write(buffer, 0, length);
        }
    }

}