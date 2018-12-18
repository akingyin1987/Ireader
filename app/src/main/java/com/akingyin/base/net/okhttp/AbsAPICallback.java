/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.akingyin.base.net.okhttp;

import com.akingyin.base.net.mode.ApiCode;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.json.JSONObject;

/**
 * OkHTTP 网络请求回调
 * Created by  on 2015/12/29.
 */
public abstract class AbsAPICallback implements Callback {

    //请求错误

    /**
     * 返回失败
     * @param code
     * @param message
     */
    public  abstract void   onFailure(int code,String  message);

    //请求成功

    /**
     * 返回成功
     * @param jsonObject
     */
    public  abstract  void  onSuccess(JSONObject   jsonObject);


    //
    /**
     * 是否验证信息
     */
    private    boolean    iSintercept  = true;

    /**
     * 验证JSON
     * @param valJsonObject
     * @return
     */
    public boolean valintercept(JSONObject   valJsonObject) {
        return iSintercept;
    }

    public void setiSintercept(boolean iSintercept) {
        this.iSintercept = iSintercept;
    }

    @Override
    public void onFailure(Call call, IOException e) {

        onFailure(0,"网络连接错误，请检查网络是否正常开启");
        if(null != call){
            call.cancel();
        }

    }

    @Override
    public void onResponse(Call call,Response response) throws IOException {
        try{
            int  httpcode = response.code();
            if(response.isSuccessful()){
                JSONObject    result = new JSONObject(response.body().string());
                if(iSintercept){
                    boolean   val = valintercept(result);
                    if(!val){
                        return;
                    }
                }
                onSuccess(result);
            }else{
                String   errormsg = "连接服务异常，请稍后再试";
                if(httpcode == ApiCode.Http.REQUEST_TIMEOUT){
                    errormsg="请求超时，请检查网络或稍后再试";
                }else if(httpcode>=ApiCode.Http.INTERNAL_SERVER_ERROR){
                    errormsg="连接服务器失败";
                }
                onFailure(httpcode,errormsg);
            }
        }catch (Exception e){
            e.printStackTrace();
            onFailure(0,"处理数据异常");
        }
    }
}
