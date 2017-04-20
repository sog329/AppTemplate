/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.base;

import java.io.File;

/**
 * Created by Jack on 2017/4/18 0018.
 */

public class DownloadTask extends Task {
    protected String mHttpFilePath = null;
    protected String mLocalFilePath = null;

    public String getHttpFilePath() {
        return mHttpFilePath;
    }

    public void setHttpFilePath(String httpFilePath) {
        if (!mHasSend) {
            mHttpFilePath = httpFilePath;
        }
    }

    public String getLocalFilePath() {
        return mLocalFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        if (!mHasSend) {
            mLocalFilePath = localFilePath;
        }
    }

    public void onProgressUpdate(int percent) {
        synchronized(mCallback) {
            if (mCallback[0] != null) {
                if (mCallback[0] instanceof DownloadTask.Callback) {
                    DownloadTask.Callback callback = (Callback) mCallback[0];
                    callback.onProgressUpdate(percent);
                }
            }
        }
    }

    @Override
    public void run() {
        File file = NetTool.downloadFile(this);
        if (file != null) {
            onSuccess(file);
        } else {
            onFail(file);
        }
    }

    public interface Callback<T, O> extends Task.Callback<T, O> {
        void onProgressUpdate(int percent);
    }
}
