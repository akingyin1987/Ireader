/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.akingyin.base.net.utils;

import android.os.Build;
import android.util.Log;
import com.akingyin.base.net.exception.ApiException;
import com.akingyin.base.net.mode.ApiResult;

import androidx.annotation.RequiresApi;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.operators.flowable.FlowableInternalHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * 封装Subscriber,异常处理
 * @ Description:
 * @ Author king
 * @ Date 2017/9/28 11:48
 * @ Version V1.0
 */

public class RxSubscriber <T extends ApiResult> extends AtomicReference<Subscription>
    implements Subscriber<T>, Subscription, Disposable {
  private static final String TAG = "RxSubscriber";

  private static final String SOCKETTIMEOUTEXCEPTION = "网络连接超时，请检查您的网络状态，稍后重试";
  private static final String CONNECTEXCEPTION = "网络连接异常，请检查您的网络状态";
  private static final String UNKNOWNHOSTEXCEPTION = "网络异常，请检查您的网络状态";

  private static final long serialVersionUID = -7251123623727029452L;
  final Consumer<? super T> onNext;
  final Consumer<? super Throwable> onError;
  final Action onComplete;
  final Consumer<? super Subscription> onSubscribe;

  public RxSubscriber(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {
    super();
    this.onNext = onNext;
    this.onError = onError;
    this.onComplete = Functions.EMPTY_ACTION;
    this.onSubscribe = FlowableInternalHelper.RequestMax.INSTANCE;
  }

  public RxSubscriber(Consumer<? super T> onNext, Consumer<? super Throwable> onError,
      Action onComplete,
      Consumer<? super Subscription> onSubscribe) {
    super();
    this.onNext = onNext;
    this.onError = onError;
    this.onComplete = onComplete;
    this.onSubscribe = onSubscribe;
  }

  @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
  @Override
  public void onSubscribe(Subscription s) {
    if (SubscriptionHelper.setOnce(this, s)) {
      try {
        onSubscribe.accept(this);
      } catch (Throwable ex) {
        Exceptions.throwIfFatal(ex);
        s.cancel();
        onError(ex);
      }
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
  @Override
  public void onNext(T t) {
    if (!isDisposed()) {
      try {
        if (ApiException.isSuccess(t)) {
          onNext.accept(t);
        }  else {

          RxJavaPlugins.onError(new Throwable(t.getMsg()));
        }
      } catch (Throwable e) {
        Exceptions.throwIfFatal(e);
        get().cancel();
        onError(e);
      }
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
  @Override
  public void onError(Throwable t) {
    if (get() != SubscriptionHelper.CANCELLED) {
      lazySet(SubscriptionHelper.CANCELLED);
      try {
        onError.accept(t);
      } catch (Throwable e) {
        Exceptions.throwIfFatal(e);
        RxJavaPlugins.onError(new CompositeException(t, e));
      }
    } else {
      RxJavaPlugins.onError(t);
    }

    if (t instanceof SocketTimeoutException) {
      Log.e(TAG, "onError: SocketTimeoutException----" + SOCKETTIMEOUTEXCEPTION);
      RxJavaPlugins.onError(new Throwable(SOCKETTIMEOUTEXCEPTION));
    } else if (t instanceof ConnectException) {
      Log.e(TAG, "onError: ConnectException-----" + CONNECTEXCEPTION);
      RxJavaPlugins.onError(new Throwable(CONNECTEXCEPTION));
    } else if (t instanceof UnknownHostException) {
      Log.e(TAG, "onError: UnknownHostException-----" + UNKNOWNHOSTEXCEPTION);
      RxJavaPlugins.onError(new Throwable(UNKNOWNHOSTEXCEPTION));
    } else {
      Log.e(TAG, "onError:----" + t.getMessage());

      RxJavaPlugins.onError(t);
    }
    onComplete();
    //        cancelLoadingDialog();
  }

  @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
  @Override
  public void onComplete() {
    if (get() != SubscriptionHelper.CANCELLED) {
      lazySet(SubscriptionHelper.CANCELLED);
      try {
        onComplete.run();
      } catch (Throwable e) {
        Exceptions.throwIfFatal(e);
        RxJavaPlugins.onError(e);
      }
    }
  }

  @Override
  public void dispose() {
    cancel();
  }

  @Override
  public boolean isDisposed() {
    return get() == SubscriptionHelper.CANCELLED;
  }

  @Override
  public void request(long n) {
    get().request(n);
  }

  @Override
  public void cancel() {
    SubscriptionHelper.cancel(this);
  }

}
