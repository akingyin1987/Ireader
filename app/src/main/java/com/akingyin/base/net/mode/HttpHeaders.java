/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.akingyin.base.net.mode;


import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import okhttp3.Request;

/**
 * HTTP headers 工具
 * Created by Administrator on 2017/9/12.
 */

public class HttpHeaders {

    public static final String  AUTHORIZATION       = "Authorization";
    public static final String  CACHE_CONTROL       = "Cache-Control";
    public static final String  CONTENT_DISPOSITION = "Content-Disposition";
    public static final String  CONTENT_ENCODING    = "Content-Encoding";
    public static final String  CONTENT_LENGTH      = "Content-Length";
    public static final String  CONTENT_MD5         = "Content-MD5";
    public static final String  CONTENT_TYPE        = "Content-Type";
    public static final String  TRANSFER_ENCODING   = "Transfer-Encoding";
    public static final String  DATE                = "Date";
    public static final String  ETAG                = "ETag";
    public static final String  EXPIRES             = "Expires";
    public static final String  HOST                = "Host";
    public static final String  LAST_MODIFIED       = "Last-Modified";
    public static final String  RANGE               = "Range";
    public static final String  LOCATION            = "Location";
    public static final String  CONNECTION          = "Connection";

    private Request request;


    private   String  token;


    private   String  currentTime;


    private   String  method;

    private   String  imei;



    public String getImei() {
        return imei;
    }

    public HttpHeaders setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public Request getRequest() {
        Map<String,String>  datas = new HashMap<>();
        datas.put("token",token);
        datas.put("currentTime",currentTime);
        datas.put("method",method);
        datas.put("imei",imei);

        request.newBuilder().addHeader("token",token)
                .addHeader("currentTime",currentTime)
                .addHeader("method",method)
                .addHeader("imei",imei)
                .build();
        return request;
    }


    public String getToken() {
        return token;
    }

    public HttpHeaders setToken(String token) {
        this.token = token;
        return this;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public HttpHeaders setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public HttpHeaders setMethod(String method) {
        this.method = method;
        return this;
    }

    public HttpHeaders(@NonNull Request request) {
        this.request = request;
    }

    private Map<String, String> headers             = new HashMap<>();

    public Map<String, String> getHeaders() {
        return headers;
    }




}
