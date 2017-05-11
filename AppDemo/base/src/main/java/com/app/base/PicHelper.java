/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.base;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;

/**
 * Created by Jack on 2017/3/16 0016.
 */

public class PicHelper {
    private PicCache mPicCache = null;
    private String mPicFolderPath = null;
    private AdapterHelper mAdapterHelper = new AdapterHelper();
    private TaskManager mTaskManager = null;
    private ArrayList<String> mAryDoing = new ArrayList<String>();
    private Map<String, Integer> mMapError = new HashMap<String, Integer>();
    //    private AdapterCallback mAdapterCallback = null;
    private IvCallback mCallback = null;
    private Task.Callback mCbTask = new Task.Callback<PicTask, Bitmap>() {
        @Override
        public void onSuccess(PicTask task, Bitmap bmp) {
            putPic(task.mPicUrl, bmp);
            onGetPic(task.mPicUrl);
        }

        @Override
        public void onFail(PicTask task, Object obj) {
            PicTask picTask = (PicTask) task;
            synchronized (mMapError) {
                if (mMapError.containsKey(picTask.mPicUrl)) {
                    mMapError.put(picTask.mPicUrl, mMapError.get(picTask.mPicUrl) + 1);
                } else {
                    mMapError.put(picTask.mPicUrl, 1);
                }
            }
            LogTool.debug(picTask.mPicUrl);
        }
    };

    private class PicCacheTask extends PicTask {
        @Override
        public void run() {
            if (mPicCache.hasPic(mPicUrl)) {
                onGetPic(mPicUrl);
            } else {
                synchronized (mAryDoing) {
                    if (mAryDoing.contains(mPicUrl)) {
                        return;
                    }
                    mAryDoing.add(mPicUrl);
                }
                super.run();
                synchronized (mAryDoing) {
                    mAryDoing.remove(mPicUrl);
                }
            }
        }
    }

    private ArrayList<ImageView> mAryIv = new ArrayList<ImageView>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg != null) {
                String url = (String) msg.obj;
                if (mAdapterHelper.mIsScrolling) {
                    onGetPic(url);
                } else {
                    synchronized (mAryIv) {
                        for (ImageView iv : mAryIv) {
                            iv.onGetPic(url);
                        }
                    }
                }
            }
        }
    };

    private View.OnAttachStateChangeListener mListener = new View.OnAttachStateChangeListener() {
        @Override
        public void onViewAttachedToWindow(View view) {
            if (view != null) {
                synchronized (mAryIv) {
                    mAryIv.remove(view);
                    mAryIv.add((ImageView) view);
                    //((ImageView) view).refresh();
                }
            }
        }

        @Override
        public void onViewDetachedFromWindow(View view) {
            if (view != null) {
                synchronized (mAryIv) {
                    mAryIv.remove(view);
                }
            }
        }
    };

    public PicHelper(int mMaxMemCount) {
        this(mMaxMemCount, 1, android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }

    public PicHelper(int mMaxMemCount, int threadSize) {
        this(mMaxMemCount, threadSize, android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }

    public PicHelper(int maxMemCount, int threadSize, int threadPriority) {
        mPicCache = new PicCache(maxMemCount);
        mTaskManager = new TaskManager(threadSize, threadPriority);
        mTaskManager.setName("PicHelper");
    }

    public synchronized void isScrolling(boolean isScrolling) {
        mAdapterHelper.mIsScrolling = isScrolling;
        if (isScrolling) {
            mTaskManager.pause();
        } else {
            mTaskManager.resume();
        }
        // 儅列表停下，开始进行预加载
        //        if (!isScrolling && mAdapterHelper.mVisibleItemCount > 0) {
        //            if (mAdapterCallback != null) {
        //                if (mAdapterHelper.mIsUp) {
        //                    int lastScreenStart = mAdapterHelper.mFirstVisibleItem - mAdapterHelper.mVisibleItemCount;
        //                    int lastScreenEnd = mAdapterHelper.mFirstVisibleItem - 1;
        //                    for (int i = lastScreenEnd; i > lastScreenStart; i--) {
        //                        String picUrl = mAdapterCallback.getPicUrl(i);
        //                        if (picUrl != null) {
        //                            getPic(picUrl, picUrl);
        //                        }
        //                    }
        //                } else {
        //                    int nextScreenStart = mAdapterHelper.mFirstVisibleItem + mAdapterHelper.mVisibleItemCount;
        //                    int nextScreenEnd = nextScreenStart + mAdapterHelper.mVisibleItemCount;
        //                    for (int i = nextScreenStart; i < nextScreenEnd; i++) {
        //                        String picUrl = mAdapterCallback.getPicUrl(i);
        //                        if (picUrl != null) {
        //                            getPic(picUrl, picUrl);
        //                        }
        //                    }
        //                }
        //            }
        //        }
    }

    //    public void setScrollIndex(int firstVisibleItem, int visibleItemCount) {
    //        if (mAdapterHelper.mFirstVisibleItem > firstVisibleItem) {
    //            mAdapterHelper.mIsUp = true;
    //        } else if (mAdapterHelper.mFirstVisibleItem < firstVisibleItem) {
    //            mAdapterHelper.mIsUp = false;
    //        }
    //        mAdapterHelper.mFirstVisibleItem = firstVisibleItem;
    //        mAdapterHelper.mVisibleItemCount = visibleItemCount;
    //        if (visibleItemCount > 0) {
    //            int size = visibleItemCount * 2;
    //            mPicCache.updateMaxMemCount(size);
    //        }
    //    }

    //    public synchronized void setAdapterCallback(AdapterCallback callback) {
    //        mAdapterCallback = callback;
    //    }

    public void setPicFolderPath(String picFolderPath) {
        if (picFolderPath != null) {
            if (picFolderPath.endsWith(File.separator)) {
                picFolderPath.substring(0, picFolderPath.length() - 1);
            }
        }
        mPicFolderPath = picFolderPath;
    }

    // 销毁栈底Bitmap对象是在锁Map中完成的
    private void putPic(String key, Bitmap bmp) {
        mPicCache.putPic(key, bmp);
    }

    public Bitmap getPic(String key, int w, int h) {
        return getPic(null, key, w, h);
    }

    // 每次取图片，若有则更改一次map中的序列
    public Bitmap getPic(String id, String key, int w, int h) {
        Bitmap bmp = mPicCache.getPic(key);
        if (bmp == null) {
            PicCacheTask task = new PicCacheTask();
            task.setPicUrl(key);
            task.setTaskId(id);
            task.setPicSize(w, h);
            task.setPicFolderPath(mPicFolderPath);
            mTaskManager.addTask(task, mCbTask);
        }
        return bmp;
    }

    public void attach(ImageView view) {
        if (view != null) {
            view.setPicCache(this);
            view.addOnAttachStateChangeListener(mListener);
        }
    }

    private void onGetPic(String url) {
        Message msg = mHandler.obtainMessage();
        msg.obj = url;
        mHandler.sendMessage(msg);
    }

    private class AdapterHelper {
        private boolean mIsScrolling = false;
        //        private boolean mIsUp = false;
        //        private int mFirstVisibleItem = 0;
        //        private int mVisibleItemCount = 0;
    }

    //    public interface AdapterCallback {
    //        String getPicUrl(int index);
    //    }

    public synchronized void showAnim(ImageView iv) {
        if (iv != null) {
            if (mCallback != null) {
                Animation anim = mCallback.getChangeAnimation();
                if (anim != null) {
                    iv.clearAnimation();
                    iv.startAnimation(anim);
                }
            }
        }
    }

    public synchronized void showDefaultPic(ImageView iv) {
        if (iv != null) {
            if (mCallback != null) {
                mCallback.setDefaultPic(iv);
            }
        }
    }

    public synchronized void setIvCallback(IvCallback callback) {
        mCallback = callback;
    }

    public interface IvCallback {
        Animation getChangeAnimation();

        void setDefaultPic(ImageView iv);
    }

    public void onDestroy() {
        mTaskManager.clearTask();
        mPicCache.clear();
    }
}
