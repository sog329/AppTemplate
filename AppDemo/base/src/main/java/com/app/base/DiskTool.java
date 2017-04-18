/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.base;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * Created by Jack on 2017/3/16 0016.
 */

public class DiskTool {
    private static String mAppFolderPath = null;

    public static void init(Context context){
        if (hasSd()) {
            PackageInfo info = DeviceTool.getAppPackageInfo(context);
            if (info != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(getSDCardPath());
                sb.append(File.separator);
                sb.append(info.packageName);
                mAppFolderPath = sb.toString();
            }
        }
    }

    /**
     * 获取内置SDCard路径
     *
     * @return
     */
    public static String getSDCardPath() {
        try {
            return Environment.getExternalStorageDirectory().getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getAppFolderPath() {
        return mAppFolderPath;
    }

    /**
     * 检查指定路径是否存在文件夹，若不存在则创建
     *
     * @param path
     *
     * @return
     */
    public static boolean checkFolder(String path) {
        boolean b = false;
        try {
            if (path != null && hasSd()) {
                File dir = new File(path);
                if (dir.exists()) {
                    b = true;
                } else {
                    b = dir.mkdirs();
                    if(!b){
                        File file = new File(getSDCardPath());
                        LogTool.debug("file.canRead="+file.canRead()+";   file.canWrite()="+file.canWrite());
                        LogTool.debug("dir.mkdirs() = " + b);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }

    /**
     * 判断是否有SD卡
     *
     * @return
     */
    public static boolean hasSd() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private static String getImageCachePicUrl(String folderPath, String picUrl) {
        String hashUrl = PicTool.ToBKDRHash(picUrl);
        StringBuilder sb = new StringBuilder();
        sb.append(folderPath);
        sb.append(File.separator);
        sb.append(hashUrl);
        return sb.toString();
    }

    public static void removeImageFromSd(String folderPath, String picUrl) {
        if (picUrl != null && hasSd()) {
            String url = getImageCachePicUrl(folderPath, picUrl);
            File file = new File(url);
            if (file.exists()) {
                file.delete();
                file = null;
            }
        }
    }

    public static Bitmap getImageFromSd(String folderPath, String picUrl) {
        if (picUrl == null || !hasSd()) {
            return null;
        } else {
            Bitmap bmp = null;
            try {
                String url = getImageCachePicUrl(folderPath, picUrl);
                bmp = BitmapFactory.decodeFile(url);
            } catch (OutOfMemoryError e) {
                if (bmp != null) {
                    if (!bmp.isRecycled()) {
                        bmp.recycle();
                    }
                    bmp = null;
                }
            } catch (Exception e) {
                if (bmp != null) {
                    if (!bmp.isRecycled()) {
                        bmp.recycle();
                    }
                    bmp = null;
                }
            }
            return bmp;
        }
    }

    public static boolean saveImageToSd(Bitmap bmp, String folderPath, String picUrl) {
        boolean b = false;
        if (checkFolder(folderPath) && hasSd() && bmp != null) {
            String imagePath = getImageCachePicUrl(folderPath, picUrl);
            try {
                File f = new File(imagePath);
                boolean clean = true;
                if (f.exists()) {
                    clean = f.delete();
                }
                if (clean) {
                    FileOutputStream out = null;
                    try {
                        if (f.createNewFile()) {
                            out = new FileOutputStream(f);
                            b = bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                            out.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogTool.debug(e.toString());
                    } finally {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (Exception e) {
                            } finally {
                                out = null;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return b;
    }
}
