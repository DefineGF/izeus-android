package com.cjm.izeus.util;


import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    public static void sendOkHttpRequest(Request request, okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        client.newCall(request).enqueue(callback);
    }
    public static void sendOkHttpRequestByURL(String address, okhttp3.Callback callback){
        Request request=new Request.Builder()
                                   .url(address)
                                   .build();
        sendOkHttpRequest(request,callback);
    }

}
