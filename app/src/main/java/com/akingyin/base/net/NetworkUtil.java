/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.akingyin.base.net;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @ Description:
 * @author king
 * @ Date 2016/12/30 18:29
 * @ Version V1.0
 */

public class NetworkUtil {

  /**
   * 判断网络是否可用
   * @param activity
   * @return
   */
  public static boolean isNetworkAvailable(Activity activity) {
    Context context = activity.getApplicationContext();
    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    if (connectivityManager == null) {
      return false;
    } else {
      NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

      if (networkInfo != null && networkInfo.length > 0) {
        for (int i = 0; i < networkInfo.length; i++) {

          if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
            return true;
          }
        }
      }
    }
    return false;
  }
}
