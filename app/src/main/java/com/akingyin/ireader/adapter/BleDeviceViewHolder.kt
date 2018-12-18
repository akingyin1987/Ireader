package com.akingyin.ireader.adapter

import android.view.View
import androidx.databinding.ViewDataBinding
import com.akingyin.ireader.R
import com.akingyin.ireader.databinding.ItemDeviceBinding
import com.chad.library.adapter.base.BaseViewHolder

/**
 *
 * Name: BleDeviceViewHolder
 * Author: akingyin
 * Email:
 * Comment: //TODO
 * Date: 2018-12-14 23:31
 *
 */
class BleDeviceViewHolder constructor(view: View) : BaseViewHolder(view) {


    fun  getViewDataBinding() : ItemDeviceBinding {
        return  itemView.getTag(R.id.BaseQuickAdapter_databinding_support) as ItemDeviceBinding
    }

}