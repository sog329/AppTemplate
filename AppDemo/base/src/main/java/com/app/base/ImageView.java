/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * Created by Jack on 2017/3/19 0019.
 */

public class ImageView extends android.widget.ImageView {
    private String mPicUrl = null;
    private PicHelper mPicHelper = null;
    private boolean mHasSetPic = false;
    private int mWidth = 0;
    private int mHeight = 0;

    public ImageView(Context context) {
        super(context);
    }

    public ImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void setPicCache(PicHelper picHelper) {
        mPicHelper = picHelper;
    }

    public boolean setPicUrl(String url) {
        boolean hasChange = false;
        Bitmap bmp = null;
        if (url == null) {
            mHasSetPic = false;
        } else if (mPicHelper != null) {
            if (mWidth > 0 && mHeight > 0) {
                bmp = mPicHelper.getPic(Integer.toString(hashCode()), url, mWidth, mHeight);
            }
            if (bmp != null) {
                if (url.equals(mPicUrl)) {
                    if (!mHasSetPic) {
                        hasChange = true;
                    }
                } else {
                    hasChange = true;
                }
                mHasSetPic = true;
            } else {
                mHasSetPic = false;
            }
        }
        mPicUrl = url;
        if (bmp == null) {
            setDefaultPic();
        } else if (hasChange) {
            setImageBitmap(bmp);
        }
        return hasChange;
    }

    public void onGetPic(String url) {
        if (mPicUrl != null && mPicUrl.equals(url)) {
            if (setPicUrl(url)) {
                if (mPicHelper != null) {
                    mPicHelper.showAnim(this);
                }
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (RuntimeException e) {
            setPicUrl(mPicUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefaultPic() {
        setImageBitmap(null);
        if (mPicHelper != null) {
            mPicHelper.showDefaultPic(this);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = right - left;
        mHeight = bottom - top;
        if (changed) {
            setPicUrl(mPicUrl);
        }
    }
}
