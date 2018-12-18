package com.akingyin.ireader.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

/**
 *
 * Name: BleDevice
 * Author: akingyin
 * Email:
 * Comment: //TODO
 * Date: 2018-12-14 21:11
 *
 */
class BleDevice (mac :String,deviceName:String,rssi : Int ,connectStatus:Int) :BaseObservable(){

    @Bindable
    var  mac:String = mac

    @Bindable
    var  deviceName:String = deviceName


    @Bindable
    var  rssi:Int = rssi

    //1=连接成功 2=连接中 3=连接失败
    @Bindable
    var  connectStatus :Int = connectStatus



}