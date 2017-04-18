/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.demo.main;

import com.app.base.CrashHandler;
import com.app.base.DeviceTool;
import com.app.base.DiskTool;
import com.app.base.LogTool;
import com.app.base.MemLeakHelper;

import android.app.Application;

/**
 * Created by Jack on 2017/3/29 0029.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DeviceTool.init(this);
        DiskTool.init(this);
        LogTool.debug("");
        MemLeakHelper.init(this);
        CrashHandler.onCreate();
    }


}
