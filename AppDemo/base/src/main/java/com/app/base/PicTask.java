/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.base;

import android.graphics.Bitmap;

/**
 * Created by Jack on 2017/4/3 0003.
 */

public class PicTask extends Task {
    protected int mWidth = 0;
    protected int mHeight = 0;
    protected String mPicUrl = null;
    protected String mPicFolderPath = null;
    private Result mResult = new Result();

    public void setPicSize(int width, int height) {
        if (!mHasSend) {
            mWidth = width;
            mHeight = height;
        }
    }

    public void setPicUrl(String picUrl) {
        if (!mHasSend) {
            this.mPicUrl = picUrl;
        }
    }

    public void setPicFolderPath(String picFolderPath) {
        if (!mHasSend) {
            mPicFolderPath = picFolderPath;
        }
    }

    public Result getResult() {
        return mResult;
    }

    @Override
    public void run() {
        Bitmap bmp = null;
        try {
            bmp = PicTool.load(mPicUrl, mPicFolderPath, mWidth, mHeight, mResult);
            LogTool.debug(mResult.isByNet ? "ByNet" : "ByLocal" + " cost " + mResult.costTimeMs + "ms");
        } catch (Exception e) {
            LogTool.debug(e.toString());
        } catch (Error e) {
            LogTool.debug(e.toString());
        }
        if (bmp == null) {
            onFail(bmp);
        } else {
            onSuccess(bmp);
        }
    }

    public static class Result {
        public boolean isByNet = false;
        public int costTimeMs = 0;
    }
}
