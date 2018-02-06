package com.exz.zjb.pop

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.exz.zjb.R
import com.exz.zjb.adapter.ListFilterAdapter
import com.exz.zjb.bean.ProvincesBean
import com.szw.framelibrary.utils.RecycleViewDivider
import kotlinx.android.synthetic.main.pop_address.view.*
import razerdp.basepopup.BasePopupWindow

class AddressPop(context: Activity?, listener: (title: String, provinceId: String,cityId: String, position: Int) -> Unit) : BasePopupWindow(context) {

    private var firstSetData = true
    private var isSecondShow=false
    private var provinceId=""
    private var cityId=""
    var data = ArrayList<ProvincesBean>()
        set(value) {
            field = value
            if (firstSetData) {//只走一次
                firstSetData=false
                for (provinceBean in value) {
                    provinceBean.CityList?.add(0,ProvincesBean.CitiesBean("","不限"))
                }
                val provinceBean = ProvincesBean()
                provinceBean.ProvinceName = "全部"
                provinceBean.ProvinceId = ""
                provinceBean.isCheck=true
                val city = ArrayList<ProvincesBean.CitiesBean>()
                provinceBean.CityList = city
                value.add(0,provinceBean)
            }
            adapter.setNewData(value)

        }
    var adapter: ListFilterAdapter<ProvincesBean>
    var adapter2: ListFilterAdapter<ProvincesBean.CitiesBean>

    init {
        isDismissWhenTouchOutside = true
        popupWindowView.recyclerView.layoutManager = LinearLayoutManager(context)
        popupWindowView.recyclerView2.layoutManager = LinearLayoutManager(context)
        popupWindowView.recyclerView.addItemDecoration(RecycleViewDivider(context!!, LinearLayoutManager.VERTICAL, SizeUtils.dp2px(1f), ContextCompat.getColor(context, R.color.MaterialGrey800)))
        popupWindowView.recyclerView2.addItemDecoration(RecycleViewDivider(context, LinearLayoutManager.VERTICAL, SizeUtils.dp2px(1f), ContextCompat.getColor(context, R.color.MaterialGrey800)))
        popupWindowView.recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        popupWindowView.recyclerView2.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        adapter = ListFilterAdapter()
        adapter2 = ListFilterAdapter()
        adapter.bindToRecyclerView(popupWindowView.recyclerView)
        adapter2.bindToRecyclerView(popupWindowView.recyclerView2)



        adapter.setOnItemClickListener { _, _, position ->
            adapter.data.forEach { it.isCheck = false }
            adapter.data[position].isCheck = true
            if (position==0) {
                adapter.data.forEach {
                    it.CityList?.forEach {
                        it.isCheck=false
                    }
                }
                provinceId=""
                cityId=""
                listener.invoke("所在地",provinceId,cityId,position)
                isSecondShow=false
                dismiss()
                popupWindowView.recyclerView2.visibility=View.GONE
            }
            adapter2.setNewData(data[position].CityList)
            adapter.notifyDataSetChanged()
            if (!isSecondShow&&position!=0) {
                isSecondShow=!isSecondShow
                popupWindowView.recyclerView2.visibility=View.VISIBLE
                val shakeAnimate = TranslateAnimation((ScreenUtils.getScreenWidth() - SizeUtils.dp2px(140f)).toFloat(), 0f, 0f, 0f)
                shakeAnimate.duration = 300
                popupWindowView.recyclerView2.animation=shakeAnimate
                popupWindowView.recyclerView2.animation.start()
            }
        }
        adapter2.setOnItemClickListener { _, _, position ->
            adapter.data.forEach {
                it.CityList?.forEach {
                    it.isCheck=false
                }
            }
            adapter2.data[position].isCheck = true
            cityId = adapter2.data[position].key
            adapter2.notifyDataSetChanged()

            adapter.data.forEach {
                it.CityList?.forEach {item->
                    if (item.isCheck) {
                        provinceId =it.key
                        val value = adapter2.data[position].value
                        listener.invoke(if (position==0) it.ProvinceName?:"" else if (value.length > 6) value.substring(0, 6) + ".." else value,provinceId,cityId,position)
                    }
                }
            }

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

    override fun onCreatePopupView(): View = createPopupById(R.layout.pop_address)

    override fun initAnimaView(): View = findViewById(R.id.popup_animation)


}