package com.juefeng.androidframework;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.juefeng.android.framework.LKUtil;
import com.juefeng.android.framework.common.base.BaseActivity;
import com.juefeng.android.framework.common.util.LogUtil;
import com.juefeng.android.framework.common.util.XmlUtil;
import com.juefeng.android.framework.down.DownHandler;
import com.juefeng.android.framework.http.EncryptoEntity;
import com.juefeng.android.framework.http.request.BaseFileAttachment;
import com.juefeng.android.framework.http.responce.HttpAsycResponceHandler;
import com.juefeng.android.framework.http.responce.HttpUploadResponceHandler;
import com.juefeng.android.framework.view.annotation.LKContentView;
import com.juefeng.android.framework.view.annotation.LKInjectView;
import com.juefeng.androidframework.entity.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@LKContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {


    @LKInjectView(R.id.test)
    TextView test;
    @LKInjectView(R.id.encrypto)
    TextView encrypto;

    @LKInjectView((R.id.imagetest))
    ImageView imageView;


    @Override
    protected void initEvent() {
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseFileAttachment attachment = new BaseFileAttachment();
                File file = new File(LKUtil.getAppConfig().getExternalImageCacheDir(),"test.txt");
                if (!file.exists()){
                    try {
                        file.createNewFile();
                        String test = "test";
                        FileOutputStream out = new FileOutputStream(file);
                        out.write(test.getBytes());
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                attachment.setImageId(file.getName());
                attachment.setFile(file);
                LKUtil.getHttpManager().updateFile("http://117.23.5.142:10080/trade/v1/tradeGoods/uploadByNative",attachment,uploadHandler);
//                File file = new File(LKUtil.getAppConfig().getExternalDownCacheDir(),"office.exe");
//                LKUtil.getLkDownFileManager().downFile("https://s1.music.126.net/download/pc/cloudmusicsetup_2_2_1[192801].exe",file,1, downHandler);
            }
        });
        encrypto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LKUtil.getLkHttpManager().get("http://192.168.2.115:80/encrypto",httpAsycResponceHandler);
                Map<String, String> map = new HashMap<>();
                map.put("1", "2");
                LKUtil.getHttpManager().post("http://192.168.2.115:80/encrypto", map, httpAsycResponceHandler);

            }
        });
    }


    @Override
    protected void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("gameId", "1");
        LKUtil.getHttpManager().post("http://117.23.5.142:10080/strategy/v1/raiderType/getRaidersTypeData",map, httpAsycResponceHandler);
        LKUtil.getImageLoader().loadRoundImage("http://tse3.mm.bing.net/th?id=OIP.O-QoMwNBHQDOicRUltTgtwEsEs&pid=15.1", imageView);
//        Map<String, String> map = new HashMap<>();
////        map.put("appId", "gts045");
////        map.put("appType", "com.juefeng.android.fristblood");
////        map.put("verCode", 1 + "");
////        map.put("channel", "other");
////        AppVersionUpdateService<VersionUpdateBean> appVersionUpdateService = new AppVersionUpdateService(MainActivity.this, "http://192.168.2.119/sysmanager/v1/sysAppVersion/checkUpgrade",
////                map, VersionUpdateBean.class, new AppVersionUpdateService.OnUpdateListener<VersionUpdateBean>() {
////
////            @Override
////            public void success(VersionUpdateBean t) {
////                LogUtil.d("success:"+t.getUrl());
////            }
////
////            @Override
////            public void nothing() {
////                LogUtil.d("nothing");
////            }
////        });
////        appVersionUpdateService.update();
    }

    HttpAsycResponceHandler<List<User>> testHandler = new HttpAsycResponceHandler<List<User>>(true) {
        @Override
        public void onSuccess(List<User> users) {
            LKUtil.getDBManager().saveOrUpdate(users);
            User user = LKUtil.getDBManager().findFirst(User.class);
            Log.d(MainActivity.class.getName(), user.getName());
        }

        @Override
        public void onFailed(int code, String message) {
            Log.d(MainActivity.class.getName(), message);
        }

        @Override
        public void onError() {
            Log.d(MainActivity.class.getName(), "error");
        }
    };

    HttpAsycResponceHandler<EncryptoEntity> httpAsycResponceHandler = new HttpAsycResponceHandler<EncryptoEntity>() {
        @Override
        public void onSuccess(EncryptoEntity users) {
            XmlUtil.generationXml(users, LKUtil.getAppConfig().getCryptoCacheFile());
            LKUtil.enableCrypto();
        }

        @Override
        public void onFailed(int code, String message) {
            Log.d(MainActivity.class.getName(), message);
        }

        @Override
        public void onError() {
            Log.d(MainActivity.class.getName(), "error");
        }
    };

    HttpAsycResponceHandler<List<User>> post = new HttpAsycResponceHandler<List<User>>(true) {
        @Override
        public void onSuccess(List<User> users) {
            LKUtil.getDBManager().saveOrUpdate(users);
            User user = LKUtil.getDBManager().findFirst(User.class);
            Log.d(MainActivity.class.getName(), user.getName());
        }

        @Override
        public void onFailed(int code, String message) {
            Log.d(MainActivity.class.getName(), message);
        }

        @Override
        public void onError() {
            Log.d(MainActivity.class.getName(), "error");
        }
    };

    DownHandler downHandler = new DownHandler() {
        @Override
        public void onSuccess(String path) {
            LogUtil.d("下载成功。。。。" + path);
        }

        @Override
        public void onFailed() {

        }
    };

    HttpUploadResponceHandler<String> uploadHandler = new HttpUploadResponceHandler<String>() {
        @Override
        public void onSuccess(String path) {
            LogUtil.d("上传成功。。。。" + path);
        }


    };

}
