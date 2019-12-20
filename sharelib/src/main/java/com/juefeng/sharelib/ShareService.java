package com.juefeng.sharelib;

import android.app.Activity;
import android.graphics.Bitmap;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.*;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.umeng.socialize.utils.UmengText;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ShareService {
    private Object imgUrl = "";
    private Activity activity;
    private String shareContent = "";
    private String shareTitle = "";
    private String targetUrl = "";
    private File file = null;


    private ShareListener shareListener;
    private UMShareListener umShareListener;

    private ShareBoardlistener shareBoardlistener;

    private static Map<SHARE_MEDIA, String> category;

    static {
        category = new HashMap<SHARE_MEDIA, String>();
        category.put(SHARE_MEDIA.WEIXIN_CIRCLE, "310010");
        category.put(SHARE_MEDIA.WEIXIN, "310020");
        category.put(SHARE_MEDIA.QZONE, "310040");
        category.put(SHARE_MEDIA.QQ, "310050");
    }

    public void setDebug(boolean bool) {
        com.umeng.socialize.utils.Log.LOG = bool;
    }

    public void openShareBoard(Activity activity, String shareUrl, String shareTitle, String shareContent, String imgUrl, ShareListener shareListener) {
        this.activity = activity;
        this.targetUrl = shareUrl;
        this.shareTitle = shareTitle;
        this.shareContent = shareContent;
        this.imgUrl = imgUrl;
        this.shareListener = shareListener;
        initShareCallBack();
        ShareBoardConfig config = new ShareBoardConfig();
        config.setCancelButtonVisibility(false);
        config.setTitleVisibility(false);
        config.setIndicatorVisibility(false);
        new ShareAction(activity)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE).setShareboardclickCallback(shareBoardlistener).open(config);
    }

    public void share(Activity activity, String shareUrl, String shareTitle, String shareContent, Object imgUrl, ShareListener shareListener, SHARE_MEDIA share_media) {
        this.activity = activity;
        this.targetUrl = shareUrl;
        this.shareTitle = shareTitle;
        this.shareContent = shareContent;
        this.imgUrl = imgUrl;
        this.shareListener = shareListener;
        initShareCallBack();
        goShare(share_media);
    }


    public void shareImage(Activity activity, File file, ShareListener shareListener, SHARE_MEDIA share_media) {
        this.activity = activity;
        this.file = file;
        this.shareListener = shareListener;
        initShareCallBack();
        goShareImage(share_media);
    }


    public void share(Activity activity, ShareListener shareListener, SHARE_MEDIA share_media) {
        this.activity = activity;
        this.shareListener = shareListener;
        initShareCallBack();
        goShare(share_media);
    }

    private String getShareType(SHARE_MEDIA sMedia) {
        return category.get(sMedia) == null ? "310030" : category.get(sMedia);
    }


    private void goShare(SHARE_MEDIA share_media) {
        String url = targetUrl + "&shareType=" + getShareType(share_media);
        UMWeb web = new UMWeb(url);
        web.setTitle(shareTitle);
        if (imgUrl instanceof File) {
            //图片文件
            web.setThumb(new UMImage(activity, (File) imgUrl));
        } else if (imgUrl instanceof String) {
            //图片链接
            web.setThumb(new UMImage(activity, (String) imgUrl));
        } else {
            if (imgUrl instanceof Integer) {
                //本地图片
                web.setThumb(new UMImage(activity, (Integer) imgUrl));
            } else if (imgUrl instanceof byte[]) {
                //图片字节
                web.setThumb(new UMImage(activity, (byte[]) imgUrl));
            } else {
                if (!(imgUrl instanceof Bitmap)) {
                    throw new RuntimeException(UmengText.UNKNOW_UMIMAGE);
                }
            }
        }
        web.setDescription(shareContent);
        new ShareAction(activity)
                .setPlatform(share_media).withMedia(web).setCallback(umShareListener).share();
    }

    private void goShareImage(SHARE_MEDIA share_media) {
        if (file == null) {
            return;
        }
        UMImage image = new UMImage(activity, file);
        new ShareAction(activity)
                .setPlatform(share_media).withMedia(image).setCallback(umShareListener).share();
    }

    private void initShareCallBack() {
        umShareListener = new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                if (shareListener != null) {
                    shareListener.shareSuccess(share_media);
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                if (shareListener != null) {
                    shareListener.shareCancel();
                }
            }
        };

        shareBoardlistener = new ShareBoardlistener() {
            @Override
            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                if (share_media == SHARE_MEDIA.QQ) {
                    goShare(share_media);
                } else if (share_media == SHARE_MEDIA.QZONE) {
                    goShare(share_media);
                } else if (share_media == SHARE_MEDIA.WEIXIN) {
                    goShare(share_media);
                } else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                    goShare(share_media);
                }
            }
        };
    }

    public interface ShareListener {
        void shareSuccess(SHARE_MEDIA share_media);

        void shareCancel();
    }
}
