/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.base;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Jack on 2017/3/22 0022.
 */

public class DeviceTool {
    protected static boolean mIsDebug = false;
    private static int mScreenWidth = 0;
    private static int mScreenHeight = 0;

    public static void init(Context context) {
        if (context != null) {
            // 屏幕尺寸
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = null;
            if (wm != null) {
                display = wm.getDefaultDisplay();
            }

            if (display != null) {
                mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
                mScreenHeight = context.getResources().getDisplayMetrics().heightPixels;
            }
            // debug模式
            ApplicationInfo applicationInfo = context.getApplicationInfo();
            if (applicationInfo != null) {
                mIsDebug = (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            } else {
                mIsDebug = false;
            }
        }
    }

    public static int getScreenWidth() {
        return mScreenWidth;
    }

    public static int getScreenHeight() {
        return mScreenHeight;
    }

    public static PackageInfo getAppPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            PackageManager pm = context.getPackageManager();
            info = pm.getPackageInfo(context.getPackageName(), 0);
            return info;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    //    public static boolean isNetworkAvailable(Context context) {
    //        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    //        NetworkInfo[] info = mgr.getNetworkInfo();
    //        if (info != null) {
    //            for (int i = 0; i < info.length; i++) {
    //                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
    //                    return true;
    //                }
    //            }
    //        }
    //        return false;
    //    }

    public static void installApk(Activity activity, String apkPath) {
        if (activity != null && apkPath != null) {
            File file = new File(apkPath);
            if (file.exists()) {
                
            }
        }
    }
}
