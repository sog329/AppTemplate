/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.base;

import java.util.LinkedHashMap;
import java.util.Map;

import android.graphics.Bitmap;

/**
 * Created by Jack on 2017/3/22 0022.
 */

public class PicCache {
    private LinkedHashMap<String, Bitmap> mMapBmp = new LinkedHashMap<String, Bitmap>();
    private int mMaxMemCount = 0;

    public PicCache(int maxMemCount) {
        mMaxMemCount = maxMemCount;
    }

    public void updateMaxMemCount(int maxMemCount) {
        if (maxMemCount > 0) {
            if (maxMemCount > mMaxMemCount) {
                mMaxMemCount = maxMemCount;
            } else if (maxMemCount * 2 < mMaxMemCount) {
                mMaxMemCount = maxMemCount;
                cleanTo(maxMemCount);
            }
        }
    }

    // 判断并清理Map中对象到制定数量
    private void cleanTo(int n) {
        while (mMapBmp.size() > n) {
            for (Map.Entry<String, Bitmap> entry : mMapBmp.entrySet()) {
                String k = entry.getKey();
                Bitmap del = entry.getValue();
                mMapBmp.remove(k);
                if (del != null && !del.isRecycled()) {
                    del.recycle();
                    del = null;
                }
                break;
            }
        }
    }

    // 每次取图片，若有则更改一次map中的序列
    public Bitmap getPic(String key) {
        Bitmap bmp = null;
        synchronized(mMapBmp) {
            bmp = mMapBmp.get(key);
            if (bmp != null) {
                mMapBmp.remove(key);
                mMapBmp.put(key, bmp);
            }
        }
        return bmp;
    }

    // 销毁栈底Bitmap对象是在锁Map中完成的
    public void putPic(String key, Bitmap bmp) {
        if (bmp != null && !bmp.isRecycled()) {
            synchronized(mMapBmp) {
                if (mMapBmp.get(key) == null) {
                    mMapBmp.put(key, bmp);
                }
                cleanTo(mMaxMemCount);
            }
        }
    }

    // 不改变顺序，判断Cache中是否有对应图片
    public boolean hasPic(String key) {
        boolean b = false;
        synchronized(mMapBmp) {
            b = mMapBmp.get(key) != null;
        }
        return b;
    }

    //清空Map中数据
    public void clear() {
        synchronized(mMapBmp) {
            cleanTo(0);
        }
    }
}
