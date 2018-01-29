package com.exz.zjb.module

import android.support.v4.content.ContextCompat
import android.view.View
import com.exz.zjb.DataCtrlClass
import com.exz.zjb.R
import com.exz.zjb.bean.GoodsBean
import com.exz.zjb.config.Urls
import com.exz.zjb.module.MainTabFragment.Companion.Intent_Type
import com.exz.zjb.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.DialogUtils
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_main_tab_detail.*
import kotlinx.android.synthetic.main.layout_owner_info.*

/**
 * Created by 史忠文
 * on 2018/1/10.
 */
class MainTabDetailActivity : BaseActivity(), View.OnClickListener, OnRefreshListener {
    var typeId = "" //类型：1出售信息 2求购信息 3出租信息 4求租信息 5招聘信息 6求职信息
    private var goodsBean: GoodsBean? = null
    private var key = ""
    private var url = ""
    override fun initToolbar(): Boolean {
        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        StatusBarUtil.setMargin(this, header)
        mTitle.text = "信息详情"
        toolbar.setNavigationOnClickListener { finish() }
        SZWUtils.setRefreshAndHeaderCtrl(this, header, refreshLayout)
        return false
    }

    override fun setInflateId() = R.layout.activity_main_tab_detail
    override fun init() {
        when (intent.getStringExtra(Intent_Type) ?: "") {
            "11" -> { //出租
                url = Urls.getLeaseInfo;key = "leaseId";lay_pay.visibility = View.GONE
                typeId="3"
            }
            "21" -> {
                url = Urls.getLeaseInfo;key = "leaseId";lay_pay.visibility = View.GONE
                typeId="3"
            }
            "12"->{//求租
                url = Urls.getRentInfo;key = "rentId";lay_pay.visibility = View.GONE
                typeId="4"
            }
            "22" -> {//求租
                url = Urls.getRentInfo;key = "rentId";lay_pay.visibility = View.GONE
                typeId="4"
            }
            "32"->{//求购
                url = Urls.getBuyInfo;key = "buyId";lay_pay.visibility = View.GONE
                typeId="2"
            }
            "41" -> { //招聘
                url = Urls.getRecruiterInfo;key = "recruiterId";lay_pay.visibility = View.VISIBLE
                typeId="5"
            }
            "42" -> {//求职
                url = Urls.getJobWantedInfo;key = "jobWantedId";lay_pay.visibility = View.VISIBLE
                typeId="6"
            }
            else -> {
            }
        }
        initEvent()
        onRefresh(refreshLayout)
    }

    private fun initData() {
        DataCtrlClass.browseAction(this,typeId,intent.getStringExtra("id") ?: "","1"){}
        DataCtrlClass.getTabDetail(this, url, key, intent.getStringExtra("id") ?: "") {
            refreshLayout?.finishRefresh()
            if (it != null) {
                goodsBean = it
                tv_title.text = it.title
                tv_address.text = it.provinceCity
                tv_date.text = it.date
                tv_description.text = it.description
                tv_pay.text = it.salary


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

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        initData()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            bt_favorite -> {//收藏 操作
                if (goodsBean != null && SZWUtils.checkLogin(this)) {
                    DataCtrlClass.editFavoriteData(this, intent.getStringExtra("id") ?: "", typeId,
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