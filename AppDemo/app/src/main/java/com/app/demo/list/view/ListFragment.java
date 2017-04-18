/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.demo.list.view;

import java.util.ArrayList;

import com.app.base.LogTool;
import com.app.demo.R;
import com.app.demo.list.ListPresenter;
import com.app.demo.list.model.Good;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Jack on 2017/4/2 0002.
 */

public class ListFragment extends Fragment implements ListPresenter.IView {
    private View mRoot = null;
    private LvAdapter mAdapter = null;
    private ListPresenter mListPresenter = new ListPresenter(this);

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        LogTool.debug("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        LogTool.debug("");
        if (inflater != null) {
            mRoot = inflater.inflate(R.layout.fragment_list, parent, false);
            ListView lv = (ListView) mRoot.findViewById(R.id.main_lv);
            mAdapter = new LvAdapter(lv);
            lv.setAdapter(mAdapter);
            mListPresenter.getGoods("kindle");
            mListPresenter.login("Jack", "123");
            mListPresenter.updateLocalPic();

        }
        return mRoot;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogTool.debug("");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogTool.debug("");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogTool.debug("");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogTool.debug("");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogTool.debug("");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogTool.debug("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogTool.debug("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogTool.debug("");
        mListPresenter.onDestroy();
        mAdapter.onDestroy();
        mRoot = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogTool.debug("");
    }

    @Override
    public void onGetGoodsBegin() {

    }

    @Override
    public void onGetGoodsSuccess(ArrayList<Good> lst) {
        mAdapter.update(lst);
    }

    @Override
    public void onGetGoodsFail(Object obj) {

    }

    @Override
    public void onLoginBegin() {

    }

    @Override
    public void onLoginSuccess(Object obj) {
        LogTool.debug((String) obj);
    }

    @Override
    public void onLoginFail(Object obj) {
        LogTool.debug((String) obj);
    }
}
