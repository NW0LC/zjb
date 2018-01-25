package com.exz.zjb.module

import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.view.View
import com.exz.zjb.DataCtrlClass
import com.exz.zjb.R
import com.exz.zjb.bean.GoodsBean
import com.exz.zjb.config.Urls
import com.exz.zjb.imageloader.BannerImageLoader
import com.exz.zjb.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.scwang.smartrefresh.layout.util.DensityUtil
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.DialogUtils
import com.szw.framelibrary.utils.StatusBarUtil
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_goods_detail.*
import kotlinx.android.synthetic.main.layout_owner_info.*

/**
 * Created by 史忠文
 * on 2018/1/10.
 */
class GoodsDetailActivity : BaseActivity(), View.OnClickListener {
    private var mScrollY = 0
    private var goodsBean: GoodsBean? = null
    override fun initToolbar(): Boolean {
        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        StatusBarUtil.setMargin(this, header)

        mTitle.text = "商品详情"
        toolbar.setNavigationOnClickListener { finish() }
        refreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            override fun onHeaderPulling(header: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                toolbar.alpha = 1 - Math.min(percent, 1f)
            }

            override fun onHeaderReleasing(header: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                toolbar.alpha = 1 - Math.min(percent, 1f)
            }
        })
        scrollView.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener {
            private var lastScrollY = 0
            private val h = DensityUtil.dp2px(170f)
            override fun onScrollChange(v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                var scrollNewY = scrollY
                if (lastScrollY < h) {
                    scrollNewY = Math.min(h, scrollNewY)
                    mScrollY = if (scrollNewY > h) h else scrollNewY
                    blurView.alpha = 1f * mScrollY / h
                    buttonBarLayout.alpha = 1f * mScrollY / h
                }
                lastScrollY = scrollNewY
            }
        })
        buttonBarLayout.alpha = 0f
        blurView.alpha = 0f
        return false
    }

    override fun setInflateId() = R.layout.activity_goods_detail
    override fun init() {

        initEvent()

        initData()

        initBanner()
    }

    private fun initBanner() {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        //设置图片加载器
        banner.setImageLoader(BannerImageLoader())
        //设置自动轮播，默认为true
        banner.isAutoPlay(true)
        //设置轮播时间
        banner.setDelayTime(3000)
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER)

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun initData() {
        DataCtrlClass.getTabDetail(this, Urls.getSellInfo, "sellId", intent.getStringExtra("id") ?: "") {
            if (it != null) {
                goodsBean = it
                banner.setImages(it.carImageUrl).start()
                tv_title.text = it.title
                tv_date.text = it.date
                tv_year.text = it.factoryYear
                tv_address.text = it.provinceCity
                tv_type.text = it.modelName
                tv_description.text = it.description

                img_head.setImageURI(it.headImg)
                tv_ownerName.text = it.nickname
                tv_ownerName.text = it.nickname
                mRatingBar.rating = it.starLevel.toFloatOrNull() ?: 0f
                tv_ownerPhone.text = String.format("电话:%s", it.mobile)
                tv_ownerCompany.text = String.format("公司:%s", it.company)
                tv_ownerAddress.text = String.format("公司地址:%s", it.companyAddress)
                bt_favorite.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
                        ContextCompat.getDrawable(this,
                                if (goodsBean?.isCollection == "1")
                                    R.mipmap.icon_goods_detail_favorite_on
                                else R.mipmap.icon_goods_detail_favorite_off), null, null)
            }
        }
    }

    private fun initEvent() {
        bt_favorite.setOnClickListener(this)
        bt_connect.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onClick(p0: View?) {
        when (p0) {
            bt_favorite -> {//收藏 操作
                if (goodsBean != null && SZWUtils.checkLogin(this)) {
                    DataCtrlClass.editFavoriteData(this, intent.getStringExtra("id") ?: "", "1",
                            if (goodsBean?.isCollection == "1") {
                                goodsBean?.isCollection = "0"
                                "0"
                            } else {
                                goodsBean?.isCollection = "1"
                                "1"
                            }) {
                        bt_favorite.setCompoundDrawablesRelativeWithIntrinsicBounds(null,
                                ContextCompat.getDrawable(this,
                                        if (goodsBean?.isCollection == "1")
                                            R.mipmap.icon_goods_detail_favorite_on
                                        else R.mipmap.icon_goods_detail_favorite_off), null, null)
                    }
                }
            }
            bt_connect -> DialogUtils.Call(this, goodsBean?.mobile ?: "")
        }
    }

}