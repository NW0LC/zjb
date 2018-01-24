package com.exz.zjb.bean

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by 史忠文
 * on 2018/1/15.
 */
open class GoodsBean(var type: Int = 1) : MultiItemEntity {
    companion object {
        const val TYPE_1=1
        const val TYPE_2=2
    }
    override fun getItemType(): Int = type





    var id=""
    var title=""
    var image=""
    var city=""
    var provinceCity=""
    var date=""

    /**
     * title : 神钢桩机
     * carImageUrl : ["http://xxx/img1.png","http://xxx/img2.png","http://xxx/img3.png"]
     * provinceCity : 江苏徐州
     * date : 2018-01-18
     * factoryYear : 2017
     * modelName : 神钢SK135R
     * description : 车主描述
     * nickname : 昵称
     * authenticationState : 状态：0审核中，1已通过 2未通过
     * starLevel : 星级，返回数字（1 ~ 5）
     * headImg : http://123.png
     * company : 公司名称
     * "salary":"5000",
     * companyAddress : 公司地址
     * mobile : 联系电话
     * isCollection : 0
     */

    var factoryYear: String=""
    var modelName: String=""
    var description: String=""
    var nickname: String=""
    var authenticationState: String=""
    var starLevel: String=""
    var headImg: String=""
    var company: String=""
    var companyAddress: String=""
    var mobile: String=""
    var isCollection: String=""
    var salary: String=""
    var carImageUrl: List<String>? = null
}