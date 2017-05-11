/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.demo.list.view;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app.base.DiskTool;
import com.app.base.ImageView;
import com.app.base.PicHelper;
import com.app.demo.R;
import com.app.demo.list.model.Good;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 15/8/19.
 */
public class LvAdapter extends BaseAdapter {
    private Handler mHandler = new Handler();
    private PicHelper mPicHelper = new PicHelper(20, 3);
    private List<Good> mLstData = new ArrayList<Good>();
    private Runnable mRnUpdate = new Runnable() {
        @Override
        public void run() {
            notifyDataSetChanged();
        }
    };

    public LvAdapter(ListView lv) {
        if (DiskTool.hasSd()) {
            StringBuilder sb = new StringBuilder();
            sb.append(DiskTool.getAppFolderPath());
            sb.append(File.separator);
            sb.append("tmpPic");
            mPicHelper.setPicFolderPath(sb.toString());
        }
        mPicHelper.setIvCallback(new PicHelper.IvCallback() {
            @Override
            public Animation getChangeAnimation() {
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1);
                int average = 400;
                int base = 300;
                int duration = base + (int) (2 * (average - base) * Math.random());
                alphaAnimation.setDuration(duration);
                return alphaAnimation;
            }

            @Override
            public void setDefaultPic(ImageView iv) {
                iv.setImageResource(android.R.color.darker_gray);
            }
        });
        //        mPicHelper.setAdapterCallback(new PicHelper.AdapterCallback() {
        //            @Override
        //            public String getPicUrl(int index) {
        //                String picUrl = null;
        //                Good model = (Good) getItem(index);
        //                if (model != null) {
        //                    picUrl = model.getPicUrl();
        //                }
        //                return picUrl;
        //            }
        //        });
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int state) {
                if (state == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    mPicHelper.isScrolling(true);
                } else {
                    mPicHelper.isScrolling(false);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //                mPicHelper.setScrollIndex(firstVisibleItem, visibleItemCount);
            }
        });
    }

    @Override
    public int getCount() {
        synchronized(mLstData) {
            return mLstData.size();
        }
    }

    @Override
    public Object getItem(int position) {
        Good model = null;
        synchronized(mLstData) {
            try {
                model = mLstData.get(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return model;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LvItemHolder holder = null;
        if (convertView == null) {
            Context context = parent.getContext();
            convertView = LayoutInflater.from(context).inflate(R.layout.main_lv_item, null);
            holder = new LvItemHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.lv_item_tv);
            holder.iv = (ImageView) convertView.findViewById(R.id.lv_item_iv);
            mPicHelper.attach(holder.iv);
            convertView.setTag(holder);
        } else {
            holder = (LvItemHolder) convertView.getTag();
        }
        Good model = (Good) getItem(position);
        if (model != null) {
            // 赋值
            holder.tv.setText(model.getName());
            holder.iv.setPicUrl(model.getPicUrl());
        }
        return convertView;
    }

    public void update(List<Good> lst) {
        synchronized(mLstData) {
            mLstData.clear();
            mLstData.addAll(lst);
        }
        mHandler.post(mRnUpdate);
    }

    public void onDestroy() {
        mHandler.removeCallbacks(mRnUpdate);
        mPicHelper.onDestroy();
    }

    private class LvItemHolder {
        TextView tv = null;
        ImageView iv = null;
    }
}
