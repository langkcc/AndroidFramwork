package com.juefeng.android.framework;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/28
 * Time: 13:53
 * Description:
 */
public interface LKImageLoader {

    /**
     * @param url       url
     * @param imageView image view
     */
    void loadImage(String url, ImageView imageView);
    /**
     * @param url       url
     * @param imageView image view
     */
    void loadRoundImage(String url, ImageView imageView);
    /**
     * @param url           url
     * @param imageView     image view
     * @param failedDrawing when failed show drawing
     */
    void loadImage(String url, ImageView imageView, int failedDrawing);

    /**
     * @param url           url
     * @param imageView     image view
     * @param failedDrawing when failed show drawing
     */
    void loadRoundImage(String url, ImageView imageView, int failedDrawing);

    /**
     * @param url           url
     * @param imageView     image view
     * @param failedDrawing when failed show drawing
     * @param width         drawing width
     * @param height        drawing height
     */
    void loadImage(String url, ImageView imageView, int failedDrawing, int width, int height);

    /**
     * @param url             url
     * @param imageView       image view
     * @param progressDrawing progressing drawing
     * @param failedDrawing   when failed show drawing
     */
    void loadImage(String url, ImageView imageView, int progressDrawing, int failedDrawing);

    /**
     * @param url             url
     * @param imageView       image view
     * @param progressDrawing progressing drawing
     * @param failedDrawing   when failed show drawing
     */
    void loadRoundImage(String url, ImageView imageView, int progressDrawing, int failedDrawing);

    /**
     * @param url             url
     * @param imageView       image view
     * @param progressDrawing progressing drawing
     * @param failedDrawing   when failed show drawing
     * @param width           drawing width
     * @param height          drawing height
     */
    void loadImage(String url, ImageView imageView, int progressDrawing, int failedDrawing, int width, int height);

    /**
     * @param url             url
     * @param imageView       image view
     * @param progressDrawing progressing drawing
     * @param failedDrawing   when failed show drawing
     * @param width           drawing width
     * @param height          drawing height
     */
    void loadRoundImage(String url, ImageView imageView, int progressDrawing, int failedDrawing, int width, int height);

    /**
     * clean image cache
     */
    void cleanCache();
}
