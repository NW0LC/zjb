package com.exz.zjb.module

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.exz.zjb.DataCtrlClassX
import com.exz.zjb.R
import com.exz.zjb.config.Urls
import com.exz.zjb.module.mine.*
import com.exz.zjb.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.scwang.smartrefresh.layout.util.DensityUtil
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main_mine.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class MineFragment : MyBaseFragment(), OnRefreshListener, View.OnClickListener, Toolbar.OnMenuItemClickListener {

    private var mOffset = 0
    private var mScrollY = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main_mine, container, false)
        return rootView
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            getUserInfo()
        }
    }

    /*
    * 获取用户资料
    *
    */
    private fun getUserInfo() {
        if (MyApplication.checkUserLogin()) {
            DataCtrlClassX.getUserInfo(context, {
                refreshLayout.finishRefresh()
                if (it != null) {
                    img_head.setImageURI(it.data?.headImg)
                    tv_userName.text = it.data?.nickname

                    //vip年费开启模式：0关闭 1开启
                    if (it.data?.modeState == "1") {
                        tv_vip.visibility = View.VISIBLE

                        endTime.text = it.data?.endTime
                        lay_indate.visibility = View.VISIBLE
                    } else {
                        lay_indate.visibility = View.INVISIBLE
                    }
                    authenticationState = it.data?.authenticationState ?: ""
                    //实名认证：-1未申请 0审核中，1已通过 2未通过"
                    tv_state.visibility = View.VISIBLE
                    when (it.data?.authenticationState) {
                        "-1" -> {
                            tv_state.text = "未认证"
                        }
                        "0" -> {
                            tv_state.text = "审核中"
                        }
                        "1" -> {
                            tv_state.text = "已认证"
                            tv_vip.visibility = View.VISIBLE
                        }
                        "2" -> {
                            tv_state.text = "未通过"
                        }

                    }


                }
            })
        } else {

            refreshLayout.finishRefresh()
            img_head.setImageURI(Urls.url + "userImg/default.png")
            tv_userName.text = "未登录"
            tv_vip.visibility = View.GONE
            tv_state.visibility = View.GONE

        }
    }

    override fun initView() {
        initBar()
        refreshLayout.setOnRefreshListener(this)
    }


    private fun initBar() {
        mTitle.text = "个人中心"
        //状态栏透明和间距处理
        StatusBarUtil.setPaddingSmart(context, toolbar)
        StatusBarUtil.setPaddingSmart(context, blurView)

        toolbar.navigationIcon = null
        toolbar.inflateMenu(R.menu.menu_mine)
        toolbar.setOnMenuItemClickListener(this)
        refreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            override fun onHeaderPulling(header: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                mOffset = offset / 2
                parallax.translationY = (mOffset - mScrollY).toFloat()
                toolbar.alpha = 1 - Math.min(percent, 1f)
            }

            override fun onHeaderReleasing(header: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                mOffset = offset / 2
                parallax.translationY = (mOffset - mScrollY).toFloat()
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
                    parallax.translationY = (mOffset - mScrollY).toFloat()
                    buttonBarLayout.alpha = 1f * mScrollY / h
                    blurView.alpha = 1f * mScrollY / h
                }
                lastScrollY = scrollNewY
            }
        })
        buttonBarLayout.alpha = 0f
        blurView.alpha = 0f
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_settings -> {
                startActivityForResult(Intent(context, SettingsActivity::class.java), 300)
            }
        }
        return false
    }

    override fun initEvent() {
        img_head.setOnClickListener(this)
        bt_sold.setOnClickListener(this)
        bt_buy.setOnClickListener(this)
        bt_lease.setOnClickListener(this)
        bt_forRent.setOnClickListener(this)
        bt_recruit.setOnClickListener(this)
        bt_job.setOnClickListener(this)
        bt_tab_1.setOnClickListener(this)
        bt_tab_2.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when (p0) {
            img_head -> {// 个人资料
                val intent = Intent(context, PersonInfoActivity::class.java)
                SZWUtils.checkLogin(this, intent, PersonInfoActivity::class.java.name)
            }

            bt_tab_1 -> {
                val intent = Intent(context, CollectBrowseActivity::class.java)
                intent.putExtra("type","7")
                startActivity(intent)
            }
            bt_tab_2 -> {
                val intent = Intent(context, CollectBrowseActivity::class.java)
                intent.putExtra("type","8")
                startActivity(intent)
            }

            bt_sold, bt_buy, bt_lease, bt_forRent, bt_recruit, bt_job -> {
                val intent = Intent(context, CenterActivity::class.java)
                intent.putExtra(CenterFragment.Intent_Type, when (p0) {
                    bt_sold -> {
                        "1"
                    }
                    bt_buy -> {
                        "2"
                    }
                    bt_lease -> {
                        "3"
                    }
                    bt_forRent -> {
                        "4"
                    }
                    bt_recruit -> {
                        "5"
                    }
                    bt_job -> {
                        "6"
                    }
                    else -> {
                        ""
                    }
                })
                SZWUtils.checkLogin(this, intent, CenterActivity::class.java.name)
            }


        }
    }


    override fun onRefresh(refreshLayout: RefreshLayout?) {
        if (MyApplication.checkUserLogin()) {
            getUserInfo()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == LoginActivity.RESULT_LOGIN_CANCELED) {
            (activity as MainActivity).mainTabBar.currentTab = 0
            (activity as MainActivity).oldPosition = 0
        } else if (resultCode == Activity.RESULT_OK) {
            //刷新
            onRefresh(refreshLayout)
        }
    }


    companion object {
        var authenticationState = ""
        fun newInstance(): MineFragment {
            val bundle = Bundle()
            val fragment = MineFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}