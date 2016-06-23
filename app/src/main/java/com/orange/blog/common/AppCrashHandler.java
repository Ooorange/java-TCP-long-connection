package com.orange.blog.common;


import android.os.Environment;
import android.text.format.DateFormat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class AppCrashHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler defaultUEH;

    public AppCrashHandler() {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        // 获取跟踪的栈信息，除了系统栈信息，还把手机型号、系统版本、编译版本的唯一标示
        StackTraceElement[] trace = ex.getStackTrace();
        StackTraceElement[] trace2 = new StackTraceElement[trace.length + 3];
        System.arraycopy(trace, 0, trace2, 0, trace.length);
        trace2[trace.length + 0] = new StackTraceElement("Android", "MODEL",
                android.os.Build.MODEL, -1);
        trace2[trace.length + 1] = new StackTraceElement("Android", "VERSION",
                android.os.Build.VERSION.RELEASE, -1);
        trace2[trace.length + 2] = new StackTraceElement("Android",
                "FINGERPRINT", android.os.Build.FINGERPRINT, -1);
        // 追加信息，因为后面会回调默认的处理方法
        ex.setStackTrace(trace2);
        ex.printStackTrace(printWriter);
        // 把上面获取的堆栈信息转为字符串，打印出来
        String stacktrace = result.toString();
        printWriter.close();
        // 这里把刚才异常堆栈信息写入SD卡的Log日志里面
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String sdcardPath = Environment.getExternalStorageDirectory()
                    .getPath();
            try {
                writeLog(stacktrace, sdcardPath + "/orange/");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        defaultUEH.uncaughtException(thread, ex);
    }

    // 写入Log信息的方法，写入到SD卡里面
    private void writeLog(String log, String name) {
        CharSequence timestamp = DateFormat.format("yyyyMMdd_HHmmss",
                System.currentTimeMillis());
        String filename = name + "_" + timestamp + ".txt";
        File file = new File(filename);
        try {
            if (file.getParentFile().mkdirs())
                file.createNewFile();
        } catch (Exception e) {
            return;
        }
        try {
            FileOutputStream stream = new FileOutputStream(file);
            OutputStreamWriter output = new OutputStreamWriter(stream);
            BufferedWriter bw = new BufferedWriter(output);
            // 写入相关Log到文件
            bw.write(log);
            bw.newLine();
            bw.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
