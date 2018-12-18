/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.akingyin.base.net.interceptor;

import java.io.IOException;

import java.util.UUID;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author king
 * @version V1.0
 * @ Description:
 *
 * Company:重庆中陆承大科技有限公司
 * @ Date ${DATE} ${TIME}
 */
public abstract class AbsHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
          request =  getRequestHeader(request);
        Response response = chain.proceed(request);

        //根据和服务端的约定判断token过期
        if (isTokenExpired(response)) {

            //同步请求方式，获取最新的Token
            String newSession = getNewToken();
            //使用新的Token，创建新的请求
             request = getNewTokenRequestHeader(request,newSession);
            //重新请求
            return chain.proceed(request);
        }
        return decryptResponse(response);
    }

    /**
     * 根据Response，判断Token是否失效
     *
     * @param response
     * @return
     */
  public   abstract boolean isTokenExpired(Response response);


    /**
     * 同步请求方式，获取最新的Token
     *
     * @return
     */
    public String   getNewToken(){
      return UUID.randomUUID().toString();
    }

  /**
   * 获取头文件
   * @param request
   * @return
   */
   protected abstract Request    getRequestHeader(Request request);

  /**
   * 获取新的token
   * @param request
   * @param token
   * @return
   */
   protected abstract Request    getNewTokenRequestHeader(Request request,String token);

  /**
   * 解析数据
   * @param response
   * @return
   */
   protected abstract  Response   decryptResponse(Response  response);
}
