package com.exz.zjb.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.zjb.R
import com.exz.zjb.bean.GoodsBean
import com.szw.framelibrary.imageloder.GlideApp
import kotlinx.android.synthetic.main.item_main_tab_sale.view.*


class MainTabSaleAdapter<T : GoodsBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_main_tab_sale, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView=helper.itemView
        GlideApp.with(mContext).load(item.image).into(itemView.sale_img)
        itemView.sale_tv_title.text=item.title
        itemView.sale_tv_address.text=item.city
        itemView.sale_tv_date.text=item.date
    }
}


