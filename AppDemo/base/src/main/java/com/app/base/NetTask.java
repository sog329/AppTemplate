/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jack on 2017/3/25 0025.
 */

public abstract class NetTask extends Task {
    private Map<String, String> mMapData = new HashMap<String, String>();

    public abstract String getHttpUrl();

    public abstract boolean isGet();

    @Override
    public void onSuccess(Object obj) {
        if (!isCancel()) {
            Object result = parseResult(obj);
            super.onSuccess(result);
        }
    }

    public abstract Object parseResult(Object obj);

    public void put(String key, String value) {
        if (!mHasSend) {
            mMapData.put(key, value);
        }
    }

    protected Map<String, String> getMap() {
        return mMapData;
    }

    @Override
    public void run() {
        NetTool.send(this);
    }

    @Override
    public void onDestroy() {
        synchronized(this) {
            mCallback = null;
        }
    }
}
