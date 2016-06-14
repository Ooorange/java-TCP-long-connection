package com.example.csdnblog4.common;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by orange on 16/5/10.
 */
public class ProjectApplication extends Application {
    private static Context context;
    private static String uuid;
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        context=this;
        AppCrashHandler appCrashHandler=new AppCrashHandler();
        Thread.setDefaultUncaughtExceptionHandler(appCrashHandler);
        uuid=MobileUtils.getUUID(this);

    }

    public static Context getContext(){
        return context;
    }

    public static String getUUID(){
        return uuid;
    }

}
