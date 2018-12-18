package com.akingyin.base

/**
 *   in 泛型，我们可以将使用父类泛型的对象赋值给使用子类泛型的对象。
 *   out 泛型，我们能够将使用子类泛型的对象赋值给使用父类泛型的对象。
 * @ Description:
 * @author king
 * @ Date 2018/8/3 15:56
 * @version V1.0
 */
interface IPresenter< V:IBaseView> {


    fun attachView(mRootView: V)

    fun detachView()
}