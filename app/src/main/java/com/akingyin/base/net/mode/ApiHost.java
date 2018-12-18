/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.akingyin.base.net.mode;

import android.text.TextUtils;
import com.akingyin.base.net.config.CommonConstants;
import java.io.File;

/**
 * Created by Administrator on 2017/9/12.
 */

public class ApiHost {

  private    static   final   String  HTTP_HEADER="http://";
  private    static   final   String  HTTPS_HEADER="https://";

  private static String host = CommonConstants.API_HOST;

  public static String getHost() {
    if(!TextUtils.isEmpty(host) && !host.endsWith(File.separator)){
      return host+File.separator;
    }
    return host;
  }

  public static void setHost(String url) {
    host = url;

  }

  public static void setHostHttp(String url) {
    if (url.startsWith(HTTPS_HEADER) || url.startsWith(HTTP_HEADER)) {
      host = url;
      host = host.replaceAll(HTTPS_HEADER, HTTP_HEADER);
    } else {
      host = HTTP_HEADER + url;
    }
  }

  public static void setHostHttps(String url) {
    if (url.startsWith(HTTPS_HEADER) || url.startsWith(HTTP_HEADER)) {
      host = url;
      host = host.replaceAll(HTTP_HEADER, HTTPS_HEADER);
    } else {
      host = HTTPS_HEADER + url;
    }
  }

  public static String getHttp() {
    if (host.startsWith(HTTPS_HEADER) || host.startsWith(HTTP_HEADER)) {
      host = host.replaceAll(HTTPS_HEADER, HTTP_HEADER);
    } else {
      host = HTTP_HEADER + host;
    }
    return host;
  }

  public static String getHttp(String   url) {
    if (url.startsWith(HTTPS_HEADER) || url.startsWith(HTTP_HEADER)) {
      url = url.replaceAll(HTTPS_HEADER, HTTP_HEADER);
    } else {
      url = HTTP_HEADER + url;
    }
    if(!url.endsWith(File.separator)){
      url = url+"/";
    }
    return url;
  }

  public static String getHttps() {
    if (host.startsWith(HTTPS_HEADER) || host.startsWith(HTTP_HEADER)) {
      host = host.replaceAll(HTTP_HEADER, HTTPS_HEADER);
    } else {
      host = HTTPS_HEADER + host;
    }
    return host;
  }
}
