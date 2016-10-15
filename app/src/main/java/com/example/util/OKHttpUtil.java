package com.example.util;

import com.example.entry.ParamsPair;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/8/9.
 */
public class OKHttpUtil {
    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    static{
        mOkHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
    }
    /**
     * 该不会开启异步线程。
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute();
    }
    /**
     * 开启异步线程访问网络
     * @param url
     * @param responseCallback
     */
    public static void enqueue(String url, Callback responseCallback){
        Request request = new Request.Builder()
                .url(url)
                .build();
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }

    public static String getStringFromServer(String url) throws IOException{
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            String responseUrl = response.body().string();
            return responseUrl;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
    private static final String CHARSET_NAME = "UTF-8";

    /**
     * 为HttpGet 的 url 方便的添加1个name value 参数。
     * @param url
     * @param name
     * @param value
     * @return
     */
    public static String attachHttpGetParam(String url, String name, String value){
        return url + "?" + name + "=" + value;
    }

    /**
     * 制作一条GET请求的URL
     * @param url
     * @param list
     * @return
     */
    public static String makeURL(String url,List<ParamsPair>list){
        StringBuffer buffer;
        if(list==null||list.size()==0){
            return url;
        }else{
            buffer = new StringBuffer();
            for(ParamsPair p:list){
                buffer.append("&");
                buffer.append(p.getKey());
                buffer.append("=");
                buffer.append(p.getValue());
            }
            buffer.deleteCharAt(0);
        }
        return url+"?"+buffer.toString()+TokenWithTime.formatGETToken();
    }

}
