/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.base;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Jack on 2017/3/16 0016.
 */

public class NetTool {
    private static final Map<String, String> mMap = new HashMap<String, String>();
    private static final int CONNECT_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;

    public static final void init() {
        // TODO: 2017/3/23 0023 初始化HashMap
    }

    public static void send(NetTask task) {
        if (task.isGet()) {
            httpGet(task);
        } else {
            httpPost(task);
        }
    }

    private static void httpGet(NetTask task) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection httpURLConnection = null;
        BufferedReader reader = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(task.getHttpUrl());
            Map<String, String> tmp = new HashMap<String, String>();
            if (task.getMap() != null) {
                tmp.putAll(task.getMap());
            }
            tmp.putAll(mMap);
            String content = paramToString(tmp);
            if (!content.isEmpty()) {
                sb.append("?");
                sb.append(content);
            }
            URL url = new URL(sb.toString());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true); // 从httpUrlConnection读入
            httpURLConnection.setUseCaches(false); // 不使用缓存
            httpURLConnection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
            httpURLConnection.setReadTimeout(READ_TIMEOUT);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            int resultCode = httpURLConnection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == resultCode) {
                String readLine = null;
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                while ((readLine = reader.readLine()) != null) {
                    result.append(readLine);
                }
                reader.close();
                reader = null;
            } else {
                task.onFail(Integer.toString(resultCode));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            task.onFail(e.toString());
        } catch (Exception e) {
            e.printStackTrace();
            task.onFail(e.toString());
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            task.onSuccess(result.toString());
        }
    }

    private static void httpPost(NetTask task) {
        StringBuilder result = new StringBuilder();
        DataOutputStream out = null;
        HttpURLConnection httpURLConnection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(task.getHttpUrl());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true); //post需要把参数写入http正文中
            httpURLConnection.setDoInput(true); // 从httpUrlConnection读入
            httpURLConnection.setUseCaches(false); // 不使用缓存
            httpURLConnection.setRequestProperty("Content-type", "application/x-java-serialized-object");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
            httpURLConnection.setReadTimeout(READ_TIMEOUT);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();
            Map<String, String> tmp = new HashMap<String, String>();
            if (task.getMap() != null) {
                tmp.putAll(task.getMap());
            }
            tmp.putAll(mMap);
            String content = paramToString(tmp);
            out = new DataOutputStream(httpURLConnection.getOutputStream());
            out.writeBytes(content);
            out.flush();
            out.close();
            out = null;
            int resultCode = httpURLConnection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == resultCode) {
                String readLine = null;
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
                while ((readLine = reader.readLine()) != null) {
                    result.append(readLine);
                }
                reader.close();
                reader = null;
            } else {
                task.onFail(Integer.toString(resultCode));
            }
            httpURLConnection.disconnect();
            httpURLConnection = null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            task.onFail(e.toString());
        } catch (Exception e) {
            e.printStackTrace();
            task.onFail(e.toString());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            task.onSuccess(result.toString());
        }
    }

    private static String paramToString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String val = entry.getValue();
                try {
                    val = URLEncoder.encode(val, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(entry.getKey()).append("=").append(val);
            }
        }
        return sb.toString();
    }

    public static Bitmap downloadPic(String picUrl) {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            if (picUrl != null) {
                url = new URL(picUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(CONNECT_TIMEOUT);
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setDoInput(true);
                conn.connect();
                is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
            }
        } catch (FileNotFoundException e) {
            LogTool.debug(e.toString());
        } catch (IOException e) {
            LogTool.debug(e.toString());
        } catch (OutOfMemoryError e) {
            LogTool.debug(e.toString());
        } catch (Exception e) {
            LogTool.debug(e.toString());
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    is = null;
                }
            }
        }
        return bitmap;
    }
}
