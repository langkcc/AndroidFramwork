package com.juefeng.android.framework.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.juefeng.android.framework.LKUtil;
import com.juefeng.android.framework.common.base.Constant;
import com.juefeng.android.framework.common.ex.HttpException;
import com.juefeng.android.framework.common.util.*;
import com.juefeng.android.framework.http.base.HttpRequestor;
import com.juefeng.android.framework.http.base.HttpThread;
import com.juefeng.android.framework.http.base.RequestHandler;
import com.juefeng.android.framework.http.interfaces.IHttpManager;
import com.juefeng.android.framework.http.request.BaseFileAttachment;
import com.juefeng.android.framework.http.request.LKRequestParams;
import com.juefeng.android.framework.http.request.LKRequestParamsSerialiser;
import com.juefeng.android.framework.http.responce.BaseHttpResponceHandler;
import com.juefeng.android.framework.http.responce.HttpAsycResponceHandler;
import com.juefeng.android.framework.http.responce.HttpUploadResponceHandler;

import java.io.File;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/22
 * Time: 17:58
 * Description:Http manager
 */
public class HttpManagerImpl extends HttpCryptoManager implements IHttpManager {

    private static final String GET = "get";
    private static final String POST = "post";

    /**
     * default http request connection timeout
     */
    private int connectionTimeOut = 3000;
    /**
     * default http request socket timeout
     */
    private int socketTimeOut = 3000;


    private static Gson gson = new GsonBuilder().registerTypeAdapter(LKRequestParams.class, new LKRequestParamsSerialiser()).create();

    public static HttpManagerImpl httpManagerImpl;

    public HttpManagerImpl() {
        super();
    }

    /**
     * get instance of HttpManagerImpl
     *
     * @return
     */
    public static HttpManagerImpl registerInstance(int socketTimeout, int connectionTimeOut) {
        if (httpManagerImpl == null) {
            httpManagerImpl = new HttpManagerImpl();
        }
        httpManagerImpl.socketTimeOut = socketTimeout;
        httpManagerImpl.connectionTimeOut = connectionTimeOut;
        return httpManagerImpl;
    }


    @Override
    public void post(String url, Map<String, String> map, HttpAsycResponceHandler handler) {
        postNativer(url, map, handler);
    }

    @Override
    public void postBody(String url, Map body, HttpAsycResponceHandler handler) {
        body = fullParamsMap(body);
        String content = gson.toJson(body);
        postNativer(url, content, handler);
    }

    @Override
    public void post(String url, HttpAsycResponceHandler handler) {
        postNativer(url, "", handler);
    }

    @Override
    public void updateFile(String url, BaseFileAttachment listFile, HttpUploadResponceHandler handler) {
        uploadNativer(url, listFile, handler);
    }

    /**
     * post request send params
     *
     * @param url
     * @param map
     * @param handler
     */
    private void postNativer(String url, Map<String, String> map, HttpAsycResponceHandler handler) {
        if (handler == null) {
            throw new HttpException("Http Responce Handler must is not null!!!");
        }
        Map<String, String> headers = null;
        LogUtil.d("send http post request,url is:" + url);
        Map<String, String> params = generateParams(map);
        sendRequest(url, headers, params, null, HttpRequestor.Method.POST, handler);
    }

    /**
     * post request send body
     *
     * @param url
     * @param content
     * @param handler
     */
    private void postNativer(String url, String content, HttpAsycResponceHandler handler) {
        if (handler == null) {
            throw new HttpException("Http Responce Handler must is not null!!!");
        }
//        url = generateUrl(url, null);
        Map<String, String> headers = null;
        LogUtil.d("send http post request,url is:" + url);
        if (content != null && !content.equals("")) {
            LogUtil.d("send http post request,body is:" + content);
            if (isCrypto()) {
                String time = System.currentTimeMillis() + "";
                content = enCrypto(content, time);
                if (content != null) {
                    headers = initHeader(time);
                }
                LogUtil.d("send http post request,after enCrypto body is:" + content);
            }
        }
        sendRequest(url, headers, null, content, HttpRequestor.Method.POST, handler);
    }

    /**
     * upload file
     *
     * @param url
     * @param baseFileAttachment
     * @param handler
     */
    private void uploadNativer(String url, BaseFileAttachment baseFileAttachment, HttpUploadResponceHandler handler) {
        if (handler == null) {
            throw new HttpException("Http Responce Handler must is not null!!!");
        }
//        url = generateUrl(url, null);
        LogUtil.d("send http post request,url is:" + url);
        if (baseFileAttachment != null) {
            LogUtil.d("send http post request,upload file name is:" + baseFileAttachment.getImageId());
        }
        Map<String, String> map = new HashMap<>();
        map.put("imageId", baseFileAttachment.getImageId());
        Map<String, String> params = generateParams(map);
        uploadFileRequest(url, params, baseFileAttachment.getFile(), handler);
    }


    @Override
    public void get(String url, HttpAsycResponceHandler handler) {
        getNativer(url, null, handler);
    }

    @Override
    public void get(String url, Map<String, String> map, HttpAsycResponceHandler handler) {
        getNativer(url, map, handler);
    }

    /**
     * get request send params
     *
     * @param url
     * @param map
     * @param handler
     */
    private void getNativer(String url, Map<String, String> map, HttpAsycResponceHandler handler) {
        if (handler == null) {
            throw new HttpException("Http Responce Handler must is not null!!!");
        }
        Map<String, String> params = null;
        if (map != null && !map.isEmpty()) {
            params = generateParams(map);
        }
        LogUtil.d("send http get request,url is:" + url);
        sendRequest(url, null, params, null, HttpRequestor.Method.GET, handler);
    }

    /**
     * generate params and sign
     *
     * @param map params
     * @return
     */
    private Map<String, String> generateParams(Map<String, String> map) {
        try {
            map = fullParamsMap(map);
            map = sortMap(map);
            StringBuilder parBuild = new StringBuilder();
            for (String key : map.keySet()) {
                if (map.get(key) != null) {
                    parBuild.append(URLEncoder.encode(key.replace(" ", "%20"), "utf8")).append("=").append(URLEncoder.encode(map.get(key).replace(" ", "%20"), "utf8")).append("&");
                }
            }
            LogUtil.d("send http request params is: " + parBuild.toString());
            String signResult = SignUtil.sign(parBuild.toString() + ResourceUtil.getString(LKUtil.app(), "slie"));
            map.put("sign", signResult);
            return map;
        } catch (Exception e) {
            throw new HttpException("Http URLEncoder enc is not support!!!", e);
        }
    }

    /**
     * generate url and sign
     *
     * @param url url
     * @param map params
     * @return
     */
//    private String generateUrl(String url, Map<String, String> map) {
//        try {
//            map = fullParamsMap(map);
//            map = sortMap(map);
//            StringBuilder parBuild = new StringBuilder();
//            for (String key : map.keySet()) {
//                if (map.get(key) != null) {
//                    parBuild.append(URLEncoder.encode(key.replace(" ", "%20"), "utf8")).append("=").append(URLEncoder.encode(map.get(key).replace(" ", "%20"), "utf8")).append("&");
//                }
//            }
//            LogUtil.d("send http get request params is: " + parBuild.toString());
//            String signResult = SignUtil.sign(parBuild.toString() + ResourceUtil.getString(LKUtil.app(), "slie"));
//            parBuild.append("sign=").append(signResult);
//            StringBuilder stringBuilder = new StringBuilder(url);
//            stringBuilder.append("?");
//            stringBuilder.append(parBuild.toString());
//            return stringBuilder.toString();
//        } catch (Exception e) {
//            throw new HttpException("Http URLEncoder enc is not support!!!", e);
//        }
//    }

    /**
     * full base params
     *
     * @param map params
     * @return
     */
    private Map<String, String> fullParamsMap(Map<String, String> map) {
        if (map == null) {
            map = new HashMap<>(10);
        }
        map.put(Constant.PLATFORM, "Android");
        map.put(Constant.DEVICE_ID, DeviceUtil.getDeviceId(LKUtil.app()));
        map.put(Constant.PHONE_BRAND, DeviceUtil.getPhoneBrand());
        map.put(Constant.PHONE_MODEL, DeviceUtil.getPhoneModel());
        map.put(Constant.SYS_VERSION, DeviceUtil.getBuildVersion());
        map.put(Constant.SYS_LEVEL, DeviceUtil.getBuildLevel() + "");
        map.put(Constant.PHONE_NET, LKUtil.getNetTypeName());
        map.put(Constant.CURRENT_TIME, System.currentTimeMillis() + "");
        map.put(Constant.APP_VERSION_CODE, DeviceUtil.getVersionCode(LKUtil.app()));
        map.put(Constant.APP_VERSION_NAME, LKUtil.app().getPackageName());
        return map;
    }

    /**
     * sort map
     *
     * @param map
     * @return
     */
    private Map<String, String> sortMap(Map<String, String> map) {
        TreeMap<String, String> tm = new TreeMap<String, String>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        tm.putAll(map);
        return tm;
    }

    /**
     * before send request,handle logic
     * judge net connection online
     *
     * @param handler
     */
    public boolean handlerNetPre(BaseHttpResponceHandler handler) {
        if (!LKUtil.netActivity()) {
            handler.onError();
            return false;
        }
        handler.onStart();
        return true;
    }


    public void uploadFileRequest(String url, Map<String, String> params, File file, HttpUploadResponceHandler handler) {
        if (!handlerNetPre(handler)) {
            handler.onFinish();
            return;
        }
        List<File> list = new ArrayList(1);
        list.add(file);
        nativeRequest(url, null, params, null, list, HttpRequestor.Method.UPLOAD, handler);
    }

    @Override
    public void sendRequest(String url, Map<String, String> headMap, Map<String, String> params, String content, HttpRequestor.Method method, HttpAsycResponceHandler handler) {
        if (!handlerNetPre(handler)) {
            handler.onFinish();
            return;
        }
        if (method == HttpRequestor.Method.GET) {
            nativeRequest(url, headMap, params, null, null, HttpRequestor.Method.GET, handler);
        } else if (method == HttpRequestor.Method.POST) {
            nativeRequest(url, headMap, params, content, null, HttpRequestor.Method.POST, handler);
        }
    }

    @Override
    public void handlerSuccess(String content, BaseHttpResponceHandler handler) {
        if (handler.isCanceled()) {
            return;
        }
        handler.parseContent(content);
    }

    @Override
    public void handlerFailed(BaseHttpResponceHandler handler) {
        if (handler.isCanceled()) {
            return;
        }
        LogUtil.d("server return error");
        handler.onError();
    }

    /**
     * send http request
     *
     * @param url     target url
     * @param content when method is post,this is body
     * @param method  get or post
     * @param handler callback service
     * @return
     */
    private void nativeRequest(String url, Map<String, String> header, Map<String, String> params, String content, List<File> files, HttpRequestor.Method method, final BaseHttpResponceHandler handler) {
        try {
            if (method == null) {
                return;
            }
            //init HttpRequestor
            HttpRequestor requestor;

            RequestHandler requestHandler = new RequestHandler() {
                @Override
                public void onSuccess(String content, boolean isCrypto, String time) {
                    try {
                        LogUtil.d("server returned,content:" + content);
                        if (isCrypto) {
                            content = deCrypto(content, time);
                            if (content == null) {
                                LogUtil.d("server returned,deCrypto failed.");
                                handlerFailed(handler);
                                return;
                            }
                            LogUtil.d("server returned,after deCrypto centent is" + content);
                        }
                        handlerSuccess(content, handler);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        handler.onFinish();
                    }
                }

                @Override
                public void onFailed() {
                    try {
                        handlerFailed(handler);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        handler.onFinish();
                    }
                }
            };
            if (header != null && header.containsKey(Constant.CURRENT_TIME)) {
                requestHandler.setRequestTime(header.get(Constant.CURRENT_TIME));
            }
            HttpRequestor.Builder builder = new HttpRequestor.Builder().setConnectionTimeOut(this.getConnectionTimeOut()).setFiles(files)
                    .setSocketTimeOut(this.getSocketTimeOut()).setUrl(url).setBody(content).setParams(params).setRequestHandler(requestHandler);
            //send http request
            if (method == HttpRequestor.Method.GET) {
                builder = builder.get();
            } else if (method == HttpRequestor.Method.POST) {
                builder = builder.post();
            } else if (method == HttpRequestor.Method.UPLOAD) {
                builder = builder.upload();
            }
            if (header != null) {
                builder.addRequestHeaderAll(header);
            }
            requestor = builder.build();
            handler.setHttpRequestor(requestor);
            AppThreadPool.execute(new HttpThread(requestor));
        } catch (Exception e) {
            LogUtil.w(e);
        }
    }


    public int getConnectionTimeOut() {
        return this.connectionTimeOut;
    }

    @Override
    public void setConnectionTimeOut(int connectionTimeOut) {
        this.connectionTimeOut = connectionTimeOut;
    }

    public int getSocketTimeOut() {
        return this.socketTimeOut;
    }

    @Override
    public void setSocketTimeOut(int socketTimeOut) {
        this.socketTimeOut = socketTimeOut;
    }


}
