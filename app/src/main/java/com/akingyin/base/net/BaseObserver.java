/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.akingyin.base.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;
import com.akingyin.base.net.exception.ApiException;
import com.akingyin.base.net.mode.ApiResult;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 *  对观察者进行简单封装
 * @ Description:
 * @ Author king
 * @ Date 2016/12/30 18:22
 * @ Version V1.0
 */

public abstract class BaseObserver<T>  implements Observer<ApiResult<T>> {

  private Context mContext;
  private ProgressDialog mDialog;
  private Disposable mDisposable;
  private final int SUCCESS_CODE = 0;

  public   BaseObserver(){

  }

  public BaseObserver(Context context, ProgressDialog dialog) {
    mContext = context;
    mDialog = dialog;

    mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override
      public void onCancel(DialogInterface dialog) {
        mDisposable.dispose();
      }
    });
  }

  @Override
  public void onSubscribe(Disposable d) {
    mDisposable = d;
  }

  @Override
  public void onNext(ApiResult<T> value) {
    if (ApiException.isSuccess(value)) {
      T t = value.getData();
      onHandleSuccess(t);
    } else {
      onHandleError(value.getStatus(), value.getMsg());
    }
  }

  @Override
  public void onError(Throwable e) {
    e.printStackTrace();

    if(mDialog != null && mDialog.isShowing()){
      mDialog.dismiss();
    }
      String   error ="出错了，"+e.getMessage();

      if(e instanceof  SocketTimeoutException){
        error="服务器响应超时，请稍后再试";

      }else if(e instanceof  ConnectException){
        error = "服务器连接超时，请稍后再试";
      }else if(e instanceof  UnknownHostException){
       error =  "出错了，请检查服务器地址是否正确";
      }


    onHandleError(-1,error);

  }

  @Override
  public void onComplete() {
    Log.d("gesanri", "onComplete");

    if(mDialog != null && mDialog.isShowing()){
      mDialog.dismiss();
    }
    if(null != mDisposable){
      mDisposable.dispose();
    }
  }

   public abstract void onHandleSuccess(T t);

 public void onHandleError(int code, String message) {
    if(null != mContext){
      Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

  }
}
