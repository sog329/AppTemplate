/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.base;

import java.util.ArrayList;

/**
 * Created by Jack on 2017/3/16 0016.
 */

public class TaskManager {
    private ArrayList<Task> mAryTask = new ArrayList<Task>();
    private ArrayList<Task> mAryDoing = new ArrayList<Task>();
    private ArrayList<Thread> mAryThread = new ArrayList<Thread>();

    private Runnable mExecutorRun = null;
    private boolean mPause = false;
    private String mName = null;
    private int mMaxThread = 1;

    public void setName(String str) {
        mName = str;
    }

    public void addTask(Task task, Task.Callback callback) {
        if (task != null) {
            task.setCallback(callback);
        }
        addTask(task);
    }

    /**
     * 任务管理器中增加任务，并开启线程执行
     *
     * @param task
     */
    public void addTask(Task task) {
        if (task != null) {
            task.hasSend();
            ArrayList<Task> aryDel = new ArrayList<Task>();
            synchronized(mAryTask) {
                for (Task t : mAryTask) {
                    if (task.equals(t)) {
                        aryDel.add(t);
                        t.onDestroy();
                    }
                }
                mAryTask.removeAll(aryDel);
                mAryTask.add(task);
            }
            if (!mPause) {
                resume();
            }
        }
    }

    public void cancelTask(Class cls) {
        if (cls != null) {
            ArrayList<Task> aryDel = new ArrayList<Task>();
            synchronized(mAryTask) {
                for (Task t : mAryTask) {
                    if (t != null) {
                        if (t.getClass().equals(cls)) {
                            aryDel.add(t);
                            t.onDestroy();
                        }
                    }
                }
                mAryTask.removeAll(aryDel);
            }
            synchronized(mAryDoing) {
                for (Task t : mAryDoing) {
                    if (t != null) {
                        if (t.getClass().equals(cls)) {
                            t.cancel();
                        }
                    }
                }
            }
        }
    }

    public void clearTask() {
        synchronized(mAryTask) {
            for (Task t : mAryTask) {
                if (t != null) {
                    t.onDestroy();
                }
            }
            mAryTask.clear();
        }
        synchronized(mAryDoing) {
            for (Task t : mAryDoing) {
                if (t != null) {
                    t.cancel();
                    t.onDestroy();
                }
            }
        }
    }

    public TaskManager() {
        this(1, android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }

    /**
     * @param threadSize 线程池数目
     */
    public TaskManager(int threadSize) {
        this(threadSize, android.os.Process.THREAD_PRIORITY_BACKGROUND);
    }

    /**
     * @param threadSize     线程池数目
     * @param threadPriority 线程优先级
     */
    public TaskManager(int threadSize, final int threadPriority) {
        mMaxThread = threadSize;
        mExecutorRun = new Runnable() {
            @Override
            public void run() {
                try {
                    android.os.Process.setThreadPriority(threadPriority);
                    int i = 0;
                    Task task = getTask();
                    while (task != null) {
                        if (mName != null) {
                            LogTool.debug(mName + " " + mAryThread.indexOf(Thread.currentThread()) + "_" + i++);
                        }
                        synchronized(mAryDoing) {
                            mAryDoing.add(task);
                        }
                        try {
                            task.run();
                        } catch (Exception e) {
                            task.onFail(e.toString());
                        } catch (Error e) {
                            task.onFail(e.toString());
                        }
                        synchronized(mAryDoing) {
                            mAryDoing.remove(task);
                        }
                        task.onDestroy();
                        task = getTask();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                synchronized(mAryThread) {
                    mAryThread.remove(Thread.currentThread());
                }
            }
        };
    }

    private Task getTask() {
        Task task = null;
        if (!mPause) {
            synchronized(mAryTask) {
                if (mAryTask.size() > 0) {
                    task = mAryTask.get(0);
                    mAryTask.remove(0);
                }
            }
            if (task != null && task.isCancel()) {
                task.onDestroy();
                task = getTask();
            }
        }
        if (mAryTask.size() > 0) {
            executeTask();
        }
        return task;
    }

    public void pause() {
        mPause = true;
    }

    public void resume() {
        mPause = false;
        executeTask();
    }

    private void executeTask() {
        synchronized(mAryThread) {
            if (mAryThread.size() < mMaxThread) {
                Thread t = new Thread(mExecutorRun);
                mAryThread.add(t);
                t.start();
            }
        }
    }
}
