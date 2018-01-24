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
import com.exz.zjb.R
import com.exz.zjb.module.mine.CenterActivity
import com.exz.zjb.module.mine.CenterFragment
import com.exz.zjb.module.mine.PersonInfoActivity
import com.exz.zjb.module.mine.SettingsActivity
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
            if (MyApplication.checkUserLogin()) {
                getUserInfo()
            }
        }
    }

    private fun getUserInfo() {

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

    }

    override fun onClick(p0: View?) {
        when (p0) {
            img_head -> {// 个人资料
                val intent = Intent(context, PersonInfoActivity::class.java)
                SZWUtils.checkLogin(this, intent, PersonInfoActivity::class.java.name)
            }
            bt_sold, bt_buy, bt_lease, bt_forRent, bt_recruit, bt_job -> {
                val intent = Intent(context, CenterActivity::class.java)
                intent.putExtra(CenterFragment.Intent_Type, when (p0) {
                    bt_sold -> {
                        "3"
                    }
                    bt_buy -> {
                        "4"
                    }
                    bt_lease -> {
                        "5"
                    }
                    bt_forRent -> {
                        "6"
                    }
                    bt_recruit -> {
                        "7"
                    }
                    bt_job -> {
                        "8"
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
        } else if (resultCode == Activity.RESULT_OK) {
            //刷新
            onRefresh(refreshLayout)
        }
    }


    companion object {
        fun newInstance(): MineFragment {
            val bundle = Bundle()
            val fragment = MineFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}