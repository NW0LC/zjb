package com.exz.zjb.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.zjb.R
import com.exz.zjb.bean.HotNewsBean
import com.szw.framelibrary.imageloder.GlideApp
import kotlinx.android.synthetic.main.item_main_news.view.*


class NewsAdapter<T : HotNewsBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_main_news, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView=helper.itemView
        GlideApp.with(mContext).load(item.url).into(itemView.img)
        itemView.tv_title.text=item.title
        itemView.tv_count.text=item.clickRate
        itemView.tv_date.text=item.date
    }
}


