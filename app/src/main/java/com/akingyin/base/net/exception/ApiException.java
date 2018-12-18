/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.akingyin.base.net.exception;

import com.akingyin.base.net.mode.ApiCode;
import com.akingyin.base.net.mode.ApiResult;
import com.alibaba.fastjson.JSONException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import retrofit2.HttpException;

/**
 * Created by Administrator on 2017/9/12.
 */

public class ApiException extends Exception {

    private  int code;
    private String message;

    public ApiException(String message) {
        super(message);

    }

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
        this.message = throwable.getMessage();
    }

    public static boolean isSuccess(ApiResult apiResult) {
        if (apiResult == null) {
            return false;
        }
        return apiResult.getStatus() == ApiCode.Response.HTTP_SUCCESS || ignoreSomeIssue(apiResult.getStatus());
    }

    private static boolean ignoreSomeIssue(int code) {
        switch (code) {
            case ApiCode.Response.TIMESTAMP_ERROR://时间戳过期
            case ApiCode.Response.ACCESS_TOKEN_EXPIRED://AccessToken错误或已过期
            case ApiCode.Response.REFRESH_TOKEN_EXPIRED://RefreshToken错误或已过期
            case ApiCode.Response.OTHER_PHONE_LOGINED: //帐号在其它手机已登录
            case ApiCode.Response.SIGN_ERROR://签名错误
                return true;
            default:
                return false;
        }
    }

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ApiException(e, ApiCode.Request.HTTP_ERROR);
            switch (httpException.code()) {
                case ApiCode.Http.UNAUTHORIZED:
                case ApiCode.Http.FORBIDDEN:
                case ApiCode.Http.NOT_FOUND:
                case ApiCode.Http.REQUEST_TIMEOUT:
                case ApiCode.Http.GATEWAY_TIMEOUT:
                case ApiCode.Http.INTERNAL_SERVER_ERROR:
                case ApiCode.Http.BAD_GATEWAY:
                case ApiCode.Http.SERVICE_UNAVAILABLE:
                default:
                    ex.message = "网络出错，请稍候再试";
                    break;
            }
            return ex;
        } else if (e instanceof JSONException ) {
            ex = new ApiException(e, ApiCode.Request.PARSE_ERROR);
            ex.message = "数据格式错误，请联系相关人员！";
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ApiException(e, ApiCode.Request.NETWORK_ERROR);
            ex.message = "网络连接错误，请检查网络是否正常或稍候重试";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ApiException(e, ApiCode.Request.SSL_ERROR);
            ex.message = "证书出错，请连接相关人员!";
            return ex;
        } else if (e instanceof SocketTimeoutException) {
            ex = new ApiException(e, ApiCode.Request.TIMEOUT_ERROR);
            ex.message = "连接服务器超时，请稍候再试!";
            return ex;
        } else {
            ex = new ApiException(e, ApiCode.Request.UNKNOWN);
            ex.message = "UNKNOWN";
            return ex;
        }
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ApiException setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getDisplayMessage() {
        return message + "(code:" + code + ")";
    }
}
