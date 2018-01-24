package com.exz.zjb.pop

import android.app.Activity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import com.blankj.utilcode.util.ScreenUtils
import com.exz.zjb.R
import com.exz.zjb.adapter.ListFilterAdapter
import com.exz.zjb.bean.ListFilterBean
import kotlinx.android.synthetic.main.pop_list.view.*
import razerdp.basepopup.BasePopupWindow

class ListSortPop(context: Activity?, listener: (title: String, id: String, position: Int) -> Unit) : BasePopupWindow(context) {

    private var firstSetData = true
    var default: String = ""
    var position = 0
    var data = ArrayList<ListFilterBean>()
        set(value) {
            field = value
            if (firstSetData) {
                firstSetData = false
//                val bean = ListFilterBean()
//                bean.name="全部"
//                bean.isCheck=true
//                value.add(0,bean)
            }
            adapter.setNewData(value)

        }
    var adapter: ListFilterAdapter<ListFilterBean>

    init {
        isDismissWhenTouchOutside = true
        popupWindowView.recyclerView.layoutManager = LinearLayoutManager(context)
        popupWindowView.recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        adapter = ListFilterAdapter()
        adapter.bindToRecyclerView(popupWindowView.recyclerView)
        adapter.setOnItemClickListener { _, _, position ->
            adapter.data.forEach { it.isCheck = false }
            adapter.data[position].isCheck = true
            adapter.notifyDataSetChanged()
            val value = adapter.data[position].value
            listener.invoke(
                    if (position == 0)
                        default
                    else {
                        if (this@ListSortPop.position == 0)
                            if (value.length > 6) value.substring(0, 6) + ".." else value
                        else
                            value
                    },
                    adapter.data[position].key,
                    this@ListSortPop.position)
            dismiss()
        }

    }

    override fun initShowAnimation(): Animation {
        val set = AnimationSet(false)
        val showAnimation = TranslateAnimation(0f, 0f, -ScreenUtils.getScreenHeight().toFloat(), 0f)
//        shakeAnimation.interpolator = CycleInterpolator(5f)//循环几次
        showAnimation.duration = 400
//        set.addAnimation(defaultAlphaAnimation)
        set.addAnimation(showAnimation)
        return set
    }

    override fun getClickToDismissView(): View = popupWindowView

    override fun onCreatePopupView(): View = createPopupById(R.layout.pop_list)

    override fun initAnimaView(): View = findViewById(R.id.popup_animation)


}