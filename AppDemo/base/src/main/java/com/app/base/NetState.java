/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by songxiaoguang on 2017/5/11.
 */

public class NetState extends BroadcastReceiver {
    public static final String NET_CHANGE = "msg_net_state_change";
    public static final String WIFI_ENABLE = "msg_wifi_enable";

    private static boolean mIsConnect = false;
    private static boolean mIsWifi = false;

    private static boolean mIsFirst = true;

    public static boolean isConnect() {
        return mIsConnect;
    }

    public static boolean isWifi() {
        return mIsWifi;
    }

    public static boolean isConnect(Context context) {
        judge(context);
        return mIsConnect;
    }

    public static boolean isWifi(Context context) {
        judge(context);
        return mIsWifi;
    }

    private static void judge(Context context) {
        if (context != null) {
            LogTool.debug("NetState: isConnect=" + NetState.isConnect() + ", isWifi=" + NetState.isWifi());
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                // Deal with the change of net state
                boolean isConnect = activeNetworkInfo.isConnected();
                if (mIsConnect != isConnect) {
                    mIsConnect = isConnect;
                    if (!mIsFirst) {
                        MsgCenter.notify(NET_CHANGE);
                        LogTool.debug("MsgCenter.notify(NET_CHANGE)");
                    }
                }
                // Deal with the change of wifi
                boolean isWifi = activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI;
                if (mIsConnect) {
                    if (mIsWifi != isWifi) {
                        mIsWifi = isWifi;
                        if (!mIsFirst) {
                            MsgCenter.notify(WIFI_ENABLE);
                            LogTool.debug("MsgCenter.notify(WIFI_ENABLE)");
                        }
                    }
                } else {
                    mIsWifi = isWifi;
                }
            } else {
                boolean isConnect = false;
                if (mIsConnect != isConnect) {
                    mIsConnect = isConnect;
                    if (!mIsFirst) {
                        MsgCenter.notify(NET_CHANGE);
                        LogTool.debug("MsgCenter.notify(NET_CHANGE)");
                    }
                }
                mIsWifi = false;
            }
        }
        mIsFirst = false;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        judge(context);
    }
}


