/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.app.base;

import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.Rect;

/**
 * @author: Jack
 * @version: 2015年5月11日
 * @description: 提供Bitmap
 */
public class PicTool {
    public static Bitmap load(String picPath, String diskPath, int w, int h, PicTask.Result result) {
        Bitmap bmp = null;
        //        diskPath = null;
        long startTime = System.currentTimeMillis();
        if (picPath != null) {
            if (picPath.toLowerCase().startsWith("http")) { // 如果是网络图片
                boolean canSave = false;
                if (DiskTool.checkFolder(diskPath)) {
                    canSave = true;
                    bmp = DiskTool.getImageFromSd(diskPath, picPath, w, h);
                }
                if (bmp == null) {
                    LogTool.debug(w + "*" + h);
                    bmp = NetTool.downloadPic(picPath, w, h);
                    bmp = PicTool.resizeBitmap(bmp, w, h);
                    if (canSave) {
                        DiskTool.saveImageToSd(bmp, diskPath, picPath, w, h);
                    }
                    if (result != null) {
                        result.isByNet = true;
                    }
                } else {
                    if (result != null) {
                        result.isByNet = false;
                    }
                }
            } else {
                bmp = PicTool.getLocalBmp(picPath, w, h);
                if (result != null) {
                    result.isByNet = false;
                }
            }
        }
        if (result != null) {
            result.costTimeMs = (int) (System.currentTimeMillis() - startTime);
        }
        return bmp;
    }
    //    public static Bitmap getBmp(String path, int k) {
    //        Bitmap bmp = null;
    //        if (path != null) {
    //            Options op = new Options();
    //            op.inJustDecodeBounds = false;
    //            op.inDither = false;
    //            op.inPreferredConfig = Bitmap.Config.ARGB_8888;
    //            op.inSampleSize = k;
    //            try {
    //                bmp = BitmapFactory.decodeFile(path, op);
    //            } catch (Exception e) {
    //                e.printStackTrace();
    //                bmp = null;
    //            } catch (OutOfMemoryError e) {
    //                e.printStackTrace();
    //                bmp = null;
    //            }
    //        }
    //        return bmp;
    //    }

    public static Rect getBmpRc(String path) {
        Rect rc = null;
        if (path != null) {
            Options op = new Options();
            op.inJustDecodeBounds = true;
            try {
                BitmapFactory.decodeFile(path, op);
                rc = new Rect(0, 0, op.outWidth, op.outHeight);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rc;
    }

    /**
     * 下载到本地的图片，和显示的图片不是等比缩放的，采用此函数进行居中的等比缩放
     *
     * @param path
     * @param w
     * @param h
     *
     * @return
     */
    public static Bitmap getLocalBmp(String path, int w, int h) {
        Bitmap bmp = null;
        if (path != null && w > 0 && h > 0) {
            Rect rc = getBmpRc(path);
            if (rc != null) {
                int srcW = rc.width();
                int srcH = rc.height();
                if (srcW > 0 && srcH > 0) {
                    float srcProportion = 1f * srcW / srcH;
                    float dstProportion = 1f * w / h;
                    if (srcProportion <= dstProportion) {
                        int scale = 1;
                        if (srcW > w) {
                            float k = 1f * srcW / w;
                            scale = (int) Math.ceil(k);
                            if (scale == 3) {
                                scale = 4;
                            }
                        }
                        Options op = new Options();
                        op.inJustDecodeBounds = false;
                        op.inSampleSize = scale;
                        Bitmap bmpSrc = BitmapFactory.decodeFile(path, op);
                        srcW = bmpSrc.getWidth();
                        srcH = bmpSrc.getHeight();
                        int dstH = (int) (srcW / dstProportion);
                        int x = 0;
                        int y = (srcH - dstH) / 2;
                        try {
                            bmp = Bitmap.createBitmap(bmpSrc, x, y, srcW, dstH);
                        } catch (Exception e) {
                            e.printStackTrace();
                            bmp = null;
                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();
                            bmp = null;
                        }
                        if (srcH != dstH) {
                            bmpSrc.recycle();
                            bmpSrc = null;
                        }
                    } else {
                        int scale = 1;
                        if (srcH > h) {
                            float k = 1f * srcH / h;
                            scale = (int) Math.ceil(k);
                            if (scale == 3) {
                                scale = 4;
                            }
                        }
                        Options op = new Options();
                        op.inJustDecodeBounds = false;
                        op.inSampleSize = scale;
                        Bitmap bmpSrc = BitmapFactory.decodeFile(path, op);
                        srcW = bmpSrc.getWidth();
                        srcH = bmpSrc.getHeight();
                        int dstW = (int) (srcH * dstProportion);
                        int x = (srcW - dstW) / 2;
                        int y = 0;
                        try {
                            bmp = Bitmap.createBitmap(bmpSrc, x, y, dstW, srcH);
                        } catch (Exception e) {
                            e.printStackTrace();
                            bmp = null;
                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();
                            bmp = null;
                        }
                        if (srcW != dstW) {
                            bmpSrc.recycle();
                            bmpSrc = null;
                        }
                    }
                }
            }
        }
        return bmp;
    }

    public static Bitmap getBmp(String path, int w, int h) {
        Bitmap bmp = null;
        if (path != null && w > 0 && h > 0) {
            Options op = new Options();
            op.inJustDecodeBounds = true;
            try {
                BitmapFactory.decodeFile(path, op);
                int srcW = op.outWidth;
                int srcH = op.outHeight;
                if (srcW > 0 && srcH > 0) {
                    int scale = srcW * srcH / w / h;
                    if (scale > 1) {
                        scale = (int) Math.sqrt(scale);
                        op.inJustDecodeBounds = false;
                        op.inSampleSize = scale;
                        bmp = BitmapFactory.decodeFile(path, op);
                    } else {
                        bmp = BitmapFactory.decodeFile(path);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                bmp = null;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                bmp = null;
            }
        }
        return bmp;
    }

    public static Bitmap getBmpByAssets(Context ct, String str) {
        Bitmap bmp = null;
        if (ct != null) {
            Resources res = ct.getResources();
            if (res != null) {
                AssetManager am = res.getAssets();
                if (am != null) {
                    InputStream is = null;
                    try {
                        is = am.open(str);
                        bmp = BitmapFactory.decodeStream(is);
                    } catch (Exception e) {
                        e.printStackTrace();
                        bmp = null;
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                        bmp = null;
                    } finally {
                        try {
                            if (is != null) {
                                is.close();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        return bmp;
    }

    public static Bitmap resizeBitmap(Bitmap bmp, int width, int height) {
        Bitmap newBmp = null;
        try {
            if (bmp != null && !bmp.isRecycled()) {
                if (bmp.getWidth() > width || bmp.getHeight() > height) {
                    Matrix matrix = new Matrix();
                    matrix.postScale(((float) width) / bmp.getWidth(), ((float) height) / bmp.getHeight());
                    newBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                    bmp.recycle();
                } else {
                    newBmp = bmp;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (newBmp != null) {
                if (!newBmp.isRecycled()) {
                    newBmp.recycle();
                    newBmp = null;
                }
            }
            newBmp = bmp;
        }
        return newBmp;
    }

    public static String ToBKDRHash(String imageUrl) {
        if (imageUrl == null) {
            return null;
        }
        int seed = 131;
        int hash = 0;
        int urlSize = imageUrl.length();
        for (int i = 0; i < urlSize; i++) {
            hash = (hash * seed) + imageUrl.charAt(i);
        }
        int result = hash & 0x7FFFFFFF;
        return String.valueOf(result);
    }
}
