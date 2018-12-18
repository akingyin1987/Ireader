package com.akingyin.base.user;


import androidx.annotation.NonNull;

/**
 * 程序内各应用获取用户信息
 * @ Description:
 * @author king
 * @ Date 2017/9/5 16:26
 * @ Version V1.0
 */

public class UserSingleInfo {
  private volatile static UserSingleInfo mSingleMode;
  public static UserSingleInfo getInstance(){
    if (mSingleMode == null){
      synchronized (UserSingleInfo.class){
        if (mSingleMode == null){
          mSingleMode = new UserSingleInfo();
        }
      }
    }
    return  mSingleMode;
  }
  private UserSingleInfo() {
    System.out.println("创建");

  }

  public     void   setUser(@NonNull UserInfoEntity user){
    this.mUser = user;
  }

  private     OnNewUserInfo   mOnNewUserInfo;

  public void setOnNewUserInfo(OnNewUserInfo onNewUserInfo) {
    mOnNewUserInfo = onNewUserInfo;
  }

  private UserInfoEntity mUser;



  public UserInfoEntity get(){
    if(null == mUser){
       if(null != mOnNewUserInfo){
         mUser = mOnNewUserInfo.getNewUser();
       }
    }
    if(null == mUser){
      return  new UserInfoEntity();
    }
    return  mUser;
  }


  public    interface   OnNewUserInfo{
     UserInfoEntity getNewUser();
  }

}
