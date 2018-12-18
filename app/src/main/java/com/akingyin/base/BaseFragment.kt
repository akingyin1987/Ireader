package com.akingyin.base

import android.content.Context
import android.os.Bundle

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.classic.common.MultipleStatusView
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import es.dmoral.toasty.Toasty
import javax.inject.Inject


/**
 * @ Description:
 * @author king
 * @ Date 2018/8/3 17:56
 * @version V1.0
 */
abstract class BaseFragment :SimpleFragment(),HasSupportFragmentInjector,IBaseView{


    @Inject
    lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onAttach(context: Context?) {
         injection()
        super.onAttach(context)
    }


    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> {
        return childFragmentInjector
    }

    /**
     * 视图是否加载完毕
     */


    private var isViewPrepare = false
    /**
     * 数据是否加载过了
     */
    private var hasLoadData = false
    /**
     * 多种状态的 View 的切换
     */
    protected var mLayoutStatusView: MultipleStatusView? = null



    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            lazyLoadDataIfPrepared()
        }
    }

    abstract   fun  injection()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
         isViewPrepare = true
        initView()
        lazyLoadDataIfPrepared()
        //多种状态切换的view 重试点击事件
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }



    private fun lazyLoadDataIfPrepared() {
        if (userVisibleHint && isViewPrepare && !hasLoadData) {
            lazyLoad()
            hasLoadData = true
        }
    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        lazyLoad()
    }




    /**
     * 初始化 ViewI
     */
    abstract fun initView()

    /**
     * 懒加载
     */
    abstract fun lazyLoad()


    override fun showMessage(msg: String?) {
        if (msg != null && null != mContext) {
            Toasty.info(mContext!!,msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showSucces(msg: String?) {
        if (msg != null && null != mContext) {
            Toasty.success(mContext!!,msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showError(msg: String?) {
        if (msg != null && null != mContext) {
            Toasty.error(mContext!!,msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showWarning(msg: String?) {
        if (msg != null && null != mContext) {
            Toasty.warning(mContext!!,msg, Toast.LENGTH_SHORT).show()
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
}