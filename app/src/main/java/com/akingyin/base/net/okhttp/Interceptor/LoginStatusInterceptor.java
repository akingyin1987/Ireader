/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.akingyin.base.net.okhttp.Interceptor;


import java.io.IOException;
import java.nio.charset.Charset;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;


/**
 * @ Description:
 * @ Author king
 * @ Date 2017/8/18 15:11
 * @ Version V1.0
 */

public class LoginStatusInterceptor implements Interceptor {
  private static final Charset UTF8 = Charset.forName("UTF-8");
  @Override public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();

    Response originalResponse = chain.proceed(request);
    if(originalResponse.isSuccessful()){
      try {
        BufferedSource source = originalResponse.body().source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();


        String tempStr = buffer.clone().readString(UTF8);


      }catch (Exception e){
        e.printStackTrace();
      }

    }
    return originalResponse;
  }
}
