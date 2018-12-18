/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.akingyin.base.net.utils;

import com.akingyin.base.net.RxException;
import com.akingyin.base.net.exception.ApiException;
import com.akingyin.base.net.mode.ApiResult;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 调度器
 * @ Description:
 * @ Author king
 * @ Date 2017/9/28 11:29
 * @ Version V1.0
 */

public class RxSchedulers {


  /**
   * 简化线程转换(消费事件会在UI线程)
   * @param <T>
   * @return
   */
  public static <T> ObservableTransformer<T, T> IO_Main() {
    return new ObservableTransformer<T, T>() {
      @Override public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())

            .observeOn(AndroidSchedulers.mainThread());
      }
    };
  }

  /**
   * 线程转换全是IO线程
   * @param <T>
   * @return
   */
  public static <T> ObservableTransformer<T, T> IO_IO() {
    return new ObservableTransformer<T, T>() {
      @Override public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io())
            .observeOn(Schedulers.io());
      }
    };
  }



  /**
   * 以RX方式处理
   * @param t
   * @param <T>
   * @return
   */
  public  static  <T> Observable<T> createData(final T t) {
    return Observable.create(new ObservableOnSubscribe<T>() {
      @Override public void subscribe(ObservableEmitter<T> subscriber) throws Exception {
        try {
          subscriber.onNext(t);
          subscriber.onComplete();
        } catch (Exception e) {
          subscriber.onError(e);
        }
      }
    });
  }

  /**
   * 处理结果数据
   * @param <T>
   * @return
   */
  public static  <T> ObservableTransformer<ApiResult<T>, T> handleResult() {
    return  new ObservableTransformer<ApiResult<T>, T>() {
      @Override public ObservableSource<T> apply(Observable<ApiResult<T>> upstream) {
        return upstream.flatMap(new Function<ApiResult<T>, ObservableSource<T>>() {
          @Override public ObservableSource<T> apply(ApiResult<T> apiResult) throws Exception {
            if(ApiException.isSuccess(apiResult)){
              return  createData(apiResult.getData());
            }
            return Observable.error(new Exception(apiResult.getMsg()));
          }
        });
      }
    };

  }



  public   void    demo(){
   ApiResult<String> apiResult = new ApiResult<>();
   Disposable  disposable = Observable.just(apiResult).compose(RxSchedulers.<ApiResult<String>>IO_Main())
        .compose(RxSchedulers.<String>handleResult())
        .subscribe(new Consumer<String>() {
          @Override public void accept(String s) throws Exception {

          }
        },new RxException<Throwable>(new Consumer<Throwable>() {
          @Override public void accept(Throwable throwable) throws Exception {
            throwable.printStackTrace();
          }
        }){

        });


  }
}
