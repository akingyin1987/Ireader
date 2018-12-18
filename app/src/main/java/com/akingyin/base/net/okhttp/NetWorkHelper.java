/*
 *
 *   Copyright (c) 2016 [akingyin@163.com]
 *
 *   Licensed under the Apache License, Version 2.0 (the "License”);
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.akingyin.base.net.okhttp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ Description:
 *  网络检测相关
 * Company:重庆中陆承大科技有限公司
 * @ Author king
 * @ Date 2016/7/6 17:55
 * @ Version V1.0
 */
public class NetWorkHelper {

  /**
   * 检测网络是否连接
   */
  public static boolean isNetConnected(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (cm != null) {
      NetworkInfo[] infos = cm.getAllNetworkInfo();
      if (infos != null) {
        for (NetworkInfo ni : infos) {
          if (ni.isConnected()) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * 检测wifi是否连接
   */
  public static boolean isWifiConnected(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (cm != null) {
      NetworkInfo networkInfo = cm.getActiveNetworkInfo();
      if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
        return true;
      }
    }
    return false;
  }

  /**
   * 检测3G是否连接
   */
  public static boolean is3gConnected(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (cm != null) {
      NetworkInfo networkInfo = cm.getActiveNetworkInfo();
      if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
        return true;
      }
    }
    return false;
  }

 public  static Pattern pattern = Pattern.compile("^(http://|https://)?((?:[A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\\.)+([A-Za-z]+)[/\\?\\:]?.*$",Pattern.CASE_INSENSITIVE);
  /**
   * 判断网址是否有效
   */
  public static boolean isLinkAvailable(String link) {

    Matcher matcher = pattern.matcher(link);
    if (matcher.matches()) {
      return true;
    }
    return false;
  }
}
