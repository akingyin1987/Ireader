package com.akingyin.ireader.adapter



import android.view.View
import android.view.ViewGroup
import com.akingyin.ireader.entity.BleDevice
import com.chad.library.adapter.base.BaseQuickAdapter
import androidx.databinding.DataBindingUtil
import com.akingyin.ireader.R
import com.akingyin.ireader.databinding.ItemDeviceBinding


/**
 *
 * Name: BleDeviceAdapter
 * Author: akingyin
 * Email:
 * Comment: //TODO
 * Date: 2018-12-14 23:03
 *
 */
class BleDeviceAdapter constructor(layoutResId: Int, data: List<BleDevice>?) : BaseQuickAdapter<BleDevice,BleDeviceViewHolder>(layoutResId,data) {


    override fun convert(helper: BleDeviceViewHolder?, item: BleDevice?) {
        var  viewHolder  = helper?.getViewDataBinding()
        viewHolder?.device = item
    }


    override fun getItemView(layoutResId: Int, parent: ViewGroup?): View {
        val binding = DataBindingUtil.inflate<ItemDeviceBinding>(mLayoutInflater, layoutResId, parent, false)
                ?: return super.getItemView(layoutResId, parent)
        val view = binding.root
        view.setTag(R.id.BaseQuickAdapter_databinding_support, binding)
        return view
    }
}