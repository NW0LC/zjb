package com.exz.zjb.bean

/**
 * Created by 史忠文
 * on 2017/10/25.
 */
class ListFilterBean(var id: String = "", var title: String = "") : KeyAndValueBean() {

    override fun absKey()=id
    override fun absValue()=title

}