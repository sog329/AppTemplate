/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.base;

/**
 * Created by Jack on 2017/3/19 0019.
 */

public class LogTool {

    public static void show(String str) {
        if (DeviceTool.mIsDebug) {
            android.util.Log.d("____", str);
        }
    }

    public static void debug(String str) {
        if (DeviceTool.mIsDebug) {
            StringBuilder sb = new StringBuilder();
            sb.append(getCurrentClassName());
            sb.append(".");
            sb.append(getCurrentMethodName());
            sb.append("(): ");
            sb.append(str);
            show(sb.toString());
        }
    }

    private static String getCurrentMethodName() {
        String methodName = null;
        try {
            StackTraceElement stack = new Throwable().getStackTrace()[2];
            methodName = stack.getMethodName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return methodName;
    }

    private static String getCurrentClassName() {
        String className = null;
        try {
            StackTraceElement stack = new Throwable().getStackTrace()[2];
            className = stack.getClassName();
            className = className.substring(className.lastIndexOf(".") + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return className;
    }
}
