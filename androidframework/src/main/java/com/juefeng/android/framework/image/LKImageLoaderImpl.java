package com.juefeng.android.framework.image;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.google.gson.Gson;
import com.juefeng.android.framework.LKImageLoader;
import com.juefeng.android.framework.LKUtil;
import com.juefeng.android.framework.common.util.AppThreadPool;
import com.juefeng.android.framework.common.util.FileUtil;
import com.juefeng.android.framework.common.util.LogUtil;
import com.juefeng.android.framework.common.util.StringUtil;
import com.juefeng.android.framework.http.base.HttpThread;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/27
 * Time: 16:01
 * Description:
 */
public class LKImageLoaderImpl implements LKImageLoader {


    /**
     * max cache image number
     */
    private static int MAXCACHE = 100;
    private static int PIXELS = 20;
    /**
     * whether enable memory cache
     */
    private static boolean isMemCache = true;

    private static File imageCacheKeyFile;

    private static LruCache<String, String> memCacheMap;

    private static Gson gson = new Gson();


    /**
     * instance
     */
    private static LKImageLoaderImpl instance;

    public static LKImageLoaderImpl registerInstance() {
        if (instance == null) {
            instance = new LKImageLoaderImpl();
        }
        return instance;
    }

    /**
     * @param url       url
     * @param imageView image view
     */
    @Override
    public void loadImage(String url, ImageView imageView) {
        loadImage(url, imageView, 0, 0);
    }

    @Override
    public void loadRoundImage(String url, ImageView imageView) {
        loadRoundImage(url, imageView, 0, 0);
    }

    /**
     * @param url           url
     * @param imageView     image view
     * @param failedDrawing when failed show drawing
     */
    @Override
    public void loadImage(String url, ImageView imageView, int failedDrawing) {
        loadImage(url, imageView, 0, failedDrawing);
    }

    @Override
    public void loadRoundImage(String url, ImageView imageView, int failedDrawing) {
        loadRoundImage(url, imageView, 0, failedDrawing);
    }

    /**
     * @param url           url
     * @param imageView     image view
     * @param failedDrawing when failed show drawing
     * @param width         drawing width
     * @param height        drawing height
     */
    @Override
    public void loadImage(String url, ImageView imageView, int failedDrawing, int width, int height) {
        loadImage(url, imageView, 0, failedDrawing, width, height);
    }

    /**
     * @param url             url
     * @param imageView       image view
     * @param progressDrawing progressing drawing
     * @param failedDrawing   when failed show drawing
     */
    @Override
    public void loadImage(String url, ImageView imageView, int progressDrawing, int failedDrawing) {
        loadImage(url, imageView, progressDrawing, failedDrawing, 0, 0);
    }

    @Override
    public void loadRoundImage(String url, ImageView imageView, int progressDrawing, int failedDrawing) {
        loadRoundImage(url, imageView, progressDrawing, failedDrawing, 0, 0);
    }

    /**
     * @param url             url
     * @param imageView       image view
     * @param progressDrawing progressing drawing
     * @param failedDrawing   when failed show drawing
     * @param width           drawing width
     * @param height          drawing height
     */
    @Override
    public void loadImage(String url, ImageView imageView, int progressDrawing, int failedDrawing, int width, int height) {
        if (!StringUtil.isUrl(url)) {
            loadFailedImage(imageView, failedDrawing, 0);
            return;
        }
        beforeLoad(imageView, progressDrawing, 0);
        String path = checkMemMap(url);
        if (path != null && !path.isEmpty()) {
            loadLocalImage(path, imageView, failedDrawing, progressDrawing, width, height, 0);
        } else {
            getNetImage(url, imageView, failedDrawing, progressDrawing, width, height, 0);
        }
    }

    /**
     * @param url             url
     * @param imageView       image view
     * @param progressDrawing progressing drawing
     * @param failedDrawing   when failed show drawing
     * @param width           drawing width
     * @param height          drawing height
     */
    @Override
    public void loadRoundImage(String url, ImageView imageView, int progressDrawing, int failedDrawing, int width, int height) {
        if (!StringUtil.isUrl(url)) {
            loadFailedImage(imageView, failedDrawing, PIXELS);
            return;
        }
        beforeLoad(imageView, progressDrawing, PIXELS);
        String path = checkMemMap(url);
        if (path != null && !path.isEmpty()) {
            File file = new File(path);
            if (!file.exists()) {
                getNetImage(url, imageView, failedDrawing, progressDrawing, width, height, PIXELS);
            } else {
                loadLocalImage(path, imageView, failedDrawing, progressDrawing, width, height, PIXELS);
            }
        } else {
            getNetImage(url, imageView, failedDrawing, progressDrawing, width, height, PIXELS);
        }
    }

    @Override
    public void cleanCache() {
        try {
            if (imageCacheKeyFile != null) {
                if (imageCacheKeyFile.exists()) {
                    imageCacheKeyFile.delete();
                }
            }
            memCacheMap = null;
        } catch (Exception e) {
            LogUtil.d("图片组件清除缓存出错了", e);
        }
    }


    /**
     * start download network image
     *
     * @param url
     * @param width
     * @param height
     */
    private void getNetImage(final String url, final ImageView imageView, final int failedDrawing,
                             final int progressDrawing, final int width, final int height, final int pixels) {
        try {
            loadProcessImage(imageView, progressDrawing, pixels);
            LogUtil.d("Get Image Url is:" + url);
            String path = new File(LKUtil.getAppConfig().getExternalImageCacheDir(), url.hashCode() + url.substring(url.lastIndexOf("."))).getAbsolutePath();
            ImageRequester requestor = new ImageRequester(url, path, new ImageReqesterHandler() {
                @Override
                public void onSuccess(String content) {
                    LogUtil.d("File cache path:" + content);
                    if (content != null && !content.isEmpty()) {
                        putMemcacheMap(url, content);
                        removeBeyondMax();
                        loadLocalImage(content, imageView, failedDrawing, progressDrawing, width, height, pixels);
                        syncCacheMapFile();
                    } else {
                        LogUtil.d("Image load failed from Network");
                        loadFailedImage(imageView, failedDrawing, pixels);
                    }
                }

                @Override
                public void onFailed() {
                    loadFailedImage(imageView, failedDrawing, pixels);
                }
            });
            AppThreadPool.execute(requestor);
        } catch (Exception e) {
            LogUtil.w(e);
            loadFailedImage(imageView, failedDrawing, pixels);
        }


    }

    /**
     * load local image file.
     *
     * @param path
     * @param imageView
     * @param width
     * @param height
     */
    private void loadLocalImage(String path, final ImageView imageView, final int failedDrawing,
                                int progressDrawing, int width, int height, final int pixels) {
        try {
            loadProcessImage(imageView, progressDrawing, pixels);
            if (width <= 0 || height <= 0) {
                if (imageView.getLayoutParams() != null) {
                    width = imageView.getLayoutParams().width;
                    height = imageView.getLayoutParams().height;
                } else {
                    width = imageView.getMeasuredWidth();
                    height = imageView.getMeasuredHeight();
                }
            }
            ImageDecode requestor = new ImageDecode(path, failedDrawing, progressDrawing, width, height, pixels, new ImageHandler() {
                @Override
                public void onSuccess(Bitmap bitmap) {
                    imageView.setImageBitmap(bitmap);
                }

                @Override
                public void onFailed() {
                    loadFailedImage(imageView, failedDrawing, pixels);
                }
            });
            AppThreadPool.execute(requestor);
        } catch (Exception e) {
            LogUtil.w(e);
            loadFailedImage(imageView, failedDrawing, pixels);
        }
    }

    /**
     * when load image failed,show failed drawing
     *
     * @param imageView
     * @param failedDrawing
     */
    private void loadFailedImage(ImageView imageView, int failedDrawing, int pixels) {
        try {
            if (failedDrawing == 0) {
                return;
            }
            Bitmap bitmap = BitmapFactory.decodeResource(LKUtil.app().getResources(), failedDrawing);
            if (pixels > 0) {
                bitmap = toRoundCornerImage(bitmap, pixels);
            }
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            LogUtil.w(e);
        }
    }


    private void loadProcessImage(ImageView imageView, int progressDrawing, int pixels) {
        try {
            if (progressDrawing != 0) {
                Bitmap bitmap = BitmapFactory.decodeResource(LKUtil.app().getResources(), progressDrawing);
                if (pixels > 0) {
                    bitmap = toRoundCornerImage(bitmap, pixels);
                }
                imageView.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            LogUtil.w(e);
        }
    }


    /**
     * before load image,show progress drawing
     *
     * @param imageView
     * @param progressDrawing
     */
    private void beforeLoad(ImageView imageView, int progressDrawing, int pixels) {
        loadProcessImage(imageView, progressDrawing, pixels);
    }


    /**
     * checke memory cache map whether contains url.
     * if contaions,return local file path.else return null.
     *
     * @param url
     * @return
     */
    private String checkMemMap(String url) {
        if (memCacheMap == null) {
            initCacheMap();
        }
        if (memCacheMap != null) {
            if (memCacheMap.containsKey(url)) {
                return memCacheMap.get(url);
            }
        }
        return null;
    }


    /**
     * put new file path in mem cache
     *
     * @param key
     * @param value
     */
    private void putMemcacheMap(String key, String value) {
        if (memCacheMap != null) {
            memCacheMap.put(key, value);
        }
    }

    /**
     * if current num beyond max.remove last
     */
    private void removeBeyondMax() {
        if (memCacheMap != null && memCacheMap.size() > MAXCACHE) {
            memCacheMap.remove(memCacheMap.size() - 1);
        }
    }

    /**
     * init cache map
     */
    private void initCacheMap() {
        try {
            if (imageCacheKeyFile == null) {
                imageCacheKeyFile = new File(LKUtil.getAppConfig().getExternalImageCacheDir(), "key.json");
            }
            if (!imageCacheKeyFile.exists()) {
                imageCacheKeyFile.createNewFile();
            } else {
                String content = FileUtil.readFile(imageCacheKeyFile);
                memCacheMap = gson.fromJson(content, LruCache.class);
            }
            if (memCacheMap == null) {
                memCacheMap = new LruCache(MAXCACHE);
            }
        } catch (Exception e) {
            LogUtil.w(e);
        }

    }


    /**
     * sync cache map to file
     */
    private void syncCacheMapFile() {
        try {
            if (imageCacheKeyFile == null) {
                imageCacheKeyFile = new File(LKUtil.getAppConfig().getExternalImageCacheDir(), "key.json");
            }
            if (!imageCacheKeyFile.exists()) {
                imageCacheKeyFile.createNewFile();
            }
            if (memCacheMap == null || memCacheMap.isEmpty()) {
                FileUtil.writeFile("", imageCacheKeyFile);
            } else {
                String content = gson.toJson(memCacheMap);
                FileUtil.writeFile(content, imageCacheKeyFile);
            }
        } catch (Exception e) {
            LogUtil.w(e);
        }

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
