/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.demo.splash;

import java.io.File;

import com.app.base.DiskTool;
import com.app.base.LogTool;
import com.app.base.NetTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;

/**
 * Created by Jack on 2017/4/3 0003.
 */

public class SplashPic {
    private static String FOLDER_NAME = "Splash";
    private static int mWidth = 0;
    private static int mHeight = 0;

    public static void init(Context context) {
        if (context != null) {
            Rect rect = new Rect();
            ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            LogTool.debug(rect.toString());
            mWidth = rect.width();
            mHeight = rect.height();
        }
    }

    public static int getWidth() {
        return mWidth;
    }

    public static int getHeight() {
        return mHeight;
    }

    public static String getLocalPicFolderPath() {
        String str = null;
        String appFolderPath = DiskTool.getAppFolderPath();
        if (appFolderPath != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(appFolderPath);
            sb.append(File.separator);
            sb.append(FOLDER_NAME);
            str = sb.toString();
        }
        return str;
    }

    public static String getLocalPicPath() {
        String str = getLocalPicFolderPath();
        if (str != null) {
            File file = new File(str);
            File[] aryFile = file.listFiles();
            if (aryFile != null && aryFile.length > 0) {
                str = aryFile[0].getAbsolutePath();
            }
        }
        return str;
    }

    public static class GetPicUrl extends NetTask {
        @Override
        public String getHttpUrl() {
            return "https://www.baidu.com";
        }

        @Override
        public boolean isGet() {
            return true;
        }

        @Override
        public Object parseResult(Object obj) {
            LogTool.debug((String) obj);
            return "http://lorempixel.com/1080/1920/nature/2/";
        }
    }
}
