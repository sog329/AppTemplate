/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.demo.main;

import com.app.base.LogTool;
import com.app.base.MemLeakHelper;
import com.app.demo.R;
import com.app.demo.list.view.ListFragment;
import com.app.demo.main.view.MainPagerAdapter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ListFragment());
        adapter.addFragment(new ListFragment());
        adapter.addFragment(new ListFragment());
        viewPager.setAdapter(adapter);

//        FragmentManager fragmentManager = getSupportFragmentManager();
//        int id = R.id.main_fragment_container;
//        Fragment fragment = fragmentManager.findFragmentById(id);
//        if (fragment == null) {
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.add(id, fragment);
//            fragmentTransaction.commit();
//        }

        LogTool.debug("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogTool.debug("");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogTool.debug("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogTool.debug("");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogTool.debug("");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogTool.debug("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogTool.debug("");
        MemLeakHelper.check(this);
    }
}
