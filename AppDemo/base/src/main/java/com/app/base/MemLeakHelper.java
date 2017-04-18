/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.base;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import android.app.Activity;
import android.app.Application;

/**
 * Created by Jack on 17/3/30.
 */

public class MemLeakHelper {
    private static RefWatcher mRefWatcher = null;

    public static synchronized void init(Application application) {
        if (application != null && mRefWatcher == null) {
            mRefWatcher = LeakCanary.install(application);
        }
    }

    public static synchronized void check(Activity activity) {
        try {
            if (mRefWatcher != null) {
                mRefWatcher.watch(activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
