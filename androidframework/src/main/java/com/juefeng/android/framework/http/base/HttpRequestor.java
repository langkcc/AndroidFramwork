package com.juefeng.android.framework.http.base;

import com.juefeng.android.framework.LKUtil;
import com.juefeng.android.framework.common.ex.HttpException;
import com.juefeng.android.framework.common.util.LogUtil;
import com.juefeng.android.framework.common.util.ResourceUtil;
import com.juefeng.android.framework.common.util.SignUtil;
import com.juefeng.android.framework.http.HttpCryptoManager;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: LangK
 * Date: 2017/9/26
 * Time: 13:37
 * Description:http util class
 */
public class HttpRequestor {

    private final String BOUNDARY = "---------------------------123821742118716";
    private final int HTTP_SUCCESS_CODE = 300;
    private String charset = "utf-8";
    /**
     * default is 3000
     */
    private Integer connectTimeout = 3000;
    /**
     * default is 3000
     */
    private Integer socketTimeout = 3000;
    private String proxyHost = null;
    private Integer proxyPort = null;

    /**
     * custom request header
     */
    private Map<String, String> headMap;

    /**
     * http method
     */
    private Method method;

    /**
     * http url
     */
    private String url;

    /**
     * http body
     */
    private String content;


    /**
     * http params
     */
    private Map<String, String> params;

    private RequestHandler requestHandler;

    private List<File> fileList;


    public void request() {
        try {
            Boolean encrypto;
            String content;
            Map<Boolean, String> result = null;
            if (method == Method.GET) {
                result = doGet(this.url, params);
            } else if (method == Method.POST) {
                result = doPost(this.url, params, this.content);
            }else if(method == Method.UPLOAD){
                result = uploadFile(url,params,fileList);
            }
            if (result == null || result.isEmpty()) {
                this.requestHandler.sendMessage(this.requestHandler.obtainMessage(RequestHandler.FAILED));
            } else {
                encrypto = (Boolean) result.keySet().toArray()[0];
                content = result.get(encrypto);
                if (encrypto) {
                    this.requestHandler.sendMessage(this.requestHandler.obtainMessage(RequestHandler.SUCCESSANDCRYPTO, content));
                } else {
                    this.requestHandler.sendMessage(this.requestHandler.obtainMessage(RequestHandler.SUCCESS, content));
                }
            }
        } catch (Exception e) {
            LogUtil.w(e);
            this.requestHandler.sendMessage(this.requestHandler.obtainMessage(RequestHandler.FAILED));
        }
    }

    /**
     * Do GET request
     *
     * @param url request url
     * @return
     * @throws Exception
     */
    public Map<Boolean, String> doGet(String url, Map<String, String> params) throws Exception {
        return doHttp(url, params, null, Method.GET);
    }

    /**
     * Do GET request
     *
     * @param url request url
     * @return
     * @throws Exception
     */
    public Map<Boolean, String> doPost(String url, Map<String, String> params, String body) throws Exception {
        return doHttp(url, params, body, Method.POST);
    }

    /**
     * Do GET request
     *
     * @param url request url
     * @return
     * @throws Exception
     */
    public Map<Boolean, String> uploadFile(String url, Map<String, String> params, List<File> fileList) throws Exception {
        String result = doUpload(url, params, fileList);
        Map map = new HashMap<Boolean, String>();
        map.put(false, result);
        return map;
    }

    /**
     * Do POST request
     *
     * @param url  request url
     * @param body params for http body
     * @return
     * @throws Exception
     */
    public Map<Boolean, String> doHttp(String url, Map<String, String> params, String body, Method method) throws Exception {
        Map<Boolean, String> result;
        boolean crypto = false;
        URL localURL = new URL(url);
        URLConnection connection = this.openConnection(localURL);
        renderRequest(connection);
        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
        httpURLConnection.setRequestMethod(method.getValue());
        httpURLConnection = initHeader(httpURLConnection);
        if (body != null && !body.isEmpty()) {
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
        } else {
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            if (params != null && !params.isEmpty()) {
                body = generateParamsString(params);
            }
        }
        httpURLConnection.connect();
        OutputStream outputStream = null;
        OutputStreamWriter outputStreamWriter = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        try {
            if (body != null && !body.isEmpty()) {
                outputStream = httpURLConnection.getOutputStream();
                outputStreamWriter = new OutputStreamWriter(outputStream);
                outputStreamWriter.write(body.toString());
                outputStreamWriter.flush();
            }
            if (httpURLConnection.getResponseCode() >= HTTP_SUCCESS_CODE) {
                if (connection != null) {
                    try {
                        connection.getInputStream().close();
                    } catch (Exception e) {
                    }
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                throw new Exception("HTTP Request is not success!!! Response code is " + httpURLConnection.getResponseCode());
            }
            crypto = checkCrypto(httpURLConnection);
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
        } finally {
            if (outputStreamWriter != null) {
                outputStreamWriter.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                try {
                    connection.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        result = new HashMap<>();
        result.put(crypto, resultBuffer.toString());
        return result;
    }


    /**
     * Do POST request
     *
     * @param url request url
     * @return
     * @throws Exception
     */
    public String doUpload(String url, Map<String, String> params, List<File> fileList) throws Exception {
        URL localURL = new URL(url);
        URLConnection connection = this.openConnection(localURL);
        renderRequest(connection);
        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
        httpURLConnection.setRequestMethod(method.getValue());
        httpURLConnection = initHeader(httpURLConnection);
        httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        httpURLConnection.connect();

        OutputStream outputStream = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        try {
            outputStream = httpURLConnection.getOutputStream();
            // text
            if (params != null) {
                StringBuffer strBuf = new StringBuffer();
                Iterator<Map.Entry<String, String>> iter = params.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, String> entry = iter.next();
                    String inputName = entry.getKey();
                    String inputValue = entry.getValue();
                    if (inputValue == null) {
                        continue;
                    }
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                    strBuf.append(inputValue);
                }
                outputStream.write(strBuf.toString().getBytes());
            }

            // file
            if (fileList != null) {
                for (File file : fileList) {
                    String inputName = file.getName();
                    String inputValue = file.getAbsolutePath();
                    if (inputValue == null) {
                        continue;
                    }
                    String filename = file.getName();
                    String contentType = "image/png";
                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
                    outputStream.write(strBuf.toString().getBytes());
                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    int bytes = 0;
                    byte[] bufferOut = new byte[1024];
                    while ((bytes = in.read(bufferOut)) != -1) {
                        outputStream.write(bufferOut, 0, bytes);
                    }
                    in.close();
                }
            }

            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            outputStream.write(endData);
            outputStream.flush();
            if (httpURLConnection.getResponseCode() >= HTTP_SUCCESS_CODE) {
                if (connection != null) {
                    try {
                        connection.getInputStream().close();
                    } catch (Exception e) {
                    }
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                throw new Exception("HTTP Request is not success!!! Response code is " + httpURLConnection.getResponseCode());
            }
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
            }
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                try {
                    connection.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return resultBuffer.toString();
    }


    /**
     * generate params string
     *
     * @param map params
     * @return
     */
    private String generateParamsString(Map<String, String> map) {
        try {
            StringBuilder parBuild = new StringBuilder();
            for (String key : map.keySet()) {
                if (map.get(key) != null) {
                    parBuild.append(URLEncoder.encode(key.replace(" ", "%20"), "utf8")).append("=").append(URLEncoder.encode(map.get(key).replace(" ", "%20"), "utf8")).append("&");
                }
            }
            return parBuild.toString();
        } catch (Exception e) {
            throw new HttpException("Http URLEncoder enc is not support!!!", e);
        }
    }


    /**
     * check http responce content whether crypto
     *
     * @param httpURLConnection
     * @return
     */
    private HttpURLConnection initHeader(HttpURLConnection httpURLConnection) {
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestProperty("Accept-Charset", charset);
        httpURLConnection.setRequestProperty("Connection", "keep-alive");
        httpURLConnection.setRequestProperty("Keep-Alive", "60");
        if (headMap != null && !headMap.isEmpty()) {
            for (String key : headMap.keySet()) {
                httpURLConnection.setRequestProperty(key, headMap.get(key));
            }
        }
        return httpURLConnection;
    }

    /**
     * check http responce content whether crypto
     *
     * @param httpURLConnection
     * @return
     */
    private boolean checkCrypto(HttpURLConnection httpURLConnection) {
        for (int i = 0; ; i++) {
            String value = httpURLConnection.getHeaderField(i);
            if (value == null) {
                break;
            }
            if (httpURLConnection.getHeaderFieldKey(i).equals(HttpCryptoManager.EncryptoKey)) {
                if (value.equals(HttpCryptoManager.EncryptoValue)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }


    private URLConnection openConnection(URL localURL) throws IOException {
        URLConnection connection;
        if (proxyHost != null && proxyPort != null) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
            connection = localURL.openConnection(proxy);
        } else {
            connection = localURL.openConnection();
        }
        return connection;
    }


    /**
     * Render request according setting
     *
     * @param connection
     */
    private void renderRequest(URLConnection connection) {

        if (connectTimeout != null) {
            connection.setConnectTimeout(connectTimeout);
        }

        if (socketTimeout != null) {
            connection.setReadTimeout(socketTimeout);
        }

    }

    /*
     * Getter & Setter
     */
    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(Integer socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public List<File> getFileList() {
        return fileList;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }

    public Map<String, String> getHeadMap() {
        if (headMap == null) {
            headMap = new HashMap<>();
        }
        return headMap;
    }

    public void setHeadMap(Map<String, String> headMap) {
        this.headMap = headMap;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public RequestHandler getRequestHandler() {
        return requestHandler;
    }

    public void setRequestHandler(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }


    /**
     * HttpRequestor Builder
     */
    public static class Builder {

        private HttpRequestor httpRequestor;


        public Builder() {
            httpRequestor = new HttpRequestor();
        }

        public Builder setConnectionTimeOut(int timeOut) {
            this.httpRequestor.setConnectTimeout(timeOut);
            return this;
        }

        public Builder setSocketTimeOut(int timeOut) {
            this.httpRequestor.setSocketTimeout(timeOut);
            return this;
        }

        public Builder addRequestHeader(String key, String value) {
            this.httpRequestor.getHeadMap().put(key, value);
            return this;
        }
        public Builder setFiles(List<File> list) {
            this.httpRequestor.setFileList(list);
            return this;
        }

        public Builder addRequestHeaderAll(Map<String, String> map) {
            this.httpRequestor.getHeadMap().putAll(map);
            return this;
        }

        public Builder setCharset(String charset) {
            this.httpRequestor.setCharset(charset);
            return this;
        }

        public Builder get() {
            this.httpRequestor.setMethod(Method.GET);
            return this;
        }

        public Builder post() {
            this.httpRequestor.setMethod(Method.POST);
            return this;
        }

        public Builder upload() {
            this.httpRequestor.setMethod(Method.UPLOAD);
            return this;
        }

        public Builder setUrl(String url) {
            this.httpRequestor.setUrl(url);
            return this;
        }

        public Builder setBody(String content) {
            this.httpRequestor.setContent(content);
            return this;
        }

        public Builder setParams(Map<String, String> params) {
            this.httpRequestor.setParams(params);
            return this;
        }

        public Builder setRequestHandler(RequestHandler requestHandler) {
            this.httpRequestor.setRequestHandler(requestHandler);
            return this;
        }


        public HttpRequestor build() throws Exception {
            if (httpRequestor.getRequestHandler() == null) {
                throw new Exception("HttpRequestor RequestHandler must is not null!!!");
            }
            return httpRequestor;
        }
    }

    /**
     * http support method
     */
    public static enum Method {
        GET("GET"), POST("POST"),UPLOAD("POST");

        Method(String value) {
            this.value = value;
        }

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


}
