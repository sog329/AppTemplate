/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.demo.main.view;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Jack on 2017/4/17 0017.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mLstFragment = new ArrayList<Fragment>();

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        synchronized(mLstFragment) {
            if (position < mLstFragment.size()) {
                fragment = mLstFragment.get(position);
            }
        }
        return fragment;
    }

    @Override
    public int getCount() {
        synchronized(mLstFragment) {
            return mLstFragment.size();
        }
    }

    public void addFragment(Fragment fragment) {
        synchronized(mLstFragment) {
            mLstFragment.add(fragment);
        }
    }
}
