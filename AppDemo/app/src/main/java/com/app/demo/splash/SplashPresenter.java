/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.demo.splash;

import static com.app.demo.splash.SplashPic.getHeight;
import static com.app.demo.splash.SplashPic.getWidth;

import com.app.base.PicTask;
import com.app.base.PicTool;
import com.app.base.Presenter;
import com.app.base.Task;
import com.app.base.TaskManager;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Jack on 2017/4/3 0003.
 */

public class SplashPresenter extends Presenter {
    private TaskManager mTaskManager = new TaskManager(1);
    private IView mViewImpl = null;
    private Bitmap mBitmap = null;
    private Task.Callback<Bitmap> mCbDownloadPic = new Task.Callback<Bitmap>() {
        @Override
        public void onSuccess(Task task, Bitmap obj) {
            if (obj != null) {
                obj.recycle();
                obj = null;
            }
        }

        @Override
        public void onFail(Task task, Object obj) {

        }
    };

    private Task.Callback<String> mCbGetPicUrl = new Task.Callback<String>() {
        @Override
        public void onSuccess(Task task, String obj) {
            downloadSplashPic(obj);
        }

        @Override
        public void onFail(Task task, Object obj) {

        }
    };

    /**
     * 从服务器获取图片，在mLocalPicFolderPath中判断文件名是否相同
     * 仅当不同时，根据屏幕尺寸缩放并删除旧图再存储
     */
    public void updateLocalPic() {
        mTaskManager.addTask(new SplashPic.GetPicUrl(), mCbGetPicUrl);
    }

    private void downloadSplashPic(String url) {
        int w = SplashPic.getWidth();
        int h = SplashPic.getHeight();
        PicTask picTask = new PicTask();
        picTask.setPicSize(w, h);
        picTask.setPicUrl(url);
        picTask.setPicFolderPath(SplashPic.getLocalPicPath());
        mTaskManager.addTask(picTask, mCbDownloadPic);
    }

    public SplashPresenter(IView viewImpl) {
        if (viewImpl != null) {
            mViewImpl = viewImpl;
        }
    }

    public void init(Context context) {
        SplashPic.init(context);
    }

    /**
     * 主线程调用，设置Splash图片
     */
    public void loadLocalPic() {
        if (mViewImpl != null) {
            int w = getWidth();
            int h = getHeight();
            String path = SplashPic.getLocalPicPath();
            mBitmap = PicTool.getBmp(path, w, h);
            if (mBitmap != null) {
                mViewImpl.showPic(mBitmap);
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mViewImpl != null) {
            mViewImpl.hidePic();
        }
        if (mBitmap != null) {
            if (!mBitmap.isRecycled()) {
                mBitmap.recycle();
            }
            mBitmap = null;
        }
    }

    public interface IView {
        void showPic(Bitmap bmp);

        void hidePic();
    }
}
