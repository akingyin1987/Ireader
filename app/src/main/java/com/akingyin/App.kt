package com.akingyin

import android.app.Application
import com.akingyin.base.utils.KissTools
import com.clj.fastble.BleManager

/**
 *
 * Name: App
 * Author: akingyin
 * Email:
 * Comment: //TODO
 * Date: 2018-12-14 22:02
 *
 */
class App  : Application() {

    override fun onCreate() {
        super.onCreate()
        KissTools.setContext(this)
        BleManager.getInstance().init(this)
        BleManager.getInstance().enableLog(true)
        if(BleManager.getInstance().isSupportBle){
            BleManager.getInstance().enableBluetooth()
        }
        println("init"+BleManager.getInstance().isBlueEnable)
    }
}