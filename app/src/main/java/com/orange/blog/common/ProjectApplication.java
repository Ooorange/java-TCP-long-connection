package com.orange.blog.common;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by orange on 16/5/10.
 */
public class ProjectApplication extends Application {
    private static Context context;
    private static String uuid;
    public static int versionID=-1;
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        context=this;
        AppCrashHandler appCrashHandler=new AppCrashHandler();
        Thread.setDefaultUncaughtExceptionHandler(appCrashHandler);
        uuid=MobileUtils.getUUID(this);
        Log.d("orangeUUId",uuid);
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null)
            versionID = info.versionCode;

    }

    public static void setContext(Context context) {
        ProjectApplication.context = context;
    }

    public static Context getContext(){
        return context;
    }

    public static String getUUID(){
        return uuid;
    }

}
