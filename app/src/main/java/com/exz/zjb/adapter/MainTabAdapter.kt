package com.exz.zjb.adapter

import android.view.View
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.exz.zjb.R
import com.exz.zjb.bean.GoodsBean
import com.szw.framelibrary.imageloder.GlideApp
import kotlinx.android.synthetic.main.item_main_tab.view.*
import kotlinx.android.synthetic.main.item_main_tab_sale.view.*
import kotlinx.android.synthetic.main.layout_mine_center_bt.view.*


class MainTabAdapter<T : GoodsBean> : BaseMultiItemQuickAdapter<T, BaseViewHolder>(ArrayList<T>()) {
    var isDelete=false
    var isEdit=false
    init {
        addItemType(GoodsBean.TYPE_1, R.layout.item_main_tab)
        addItemType(GoodsBean.TYPE_2, R.layout.item_main_tab_sale)
    }

    override fun convert(helper: BaseViewHolder, item: T) {
        val itemView=helper.itemView
        itemView.lay_btn.visibility= View.GONE
        itemView.lay_btn.visibility=if (isDelete&&isEdit)View.VISIBLE else View.GONE
        itemView.tv_left.visibility=if (isDelete)View.VISIBLE else View.GONE
        itemView.tv_right.visibility=if (isEdit)View.VISIBLE else View.GONE
        helper.addOnClickListener(R.id.tv_left)
        helper.addOnClickListener(R.id.tv_right)



        when (helper.itemViewType) {
            GoodsBean.TYPE_1-> {
                itemView.tv_title.text=item.title
                itemView.tv_address.text=item.provinceCity
                itemView.tv_date.text=item.date
                helper.addOnClickListener(R.id.img)
            }
            GoodsBean.TYPE_2-> {
                GlideApp.with(mContext).load(item.image).into(itemView.sale_img)
                itemView.sale_tv_title.text=item.title
                itemView.sale_tv_address.text=item.provinceCity
                itemView.sale_tv_date.text=item.date
            }
            else -> {
            }
        }

    }
}


