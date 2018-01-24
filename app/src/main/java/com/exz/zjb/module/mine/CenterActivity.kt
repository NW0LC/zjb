package com.exz.zjb.module.mine

import android.support.v4.app.Fragment
import com.exz.zjb.R
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil

class CenterActivity : BaseActivity() {
    lateinit var fragment:Fragment
    override fun initToolbar(): Boolean {
        StatusBarUtil.darkMode(this)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_mine_center

    override fun init() {
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = CenterFragment.newInstance()
        fragment.arguments?.putString(CenterFragment.Intent_Type,intent.getStringExtra(CenterFragment.Intent_Type)?:"")
        transaction.add(R.id.frameLay, fragment)
        transaction.commit()
    }

}