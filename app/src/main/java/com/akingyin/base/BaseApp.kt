package com.akingyin.base

import com.akingyin.ireader.BuildConfig

import dagger.android.DaggerApplication
import timber.log.Timber

/**
 * @ Description:
 * @author king
 * @ Date 2018/9/3 12:17
 * @version V1.0
 */
  abstract class BaseApp : DaggerApplication() {


    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            initLeakCanary()
        }
    }

    private fun   initLeakCanary() {


    }



    abstract   fun   initInjection()


}