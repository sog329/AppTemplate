/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.demo.main;

import java.io.File;

import com.app.base.DiskTool;
import com.app.base.DownloadTask;
import com.app.base.LogTool;
import com.app.base.Presenter;
import com.app.base.TaskManager;
import com.app.demo.splash.SplashPresenter;

/**
 * Created by Jack on 2017/4/17 0017.
 */

public class MainPresenter extends Presenter {
    private TaskManager mTaskManager = new TaskManager();
    private SplashPresenter mSplashPresenter = new SplashPresenter(null);

    private static String FOLDER_NAME = "apk";
    private static String APK_NAME = "update.apk";
    private DownloadTask.Callback mCallback = new DownloadTask.Callback<DownloadTask, File>() {
        @Override
        public void onSuccess(DownloadTask task, File obj) {

        }

        @Override
        public void onFail(DownloadTask task, Object obj) {

        }

        @Override
        public void onProgressUpdate(int percent) {
            LogTool.debug(percent + "%");
        }
    };

    public void downloadApk() {
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.setHttpFilePath("http://gdown.baidu.com/data/wisegame/2d5bf81de4e0ca42/weixin_1041.apk");
        StringBuilder sb = new StringBuilder();
        sb.append(DiskTool.getAppFolderPath());
        sb.append(File.separator);
        sb.append(FOLDER_NAME);
        DiskTool.checkFolder(sb.toString());
        sb.append(File.separator);
        sb.append(APK_NAME);
        downloadTask.setLocalFilePath(sb.toString());
        mTaskManager.addTask(downloadTask, mCallback);
    }

    public void updateSplashPic() {
        mSplashPresenter.updateLocalPic();
    }

    @Override
    public void onDestroy() {
        mSplashPresenter.onDestroy();
    }
}
