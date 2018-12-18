package com.akingyin.ireader

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioManager
import android.media.SoundPool
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.TextUtils

import android.view.Menu
import android.view.MenuItem
import android.view.View

import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.akingyin.base.SimpleActivity
import com.akingyin.base.dialog.DialogUtil
import com.akingyin.base.utils.ConvertUtils
import com.akingyin.base.utils.PreferencesUtil
import com.akingyin.bleqpp.BleQppNfcCameraServer
import com.akingyin.ireader.adapter.BleDeviceAdapter

import com.akingyin.ireader.entity.BleDevice
import com.akingyin.ireader.model.MainViewModel
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleScanCallback
import com.clj.fastble.scan.BleScanRuleConfig
import com.zlcdgroup.nfcsdk.ConStatus
import com.zlcdgroup.nfcsdk.RfidConnectorInterface
import com.zlcdgroup.nfcsdk.RfidInterface
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_toolbar.*


/**
 *
 * Name: IndexActivity
 * Author: akingyin
 * Email:
 * Comment: //TODO
 * Date: 2018-12-13 21:06
 *
 */

open class IndexActivity :  SimpleActivity(), RfidConnectorInterface {

    lateinit var  mainViewModel:MainViewModel
    lateinit  var  bleDeviceAdapter :BleDeviceAdapter
    lateinit var   connectMac:String


    override fun initInjection() {
    }

    override fun getLayoutId(): Int {
        return  R.layout.activity_main
    }

    override fun supportDataBinding(): Boolean {
        return  false
    }

    override fun initializationData(savedInstanceState: Bundle?) {
    }

    override fun onSaveInstanceData(outState: Bundle?) {
    }

    override fun initView() {
        setToolBar(toolbar,"IReader" )
        connectMac = PreferencesUtil.get("connectMac","")
        simpleRfid = PreferencesUtil.get("simpleRfid","")
        initPool(this)
        tv_simple_rfid.text = "样本标签：$simpleRfid"
        tv_device_be.text = "电量：　ＢＥ　"
        tv_result.text=""
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

          bleDeviceAdapter = BleDeviceAdapter(R.layout.item_device,null)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.itemAnimator = DefaultItemAnimator()
        recycler.adapter = bleDeviceAdapter
        progress_spinner.visibility = View.VISIBLE
        bleDeviceAdapter.setOnItemClickListener { adapter, view, position ->
            connectMac = bleDeviceAdapter.getItem(position)!!.mac
            cb_clean.isChecked = false
            ic_clean.visibility = View.GONE
            cb_right.isChecked = false
            BleQppNfcCameraServer.getInstance(this).sendCleanData(byteArrayOf(0))
            BleQppNfcCameraServer.getInstance(this).sendLightData(byteArrayOf(0))
            cb_right.setBackgroundResource(R.drawable.icon_guandeng)
            PreferencesUtil.put("connectMac",bleDeviceAdapter.getItem(position)?.mac)
            bleDeviceAdapter.data.forEach{
                it.connectStatus = 0
                if(it.mac == connectMac){
                    it.connectStatus = 2
                }
            }
            bleDeviceAdapter.notifyDataSetChanged()
            BleQppNfcCameraServer.getInstance(this).connect(connectMac,true)
        }

        btn_setting_simple.setOnClickListener {

            if(TextUtils.isEmpty(tempRfid)){
              showError("当前没有获取到标签信息")
            }else{

                PreferencesUtil.put("simpleRfid",tempRfid)
                tv_simple_rfid.text="样本标签：$tempRfid"
                simpleRfid = tempRfid
                tv_result.text="TRUE"
                tv_result.setTextColor( Color.GREEN)
            }
        }
        cb_right.setOnClickListener {
            if( BleQppNfcCameraServer.getInstance(this).current != ConStatus.CONNECTED){
                showError("未连接成功无法使用")
                return@setOnClickListener
            }
            if(cb_right.isChecked){
                BleQppNfcCameraServer.getInstance(this).sendLightData(byteArrayOf(1))
                cb_right.setBackgroundResource(R.drawable.icon_kaideng)
            }else{
                cb_right.setBackgroundResource(R.drawable.icon_guandeng)
                BleQppNfcCameraServer.getInstance(this).sendLightData(byteArrayOf(0))

            }
        }

        cb_clean.setOnClickListener{
            if( BleQppNfcCameraServer.getInstance(this).current != ConStatus.CONNECTED){
                showError("未连接成功无法使用")
                return@setOnClickListener
            }
            if(cb_clean.isChecked){
                BleQppNfcCameraServer.getInstance(this).sendCleanData(byteArrayOf(1))
                ic_clean.visibility = View.VISIBLE
            }else{
                BleQppNfcCameraServer.getInstance(this).sendCleanData(byteArrayOf(0))
                ic_clean.visibility = View.GONE
            }
        }

        ic_clean.setOnClickListener{
            if( BleQppNfcCameraServer.getInstance(this).current != ConStatus.CONNECTED){
                showError("未连接成功无法使用")
                return@setOnClickListener
            }
            cb_clean.isChecked = !cb_clean.isChecked
            if(cb_clean.isChecked){
                BleQppNfcCameraServer.getInstance(this).sendCleanData(byteArrayOf(1))
                ic_clean.visibility = View.VISIBLE
            }else{
                BleQppNfcCameraServer.getInstance(this).sendCleanData(byteArrayOf(0))
                ic_clean.visibility = View.GONE
            }
        }
        onScanBleDevice()
    }


    var mainHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (1 == msg.what) {

                handTag(msg.obj.toString())
            }else if(2 == msg.what){
                bleDeviceAdapter.notifyDataSetChanged()
            }
        }
    }

    override fun startRequest() {
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        if(progress_spinner.visibility == View.VISIBLE){
           var item = menu?.findItem(R.id.menu_flush)?.setVisible(false)
        }else{
            var item = menu?.findItem(R.id.menu_flush)?.setVisible(true)
        }
        return  true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.menu_flush){
            onScanBleDevice()
            progress_spinner.visibility = View.VISIBLE
             var menuItem = item.setVisible(false)

            invalidateOptionsMenu()

        }
        return super.onOptionsItemSelected(item)
    }


    private fun   onScanBleDevice(){
        if(BleQppNfcCameraServer.getInstance(this).current == ConStatus.CONNECTED){
            BleQppNfcCameraServer.getInstance(this).sendCleanData(byteArrayOf(0))
            BleQppNfcCameraServer.getInstance(this).sendLightData(byteArrayOf(0))
        }

        BleQppNfcCameraServer.getInstance(this).connectDestroy()
        BleManager.getInstance().initScanRule(BleScanRuleConfig.Builder().setScanTimeOut(10000).build())
        BleManager.getInstance().scan(object : BleScanCallback() {
            override fun onScanFinished(scanResultList: List<com.clj.fastble.data.BleDevice>) {
                progress_spinner.visibility = View.GONE
                invalidateOptionsMenu()



            }

            override fun onScanStarted(success: Boolean) {
                println("onScanStarted=$success")
            }

            override fun onScanning(bleDevice: com.clj.fastble.data.BleDevice) {
                println("onScanning")
              var  resutl  = false
              bleDeviceAdapter.data.forEach {
                  if(it.mac == bleDevice.mac){
                      it.rssi = bleDevice.rssi
                      resutl = true
                  }
              }
              if(!resutl ){
                  bleDeviceAdapter.addData(0,BleDevice(bleDevice.mac,bleDevice.name,bleDevice.rssi,0))
              }

            }
        })


    }

    override fun onNewRfid(data: ByteArray?, p1: RfidInterface?) {
    }

    override fun onConnectStatus(constants:  ConStatus?) {
        println("onConnectStatus $constants")
        bleDeviceAdapter.data.forEach {
            if(it.mac == connectMac){
                when (constants) {
                    ConStatus.CONNECTED -> it.connectStatus = 1
                    ConStatus.CONNECTFAIL -> it.connectStatus = 3
                    else -> it.connectStatus = 2
                }
                mainHandler.sendEmptyMessage(2)
                return
            }
        }
    }

    override fun onElectricity(elect: Int) {
        tv_device_be.text = "电量：　ＢＥ　$elect%"

    }

    override fun onPause() {
        super.onPause()
        BleQppNfcCameraServer.getInstance(this).onregistered(this)
    }

    override fun onResume() {
        super.onResume()
        BleQppNfcCameraServer.getInstance(this).onregistered(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if(null != tagFromIntent){
            var rfid = ConvertUtils.bytes2HexStrReverse(tagFromIntent!!.id)
            handTag(rfid)
        }
    }


    var   simpleRfid:String = ""
    var   tempRfid=""
    fun   handTag(rfid:String){
        tempRfid = rfid
        tv_read_rfid.text = "读取标签：$rfid"
        if(TextUtils.isEmpty(simpleRfid)){
            DialogUtil.showConfigDialog(this,"当前没有样本标签，是否将[$rfid]做为样本标签") {
                if(it){
                    PreferencesUtil.put("simpleRfid",rfid)
                    tv_simple_rfid.text="样本标签：$rfid"
                    tv_result.text="TRUE"
                    tv_result.setTextColor( Color.GREEN)
                }
            }
        }else{
            if(TextUtils.equals(simpleRfid,rfid)){
                tv_result.text="TRUE"
                tv_result.setTextColor( Color.GREEN)
            }else{
                tv_result.text="FALSE"
                tv_result.setTextColor( Color.RED)
                play()

            }
        }

    }

    private var pool: SoundPool? = null
    private var soundID: Int = 0
    private fun initPool(context: Context) {
        if (null != pool) {
            pool!!.release()
            pool = null
        }
        pool = SoundPool(13, AudioManager.STREAM_SYSTEM, 8)
        soundID = pool!!.load(context, R.raw.warning, 1)

    }

    private fun play() {
        pool?.play(soundID, 0.8f, 0.8f, 1, 0, 1f)
    }


    override fun onDestroy() {
        if(BleManager.getInstance().isSupportBle){
            BleQppNfcCameraServer.getInstance(this).sendCleanData(byteArrayOf(0))
            BleQppNfcCameraServer.getInstance(this).sendLightData(byteArrayOf(0))
            BleQppNfcCameraServer.getInstance(this).connectDestroy()
        }
        if(null != pool){
            pool!!.release()
        }
        BleManager.getInstance().cancelScan()

        super.onDestroy()
    }
}