package com.juefeng.android.framework.image;

import android.graphics.*;
import android.widget.ImageView;
import com.juefeng.android.framework.LKUtil;
import com.juefeng.android.framework.common.util.LogUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/27
 * Time: 17:22
 * Description:
 */
public class ImageDecode implements Runnable {


    /**
     * image url
     */
    private String path;
    private int failedDrawing;
    private int progressDrawing;
    private int width;
    private int height;
    private int pixles;
    private ImageHandler callback;

    public ImageDecode(String path, int failedDrawing, int progressDrawing, int width, int height, int pixles, ImageHandler callback) {
        this.path = path;
        this.failedDrawing = failedDrawing;
        this.progressDrawing = progressDrawing;
        this.width = width;
        this.height = height;
        this.pixles = pixles;
        this.callback = callback;
    }

    @Override
    public void run() {
        deCode();
    }


    /**
     * Do GET request
     *
     * @return
     * @throws Exception
     */
    public void deCode() {
        Bitmap bitmap;
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opts);
            int btwidth = opts.outWidth;
            int btheight = opts.outHeight;
            if (width > 0 && height > 0) {
                int s = 1;
                if (btwidth > width && btheight > height) {
                    if (btwidth > btheight) {
                        s = Math.round(btheight / height);
                    } else {
                        s = Math.round(btwidth / width);
                    }
                }
                opts.inSampleSize = s;
            }
            opts.inJustDecodeBounds = false;
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmap = BitmapFactory.decodeFile(path, opts);
            bitmap = compressBitmap(bitmap, 100);
            if (bitmap != null) {
                if (pixles > 0) {
                    bitmap = toRoundCornerImage(bitmap, pixles);
                }
                callback.sendMessage(callback.obtainMessage(ImageHandler.SUCCESS, bitmap));
            } else {
                if (failedDrawing > 0) {
                    bitmap = BitmapFactory.decodeResource(LKUtil.app().getResources(), failedDrawing);
                    if (pixles > 0) {
                        bitmap = toRoundCornerImage(bitmap, pixles);
                    }
                    callback.sendMessage(callback.obtainMessage(ImageHandler.SUCCESS, bitmap));
                } else {
                    callback.sendMessage(callback.obtainMessage(ImageHandler.FAILED));
                }
            }
        } catch (Exception e) {
            LogUtil.w(e);
            callback.sendMessage(callback.obtainMessage(ImageHandler.FAILED));
        }
    }


    /**
     * compress bitmap
     *
     * @param bitmap
     * @param sizeLimit
     * @return
     */
    private Bitmap compressBitmap(Bitmap bitmap, long sizeLimit) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        // loop
        while (baos.toByteArray().length / 1024 > sizeLimit) {
            // clean baos
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 10;
        }
        Bitmap newBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()), null, null);
        return newBitmap;
    }

    /**
     * 获取圆角位图的方法
     *
     * @param bitmap 需要转化成圆角的位图
     * @param pixels 圆角的度数，数值越大，圆角越大
     * @return 处理后的圆角位图
     */
    private Bitmap toRoundCornerImage(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        // 抗锯齿
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

}
