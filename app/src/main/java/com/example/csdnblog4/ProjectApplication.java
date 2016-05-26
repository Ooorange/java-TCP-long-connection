package com.example.csdnblog4;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by orange on 16/5/10.
 */
public class ProjectApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        AppCrashHandler appCrashHandler=new AppCrashHandler();
        Thread.setDefaultUncaughtExceptionHandler(appCrashHandler);
    }
}
