package com.akingyin.base.user;

import java.io.Serializable;

/**
 * 基础用户信息
 */

public class UserInfoEntity implements Serializable {


  public    Long   localId;


  public   String     userId;


  public   String     account;


  public   String     name;


  public   String     token; //token

  /**
   * token 过期时间
   */
  public   Long       tokenExpiryTime;


  public   String     refreshToken;//刷新时使用token


  public   Long       accessTokenExpiryTime;


  public   String     accessToken;


  public   String     openId;




  public   Long       loginTime;//登录时间


  public   Long       elapsedRealtime;//登录成功时手机运行时间
}
