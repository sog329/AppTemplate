/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.base;

/**
 * Created by Jack on 2017/3/30 0030.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler mCrashHandler = null;

    public static synchronized void onCreate() {
        if (mCrashHandler == null) {
            mCrashHandler = new CrashHandler();
        }
        mCrashHandler.init();
    }

    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler = null;

    private void init() {
        mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        StringBuilder sb = new StringBuilder();
        sb.append("Thread[");
        sb.append(t.getName());
        sb.append("]: ");
        sb.append(e.getMessage());
        LogTool.debug(sb.toString());
        if (mUncaughtExceptionHandler != null) {
            if (DeviceTool.mIsDebug) {
                mUncaughtExceptionHandler.uncaughtException(t, e);
            } else {

            }
        }
    }
}
