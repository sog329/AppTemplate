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

public class MsgCenter {
    private static List<SoftReference<Msg>> mLstMsg = new ArrayList();
    private static TaskManager mTaskManager = new TaskManager();

    /**
     * 实际只存储作为成员变量/implements的Msg对象，匿名类会被丢弃，因为无法注销
     * @param msg
     */
    public static void register(Msg msg) {
        if (msg != null) {
            mLstMsg.add(new SoftReference(msg));
        }
    }

    public static void unregister(Msg msg) {
        for (SoftReference<Msg> softReference : mLstMsg) {
            Msg message = softReference.get();
            if (message != null && message.equals(msg)) {
                mLstMsg.remove(message);
            }
        }
    }

    public static void notify(final String id) {
        notify(id, null);
    }

    public static void notify(final String id, final Object obj) {
        Task task = new Task() {
            @Override
            public void run() {
                if (id != null) {
                    LogTool.debug("id=" + id);
                    for (SoftReference<Msg> softReference : mLstMsg) {
                        Msg msg = softReference.get();
                        if (msg != null) {
                            LogTool.debug("    msg.getId()=" + msg.getId());
                            if (id.equals(msg.getId())) {
                                msg.notify(obj);
                            }
                        } else {
                            LogTool.debug("msg==null");
                        }
                    }
                }
            }
        };
        task.setTaskId(id);
        mTaskManager.addTask(task);
    }

    public static void clear() {
        mLstMsg.clear();
    }

    public interface Msg<T> {
        String getId();

        void notify(T t);
    }

}
