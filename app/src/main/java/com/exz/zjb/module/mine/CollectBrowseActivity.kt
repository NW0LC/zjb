package com.exz.zjb.module.mine

import android.support.v4.app.Fragment
import com.exz.zjb.R
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_conter_browse.*

/**
 * Created by pc on 2018/1/25.
 * 我的 收藏 浏览记录
 */

class CollectBrowseActivity : BaseActivity() {
    private val mTitles = arrayOf("求购信息", "出售信息", "求租信息", "出租信息", "求职信息", "招聘信息")
    var type = ""
    private var listFragment = ArrayList<Fragment>()
    override fun initToolbar(): Boolean {
        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setMargin(this, llTab)
        return false
    }

    override fun setInflateId(): Int {
        return R.layout.activity_conter_browse
    }

    override fun init() {
        super.init()


        listFragment.add(CenterFragment.newInstance(intent.getStringExtra("type"), "2"))
        listFragment.add(CenterFragment.newInstance(intent.getStringExtra("type"), "1"))
        listFragment.add(CenterFragment.newInstance(intent.getStringExtra("type"), "4"))
        listFragment.add(CenterFragment.newInstance(intent.getStringExtra("type"), "3"))
        listFragment.add(CenterFragment.newInstance(intent.getStringExtra("type"), "6"))
        listFragment.add(CenterFragment.newInstance(intent.getStringExtra("type"), "5"))
        mTab.setViewPager(mViewPager, mTitles, this@CollectBrowseActivity, listFragment)
    }
}
