/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.demo.list;

import java.util.ArrayList;

import com.app.base.NetTask;
import com.app.base.Presenter;
import com.app.base.TaskManager;
import com.app.demo.list.model.Good;
import com.app.demo.main.model.LoginTask;

/**
 * Created by Jack on 2017/3/26 0026.
 */

public class ListPresenter extends Presenter {
    private IView mViewImpl = null;
    private TaskManager mTaskManager = new TaskManager();

    private final NetTask.Callback mCbGetGoods = new NetTask.Callback<NetTask, ArrayList<Good>>() {
        @Override
        public void onSuccess(NetTask task, ArrayList<Good> lst) {
            if (mViewImpl != null) {
                mViewImpl.onGetGoodsSuccess(lst);
            }
        }

        @Override
        public void onFail(NetTask task, Object obj) {
            if (mViewImpl != null) {
                mViewImpl.onGetGoodsFail(obj);
            }
        }
    };

    private final NetTask.Callback mCbLogin = new NetTask.Callback() {
        @Override
        public void onSuccess(Object task, Object obj) {
            if (mViewImpl != null) {
                mViewImpl.onLoginSuccess(obj);
            }
        }

        @Override
        public void onFail(Object task, Object obj) {
            if (mViewImpl != null) {
                mViewImpl.onLoginFail(obj);
            }
        }
    };

    public ListPresenter(IView viewImpl) {
        mViewImpl = viewImpl;
    }

    public void updateLocalPic() {

    }

    public void getGoods(String str) {
        Good.GetListTask getListTask = new Good.GetListTask();
        getListTask.put("code", "utf-8");
        getListTask.put("q", str);
        mViewImpl.onGetGoodsBegin();
        mTaskManager.addTask(getListTask, mCbGetGoods);
    }

    public void login(String n, String p) {
        LoginTask loginTask = new LoginTask();
        loginTask.put("username", n);
        loginTask.put("password", p);
        mViewImpl.onLoginBegin();
        mTaskManager.addTask(loginTask, mCbLogin);
        //        netHelper.cancel(Login.class);
    }

    public void onDestroy() {
        mTaskManager.clearTask();
    }

    public interface IView {
        void onGetGoodsBegin();

        void onGetGoodsSuccess(ArrayList<Good> lst);

        void onGetGoodsFail(Object obj);

        void onLoginBegin();

        void onLoginSuccess(Object obj);

        void onLoginFail(Object obj);
    }
}
