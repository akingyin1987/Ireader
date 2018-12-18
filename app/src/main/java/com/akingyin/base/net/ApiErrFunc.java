/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.akingyin.base.net;



import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Throwable转Observable<T>
 * Created by Administrator on 2017/9/12.
 */

public class ApiErrFunc<T> implements Function<Throwable,Observable<T>> {

    @Override
    public Observable<T> apply(@NonNull Throwable throwable) throws Exception {

        return Observable.error(throwable);
    }
}
