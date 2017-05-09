/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.base;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 2017/5/6 0006.
 */

public class ObserverManager {
    private static List<SoftReference<Observer>> mLstObserver = new ArrayList();
    private static TaskManager mTaskManager = new TaskManager();

    public static void add(Observer observer) {
        mLstObserver.add(new SoftReference(observer));
    }

    public static void remove(Observer observer) {
        for (SoftReference<Observer> sr : mLstObserver) {
            Observer ob = sr.get();
            if (ob != null && ob.equals(observer)) {
                mLstObserver.remove(sr);
            }
        }
    }

    public static void notify(final String key, final Object obj) {
        Task task = new Task() {
            @Override
            public void run() {
                if (key != null) {
                    for (SoftReference<Observer> sr : mLstObserver) {
                        Observer ob = sr.get();
                        if (ob != null && key.equals(ob.getKey())) {
                            ob.notify(obj);
                        }
                    }
                }
            }
        };
        mTaskManager.addTask(task);
    }

    public static void clear() {
        mLstObserver.clear();
    }

    public interface Observer {
        String getKey();

        void notify(Object obj);
    }

}
