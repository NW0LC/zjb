package com.exz.zjb.bean

/**
 * Created by 史忠文
 * on 2017/10/19.
 */

class BannersBean(private var image: String = "") :AbsBanner{
    override fun getImgUri()= image

    /**
     *        "adsId":"1",
     *        "mark":"1",
     *        "id":"1",
     *        "image":"http://123.png",
     *        "url":"网页地址"
     *
     */

    var adsId: String=""
    var mark: String=""
    var id: String=""
    var url: String=""

}
