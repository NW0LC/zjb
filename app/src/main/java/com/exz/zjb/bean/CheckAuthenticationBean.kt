package com.exz.zjb.bean

/**
 * Created by 史忠文
 * on 2018/1/22.
 */

class CheckAuthenticationBean {

    /**
     * userId : 1
     * checkState :
     * checkResult : {"userName ":{"value ":"联系人姓名 ","check":"1"},"IDNumber ":{"value ":"身份证号码 ","check ":"1 "},"IDCardPositive ":{"value ":"身份证照片[正面]","check ":"2 "},"IDCardReverse ":{"value ":"身份证照片[反面]","check ":"2 "},"reason ":"拒绝原因 "}
     */

    var userId: String=""
    var checkState: String=""
    var checkResult: CheckResultBean? = null

    class CheckResultBean {
        val reason: String? = null
        var userName :ResultBean?=null
        var IDNumber :ResultBean?=null
        var IDCardPositive :ResultBean?=null
        var IDCardReverse :ResultBean?=null

    }
    class ResultBean {
        var value: String=""
        var check: String=""
    }
}
