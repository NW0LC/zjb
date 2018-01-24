package com.exz.zjb.bean

import com.flyco.tablayout.listener.CustomTabEntity

class TabEntity(var title: String, private var selectedIcon: Int=0, private var unSelectedIcon: Int=0) : CustomTabEntity {

    override fun getTabTitle(): String = title

    override fun getTabSelectedIcon(): Int = selectedIcon

    override fun getTabUnselectedIcon(): Int = unSelectedIcon
}