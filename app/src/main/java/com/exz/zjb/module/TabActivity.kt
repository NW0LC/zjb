package com.exz.zjb.module

import android.content.Context
import android.support.v4.app.Fragment
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.exz.zjb.R
import com.exz.zjb.bean.TabEntity
import com.exz.zjb.module.SearchActivity.Companion.Intent_Search_Content
import com.flyco.tablayout.listener.CustomTabEntity
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_main_tab.*

/**
 * Created by 史忠文
 */

class TabActivity : BaseActivity() {

    private lateinit var mTitles: ArrayList<CustomTabEntity>
    private val mFragments = ArrayList<Fragment>()
    override fun initToolbar(): Boolean {
        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_main_tab

    override fun init() {
        mTitles = when (intent.getStringExtra(Intent_Tab)) {
            "11","12" -> {
                mFragments.add(MainTabFragment.newInstance("11"))
                mFragments.add(MainTabFragment.newInstance("12"))
                arrayListOf(TabEntity("出租信息"), TabEntity("求租信息"))
            }
            "21","22" -> {
                mFragments.add(MainTabFragment.newInstance("21"))
                mFragments.add(MainTabFragment.newInstance("22"))
                arrayListOf(TabEntity("出租信息"), TabEntity("求租信息"))
            }
            "31","32"-> {
                mFragments.add(MainTabFilterFragment.newInstance())
                mFragments.add(MainTabFragment.newInstance("32"))
                arrayListOf(TabEntity("出售信息"), TabEntity("求购信息"))
            }
            "41","42"-> {
                mFragments.add(MainTabFragment.newInstance("41"))
                mFragments.add(MainTabFragment.newInstance("42"))
                arrayListOf(TabEntity("招聘信息"), TabEntity("求职信息"))
            }
            else -> {
                mFragments.add(MainTabFragment.newInstance())
                mFragments.add(MainTabFragment.newInstance())
                arrayListOf(TabEntity("参数"), TabEntity("错误"))
            }
        }

        tab.setTabData(mTitles, this, R.id.frameLayout, mFragments)
        when (intent.getStringExtra(Intent_Tab)) {
            "11" -> { tab.currentTab=0;mFragments[0].arguments?.putString(Intent_Search_Content,intent.getStringExtra(Intent_Search_Content)?:"")}
            "12" -> { tab.currentTab=1;mFragments[1].arguments?.putString(Intent_Search_Content,intent.getStringExtra(Intent_Search_Content)?:"")}
            "21" -> { tab.currentTab=0;mFragments[0].arguments?.putString(Intent_Search_Content,intent.getStringExtra(Intent_Search_Content)?:"")}
            "22" -> { tab.currentTab=1;mFragments[1].arguments?.putString(Intent_Search_Content,intent.getStringExtra(Intent_Search_Content)?:"")}
            "31" -> { tab.currentTab=0;mFragments[0].arguments?.putString(Intent_Search_Content,intent.getStringExtra(Intent_Search_Content)?:"")}
            "32" -> { tab.currentTab=1;mFragments[1].arguments?.putString(Intent_Search_Content,intent.getStringExtra(Intent_Search_Content)?:"")}
            "41" -> { tab.currentTab=0;mFragments[0].arguments?.putString(Intent_Search_Content,intent.getStringExtra(Intent_Search_Content)?:"")}
            "42" -> { tab.currentTab=1;mFragments[1].arguments?.putString(Intent_Search_Content,intent.getStringExtra(Intent_Search_Content)?:"")}
            else -> {
            }
        }
        initEvent()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (SearchActivity.isShouldHideKeyboard(v, ev)) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                frameLayout.isFocusable = true
                frameLayout.isFocusableInTouchMode = true
                frameLayout.requestFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }

    }

    companion object {
        var Intent_Tab = "Intent_Tab"
    }
}