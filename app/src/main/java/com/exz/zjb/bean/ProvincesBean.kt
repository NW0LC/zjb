package com.exz.zjb.bean
import com.exz.zjb.bean.KeyAndValueBean

open class ProvincesBean(var ProvinceId: String? = null, var ProvinceName: String? = null) : KeyAndValueBean() {
//    "data":[{
//        "Initial": "A",
//        "ProvinceId": "1",
//        "ProvinceName": "安徽省",
//        "CityList": [{
//            "CityId": "2",
//            "CityName": "安庆市"
//        },...]
//    },...]

    var Initial = ""
    var CityList: ArrayList<CitiesBean>? = null

    override fun absKey() = ProvinceId ?: ""
    override fun absValue() = ProvinceName ?: ""

    class CitiesBean(var CityId: String? = null, var CityName: String? = null) : KeyAndValueBean() {
        override fun absKey() = CityId ?: ""

        override fun absValue() = CityName ?: ""
    }
}