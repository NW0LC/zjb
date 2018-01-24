package com.exz.zjb.app

import android.app.Application
import com.exz.zjb.bean.MyObjectBox
import com.szw.framelibrary.app.MyApplication
import io.objectbox.BoxStore


/**
 * Created by 史忠文
 * on 2017/10/16.
 */

class ToolApplication : MyApplication() {
    lateinit var boxStore: BoxStore//数据库入口

    override fun getSaltStr(): String? = "fd6c2498aa0e"

    override fun onCreate() {
        super.onCreate()
        init()
        //数据库初始化
        boxStore = MyObjectBox.builder().androidContext(this).build()
    }


    companion object {
        fun getAPP(app:Application): ToolApplication = app as ToolApplication
    }
}
