package com.exz.zjb.bean



/**
 * Created by pc
 * on 2017/3/16.
 */

class CityBean {
    var info: InfoEntity? = null
    class InfoEntity {
        var provinces: ArrayList<ProvincesEntity>? = null

        class ProvincesEntity(var areaId: String? = null, var areaName: String? = null): KeyAndValueBean() {
            override fun absKey()=areaId?:""

            override fun absValue()=areaName?:""
            var cities: ArrayList<CitiesEntity>? = null

            class CitiesEntity(var areaId: String? = null, var areaName: String? = null): KeyAndValueBean() {
                override fun absKey()=areaId?:""

                override fun absValue()=areaName?:""

                var counties: ArrayList<CountiesEntity>? = null

                class CountiesEntity(var areaId: String? = null, var areaName: String? = null): KeyAndValueBean() {
                    override fun absKey()=areaId?:""

                    override fun absValue()=areaName?:""
                }
            }
        }
    }
}
