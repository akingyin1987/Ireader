/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.akingyin.base.time;

import android.os.SystemClock;

import com.akingyin.base.user.UserInfoEntity;
import com.akingyin.base.user.UserSingleInfo;


/**
 * 终端时间处理
 * @ Description:
 * @author king
 * @ date 2017/9/13 13:06
 * @version  V1.0
 */
public class AppTime {
  private volatile static AppTime singleton;

  private   Long   loginTme;

  private   Long   elapsedRealtime;

  public void setLoginTme(Long loginTme) {
    this.loginTme = loginTme;
  }

  public void setElapsedRealtime(Long elapsedRealtime) {
    this.elapsedRealtime = elapsedRealtime;
  }

  public  synchronized   Long    getTime(){
    if(null == loginTme || null == elapsedRealtime){
      UserInfoEntity user = UserSingleInfo.getInstance().get();
      if(null != user && null != user.loginTime && null != user.elapsedRealtime){
        loginTme = user.loginTime;
        elapsedRealtime = user.elapsedRealtime;
      }else{
        return  System.currentTimeMillis();
      }
    }
    return  loginTme+(SystemClock.elapsedRealtime() - elapsedRealtime);
  }

  public static AppTime getInstance() {
    if (singleton == null) {
      synchronized (AppTime.class) {
        if (singleton == null) {
          singleton = new AppTime();
        }
      }
    }
    return singleton;
  }

  private AppTime() {
  }
}
