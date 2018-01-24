package com.exz.zjb.bean

import com.exz.zjb.config.Urls
import com.google.gson.annotations.SerializedName

/**
 * Created by pc on 2018/1/24.
 */

class UserInfo {

    /**
     * mobile : 13236027713
     * headImg : http://zjb.xzsem.cn/userImg/default.png
     * nickname : 桩机宝-3
     * company :
     * companyAddress :
     * authenticationState : -1
     * modeState : 0
     * endTime :
     */

    @SerializedName("mobile")
    var mobile: String? = null
    @SerializedName("headImg")
    var headImg = Urls.url+"userImg/default.png"
    @SerializedName("nickname")
    var nickname = ""
    @SerializedName("company")
    var company: String? = null
    @SerializedName("companyAddress")
    var companyAddress: String? = null
    @SerializedName("authenticationState")
    var authenticationState: String? = null
    @SerializedName("modeState")
    var modeState: String? = null
    @SerializedName("endTime")
    var endTime: String? = null
}
