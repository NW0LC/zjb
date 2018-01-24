package com.exz.zjb.adapter

import android.support.v4.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.zjb.R
import com.exz.zjb.bean.KeyAndValueBean
import kotlinx.android.synthetic.main.item_list_filter.view.*
import org.jetbrains.anko.textColor


class ListFilterAdapter<T : KeyAndValueBean>: BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_list_filter, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        helper.itemView.title.text=item.value
        helper.itemView.title.textColor=ContextCompat.getColor(mContext,if (item.isCheck)R.color.colorPrimary else R.color.MaterialGrey700)
        helper.itemView.title.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,if (item.isCheck)ContextCompat.getDrawable(mContext,R.drawable.vector_check)else null,null)
        helper.itemView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.White))

    }

}