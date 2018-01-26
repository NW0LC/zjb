package com.exz.zjb.adapter

import android.support.v7.widget.RecyclerView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.zjb.R
import com.exz.zjb.bean.GoodsBean
import com.szw.framelibrary.imageloder.GlideApp
import kotlinx.android.synthetic.main.item_main.view.*


class MainAdapter<T : GoodsBean> : BaseQuickAdapter<T, BaseViewHolder>(R.layout.item_main, ArrayList<T>()) {

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView=helper.itemView
        GlideApp.with(mContext).load(item.image).into(itemView.img)
        itemView.tv_title.text=item.title
        itemView.tv_address.text=item.city
        itemView.tv_date.text=item.date

        itemView.img.layoutParams.width=(ScreenUtils.getScreenWidth()-SizeUtils.dp2px(1f))/2
        itemView.img.layoutParams.height=(ScreenUtils.getScreenWidth()-SizeUtils.dp2px(1f))/2-SizeUtils.dp2px(30f)
        val layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)

        if (if (headerLayoutCount==0)helper.adapterPosition%2==1 else helper.adapterPosition%2==0) {
            layoutParams.leftMargin= SizeUtils.dp2px(0.5f)
        }else{
            layoutParams.rightMargin=SizeUtils.dp2px(0.5f)
        }
        layoutParams.topMargin= SizeUtils.dp2px(1f)
        itemView.layoutParams= layoutParams
    }
}


