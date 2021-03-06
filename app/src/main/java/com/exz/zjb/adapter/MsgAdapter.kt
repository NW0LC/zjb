package com.exz.zjb.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.zjb.DataCtrlClass
import com.exz.zjb.R
import com.exz.zjb.bean.MsgBean
import com.exz.zjb.module.MsgActivity
import kotlinx.android.synthetic.main.item_msg.view.*

class MsgAdapter<T : MsgBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_msg, ArrayList<T>()) {
    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView = helper.itemView
        if (helper.adapterPosition == 0) {
            itemView.tv_date.visibility = View.VISIBLE
        } else if (data[helper.adapterPosition - 1].date == item.date) {
            itemView.tv_date.visibility = View.GONE
        }
        itemView.tv_date.text = item.date
//        itemView.tv_title.text=item.con
        itemView.tv_content.text = item.substance
        itemView.tv_round.visibility = if (item.state == "1") {
            View.GONE
        } else View.VISIBLE
        itemView.btnDelete.setOnClickListener {
            DataCtrlClass.deleteMessage(mContext, data[helper.adapterPosition].id) {it,id->
                if (it!=null)
                MsgActivity.removeItem(this,id)
            }
        }
    }

}