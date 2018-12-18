package com.akingyin.base

import android.content.Context
import android.os.Bundle

import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.classic.common.MultipleStatusView
import es.dmoral.toasty.Toasty
import android.nfc.tech.MifareClassic
import android.content.IntentFilter
import android.app.PendingIntent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.NfcV
import android.nfc.tech.NfcF
import android.nfc.tech.NfcB
import android.nfc.tech.NfcA
import android.content.Intent
import android.os.Parcelable
import com.akingyin.base.utils.ConvertUtils






/**
 * @ Description:
 * @author king
 * @ Date 2018/9/7 16:03
 * @version V1.0
 */
abstract class SimpleActivity : AppCompatActivity() ,IBaseView{

    protected   var multipleStatusView : MultipleStatusView?=null
    // Log tag
    protected var TAG_LOG: String? = null


    protected var mContext: Context? = null

    private var mAdapter: NfcAdapter? = null
    private var mPendingIntent: PendingIntent? = null
    private var mFilters: Array<IntentFilter>? = null
    private var mTechLists: Array<Array<String>>? = null
    var mf: MifareClassic? = null
    var tagFromIntent: Tag? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjection()
        super.onCreate(savedInstanceState)
        AppManager.getInstance()!!.addActivity(this)
        TAG_LOG = this.localClassName
        if(!supportDataBinding()){
            setContentView(getLayoutId())
        }

        mContext = this

        initializationData(savedInstanceState)
        initView()
        mAdapter = NfcAdapter.getDefaultAdapter(this)
        if (null == mAdapter) {
            showWarning("当前终端不支持NFC")
        } else {

            mPendingIntent = PendingIntent.getActivity(this, 0, Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
            val ndef = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
            try {
                ndef.addDataType("*/*")
                mFilters = arrayOf(ndef)
                mTechLists = arrayOf(arrayOf(MifareClassic::class.java.name), arrayOf(NfcA::class.java.name), arrayOf(NfcB::class.java.name), arrayOf(NfcF::class.java.name), arrayOf(NfcV::class.java.name))

            } catch (e: IntentFilter.MalformedMimeTypeException) {

                e.printStackTrace()
            }

        }
        startRequest()
        initStatusViewListion()

    }

    abstract   fun    supportDataBinding():Boolean

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        onSaveInstanceData(outState)
    }

    protected fun setToolBar(toolbar: Toolbar, title: String) {
        toolbar.setTitle(title)

        setSupportActionBar(toolbar)
        if (null != supportActionBar) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            toolbar.setNavigationOnClickListener(View.OnClickListener { onBackPressed() })
        }

    }

    /**
     * dagger2注入
     */
    abstract   fun   initInjection()

    private   fun   initStatusViewListion(){
        multipleStatusView?.setOnClickListener(mRetryClickListener)
    }

    open   var  mRetryClickListener : View.OnClickListener = View.OnClickListener {
        startRequest()
    }


    /**
     * 加载布局
     */
    @LayoutRes
    abstract    fun    getLayoutId():Int

    /**
     * 初始化数据
     */
    abstract    fun    initializationData(savedInstanceState : Bundle?)


    /**
     * 保存当前状态
     */
    abstract   fun    onSaveInstanceData(outState: Bundle?)


    /**
     * 初始化View
     */
    abstract    fun    initView()


    /**
     * 开始请求
     */
    abstract    fun     startRequest()


    /**
     * 打开软键盘
     */
    fun   openKeyBord(mEditText: EditText, mContext: Context){
        var imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

    }

    /**
     * 关阀软键盘
     */
    fun   closeKeyBord(mEditText: EditText, mContext: Context){
        val   imm  = mContext.getSystemService(Context.INPUT_METHOD_SERVICE)  as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken,0)
    }

    override fun showMessage(msg: String?) {
        if (msg != null) {
            Toasty.info(this,msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showSucces(msg: String?) {
        if (msg != null) {
            Toasty.success(this,msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showError(msg: String?) {
        if (msg != null) {
            Toasty.error(this,msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showWarning(msg: String?) {
        if (msg != null) {
            Toasty.warning(this,msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun close() {

    }

    override fun showTips(msg: String?) {
    }

    override fun showLoadDialog(msg: String?) {
    }

    override fun hideLoadDialog() {
    }

    override fun showLoading() {
    }

    override fun dismissLoading() {
    }

    override fun onResume() {

        if (null != mAdapter) {
            mAdapter!!.enableForegroundDispatch(this, mPendingIntent, mFilters, mTechLists)
        }

        super.onResume()

    }

    override fun onPause() {
        if (null != mAdapter) {
            mAdapter!!.disableForegroundDispatch(this)
        }

        super.onPause()

    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        println("onNewIntent---rfid")
        if (NfcAdapter.ACTION_TECH_DISCOVERED == intent.action) {
            tagFromIntent = intent.getParcelableExtra<Parcelable>(NfcAdapter.EXTRA_TAG) as Tag?

        }

    }

    override fun onDestroy() {
        AppManager.getInstance()?.finishActivity(this)
        super.onDestroy()
    }

}