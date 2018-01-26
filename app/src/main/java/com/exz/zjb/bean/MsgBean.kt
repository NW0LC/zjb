package com.exz.zjb.bean

/**
 * Created by 史忠文
 * on 2017/12/13.
 */

open class MsgBean(
        /**
         *         "id":"编号",
         *         "substance":"内容",
         *         "state":"阅读状态：0未读 1已读" ,
         *         "date":"2018-01-18",
         *
         *
         */
        var id: String = "") {
    var substance: String=""
    var state: String=""
    var date: String=""
}
