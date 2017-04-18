/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.demo.main.model;

import com.app.base.NetTask;

/**
 * Created by Jack on 17/4/1.
 */

public class LoginTask extends NetTask {
    @Override
    public String getHttpUrl() {
        return "https://passport.jd.com/new/Login.aspx";
    }

    @Override
    public boolean isGet() {
        return false;
    }

    @Override
    public Object parseResult(Object obj) {
        return obj;
    }

}
