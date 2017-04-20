/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.base;

/**
 * Created by Jack on 2017/3/16 0016.
 */

public abstract class Task {
    protected boolean mIsCancel = false;
    protected boolean mHasSend = false; // 记录是否被投递，包含内部变量不再修改
    private String mTaskId = null;
    protected Callback[] mCallback = new Callback[] {null};

    public abstract void run();

    protected void setCallback(NetTask.Callback callback) {
        synchronized(mCallback) {
            mCallback[0] = callback;
        }
    }

    public void onSuccess(Object obj) {
        if (!mIsCancel) {
            synchronized(mCallback) {
                if (mCallback[0] != null) {
                    mCallback[0].onSuccess(this, obj);
                }
            }
        }
    }

    public void onFail(Object obj) {
        if (!mIsCancel) {
            synchronized(mCallback) {
                if (mCallback[0] != null) {
                    mCallback[0].onFail(this, obj);
                }
            }
        }
    }

    public void onDestroy() {
        synchronized(mCallback) {
            mCallback[0] = null;
        }
    }

    public void setTaskId(String taskId) {
        if (!mHasSend) {
            mTaskId = taskId;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Task && obj != null) {
            boolean isSame = getClass().equals(obj.getClass());
            // 先判断是同类型，再判断ID
            if (isSame) {
                Task task = (Task) obj;
                String id = task.mTaskId;
                if (id != null) {
                    isSame = id.equals(mTaskId);
                } else {
                    isSame = false;
                }
            }
            return isSame;
        } else {
            return super.equals(obj);
        }
    }

    public void cancel() {
        synchronized(this) {
            mIsCancel = true;
        }
    }

    public boolean isCancel() {
        return mIsCancel;
    }

    protected void hasSend() {
        mHasSend = true;
    }

    public interface Callback<T, O> {
        void onSuccess(T task, O obj);

        void onFail(T task, Object obj);
    }
}
