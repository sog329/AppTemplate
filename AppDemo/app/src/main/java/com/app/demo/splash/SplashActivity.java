/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.demo.splash;

import com.app.base.LogTool;
import com.app.base.MemLeakHelper;
import com.app.demo.R;
import com.app.demo.main.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashActivity extends Activity implements SplashPresenter.IView {
    private Handler mHandler = new Handler();
    private SplashPresenter mPresenter = new SplashPresenter(this);
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mPresenter.init(this);
        mPresenter.loadLocalPic();
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
        mHandler.postDelayed(mRunnable, 3000);
        LogTool.debug("");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
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
        mPresenter.onDestroy();
        mHandler.removeCallbacks(mRunnable);
        MemLeakHelper.check(this);
    }

    @Override
    public void showPic(Bitmap bmp) {
        if (bmp != null) {
            ImageView bg = (ImageView) findViewById(R.id.splash_bg);
            bg.setImageBitmap(bmp);
        }
    }

    @Override
    public void hidePic() {
        ImageView bg = (ImageView) findViewById(R.id.splash_bg);
        bg.setImageBitmap(null);
    }
}
