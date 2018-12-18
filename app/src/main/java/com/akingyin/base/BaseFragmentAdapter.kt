package com.akingyin.base

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import java.util.*

/**
 * @ Description:
 * @author king
 * @ Date 2018/8/6 10:20
 * 该类内的每一个生成的 Fragment 都将保存在内存之中，
 * 因此适用于那些相对静态的页，数量也比较少的那种；
 * 如果需要处理有很多页，并且数据动态性较大、占用内存较多的情况，
 * 应该使用FragmentStatePagerAdapter。
 * @version V1.0
 */
class BaseFragmentAdapter : FragmentPagerAdapter {


    private   var  fragmentlist:List<Fragment>?= ArrayList()

    private   var  mTitles:List<String>?=null

    constructor(fm: FragmentManager?, fragmentlist: List<Fragment>?) : super(fm) {
        this.fragmentlist = fragmentlist
    }

    constructor(fm: FragmentManager?, fragmentlist: List<Fragment>?, mTitles: List<String>?) : super(fm) {
        this.fragmentlist = fragmentlist
        this.mTitles = mTitles
    }

    //刷新fragment
    @SuppressLint("CommitTransaction")
    private fun setFragments(fm: FragmentManager, fragments: List<Fragment>, mTitles: List<String>) {
        this.mTitles = mTitles
        if (this.fragmentlist != null) {
            val ft = fm.beginTransaction()
            val  iterable = fragmentlist?.iterator()
            while (iterable?.hasNext()!!){
                ft.remove(iterable.next())
            }
            ft?.commitAllowingStateLoss()
            fm.executePendingTransactions()
        }
        this.fragmentlist = fragments
        notifyDataSetChanged()
    }
    override fun getItem(p0: Int): Fragment {
        return  fragmentlist!![p0]
    }

    override fun getCount(): Int {
      return   fragmentlist!!.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
       return  if(null == mTitles)"" else mTitles!![position]
    }
}