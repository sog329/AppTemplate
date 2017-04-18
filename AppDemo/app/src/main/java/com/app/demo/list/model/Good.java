/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.demo.list.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.app.base.LogTool;
import com.app.base.NetTask;

/**
 * Created by Jack on 17/4/1.
 */

public class Good {
    private String mName = null;
    private String mPicUrl = null;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPicUrl() {
        return mPicUrl;
    }

    public void setPicUrl(String picUrl) {
        mPicUrl = picUrl;
    }

    public static class GetListTask extends NetTask {
        @Override
        public String getHttpUrl() {
            return "https://suggest.taobao.com/sug";
        }

        @Override
        public boolean isGet() {
            return true;
        }

        @Override
        public Object parseResult(Object obj) {
            ArrayList<Good> lst = new ArrayList<Good>();
            String str = (String) obj;
            LogTool.debug(str);
            try {
                JSONObject jsonObject = new JSONObject(str);
                JSONArray result = jsonObject.getJSONArray("result");
                String ary[] = {"nightlife", "abstract", "animals", "business", "cats", "city", "food", "fashion", "people", "nature", "sports", "technics", "transport"};
                for (String s : ary) {
                    String picUrl = "http://lorempixel.com/400/300/" + s + "/";
                    StringBuilder sb = null;
                    for (int i = 0; i < result.length(); i++) {
                        JSONArray item = result.getJSONArray(i);
                        Good good = new Good();
                        sb = new StringBuilder();
                        sb.append(i);
                        sb.append("\n");
                        sb.append(item.get(0).toString());
                        sb.append("\n");
                        sb.append(item.get(1).toString());
                        sb.append("\n");
                        sb.append(s);
                        sb.append(i);
                        good.setName(sb.toString());
                        sb = new StringBuilder();
                        sb.append(picUrl);
                        sb.append(i);
                        good.setPicUrl(sb.toString());
                        lst.add(good);
                    }
                }
            } catch (JSONException e) {
                onFail(e.toString());
            }
            return lst;
        }
    }
}
