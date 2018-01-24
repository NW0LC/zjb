package com.exz.zjb

import android.content.Context
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.FileIOUtils
import com.exz.zjb.bean.*
import com.exz.zjb.config.Urls
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.lzy.okgo.request.PostRequest
import com.lzy.okgo.request.base.Request
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.app.MyApplication.Companion.loginUserId
import com.szw.framelibrary.app.MyApplication.Companion.salt
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.net.NetEntity
import com.szw.framelibrary.utils.net.callback.DialogCallback
import com.szw.framelibrary.utils.net.callback.JsonCallback
import com.szw.framelibrary.view.CustomProgress
import org.jetbrains.anko.toast

/**
 * Created by 史忠文
 * on 2018/1/9.
 */
object DataCtrlClass{
    /**
     * 登录
     * */
    fun loginNoDialog(mobile: String, pwd: String,listener: (userId: String?) -> Unit) {

        val params = HashMap<String, String>()
        params["mobile"] = mobile
        params["password"] = pwd
        params["deviceType"] = "1"
//      params.put("jpushToken", JPushInterface.getRegistrationID(this))
        params["requestCheck"] = EncryptUtils.encryptMD5ToString(mobile+pwd, salt).toLowerCase()
        OkGo.post<NetEntity<String>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : JsonCallback<NetEntity<String>>() {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 登录
     * */
    fun login(context: Context, mobile: String, pwd: String, listener: (userId: String?) -> Unit) {
//        mobile	string	必填	手机号
//                password	string	必填	密码
//                jpushToken	string	选填	极光推送令牌
//                deviceType	string	必填	设备类型：1 Android；2 iOS
//        requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params["mobile"] = mobile
        params["password"] = pwd
        params["deviceType"] = "1"
//      params.put("jpushToken", JPushInterface.getRegistrationID(this))
        params["requestCheck"] = EncryptUtils.encryptMD5ToString(mobile+pwd, salt).toLowerCase()
        OkGo.post<NetEntity<String>>(Urls.Login)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            context.toast(response.body().message)
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 获取验证码
     * @param[phone] string	必填	手机号
     * @param[purpose] string	必填	用途：1注册，2忘记密码，3设置支付密码
     * */
    fun getSecurityCode(context: Context, phone: String, purpose: String, listener: (errorMsg: String?) -> Unit) {
//       mobile	string	必填	手机号
//       purpose	string	必填	用途：1注册,2忘记密码
//       requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params["mobile"] = phone
        params["purpose"] = purpose
        params["requestCheck"] = EncryptUtils.encryptMD5ToString(phone + MyApplication.salt).toLowerCase()
        OkGo.post<NetEntity<String>>(Urls.ObtainCode)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data?:"")
                        } else {
                            listener.invoke(null)
                        }
                        context.toast(response.body().message)
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 注册
     * @param[phone] string	必填	手机号
     * @param[code] string	必填	验证码
     * @param[pwd] string	必填	密码
     * */
    fun register(context: Context, phone: String, code: String, pwd: String, listener: (userId: String?) -> Unit) {
//        mobile	string	必填	手机号
//        code	string	必填	验证码
//        password	string	必填	密码
//        jpushToken	string	选填	极光推送令牌
//        deviceType	string	必填	设备类型：1 Android；2 iOS
//        requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params["mobile"] = phone
        params["code"] = code
        params["password"] = pwd
        params["deviceType"] = "1"
//        params.put("jpushToken", JPushInterface.getRegistrationID(this))
        params["requestCheck"] = EncryptUtils.encryptMD5ToString(phone + code, salt).toLowerCase()
        OkGo.post<NetEntity<String>>(Urls.Register)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                        context.toast(response.body().message)
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 注册
     * @param[phone] string	必填	手机号
     * @param[code] string	必填	验证码
     * @param[pwd] string	必填	密码
     * */
    fun forgetPassword(context: Context, phone: String, code: String, pwd: String, listener: (userId: NetEntity<Void>?) -> Unit) {
//        mobile	string	必填	手机号
//        code	string	必填	验证码
//        password	string	必填	新密码
//        requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params["mobile"] = phone
        params["code"] = code
        params["password"] = pwd
        params["requestCheck"] = EncryptUtils.encryptMD5ToString(phone + code, salt).toLowerCase()
        OkGo.post<NetEntity<Void>>(Urls.ForgetPassword)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<Void>>(context) {
                    override fun onSuccess(response: Response<NetEntity<Void>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body())
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<Void>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 提交实名认证审核资料
     * */
    fun submitAuthentication(context: Context, userName: String, IDNumber: String, IDCardPositive: String,IDCardReverse: String, listener: (userId: NetEntity<Void>?) -> Unit) {
//        userId	string	必填	用户id
//        userName	string	必填	联系人姓名
//        IDNumber	string	必填	身份证号码
//        IDCardPositive	data	必填	身份证照片[正面]（数据流）
//        IDCardReverse	data	必填	身份证照片[反面]（数据流）
//        requestCheck	string	必填	验证请求
//
//

        val params = HashMap<String, String>()
        params["userId"] = loginUserId
        params["userName"] = userName
        params["IDNumber"] = IDNumber
        params["IDCardPositive"] = IDCardPositive
        params["IDCardReverse"] = IDCardReverse
        params["requestCheck"] = EncryptUtils.encryptMD5ToString(loginUserId, salt).toLowerCase()
        OkGo.post<NetEntity<Void>>(Urls.SubmitAuthentication)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<Void>>(context) {
                    override fun onSuccess(response: Response<NetEntity<Void>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body())
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<Void>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    } /**
     * 提交实名认证审核资料
     * */
    fun editAuthentication(context: Context, userName: String, IDNumber: String, IDCardPositive: String,IDCardReverse: String, listener: (userId: NetEntity<Void>?) -> Unit) {
//        userId	string	必填	用户id
//        userName	string	必填	联系人姓名
//        IDNumber	string	必填	身份证号码
//        IDCardPositive	data	必填	身份证照片[正面]（数据流）
//        IDCardReverse	data	必填	身份证照片[反面]（数据流）
//        requestCheck	string	必填	验证请求
//
//

        val params = HashMap<String, String>()
        params["userId"] = loginUserId
        params["userName"] = userName
        params["IDNumber"] = IDNumber
        params["IDCardPositive"] = IDCardPositive
        params["IDCardReverse"] = IDCardReverse
        params["requestCheck"] = EncryptUtils.encryptMD5ToString(loginUserId, salt).toLowerCase()
        OkGo.post<NetEntity<Void>>(Urls.editAuthentication)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<Void>>(context) {
                    override fun onSuccess(response: Response<NetEntity<Void>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body())
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<Void>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 实名认证审核结果接口
     * */
    fun checkAuthentication(context: Context, listener: (userId: CheckAuthenticationBean?) -> Unit) {
        val params = HashMap<String, String>()
        params["userId"] = loginUserId
        params["requestCheck"] = EncryptUtils.encryptMD5ToString(loginUserId, salt).toLowerCase()
        OkGo.post<NetEntity<CheckAuthenticationBean>>(Urls.checkAuthentication)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<CheckAuthenticationBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<CheckAuthenticationBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<CheckAuthenticationBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 首页_Banner
     * */
    fun banner(context: Context?, listener: (userId: ArrayList<BannersBean>?) -> Unit) {
        val params = HashMap<String, String>()
        params["requestCheck"] = EncryptUtils.encryptMD5ToString("Banner", salt).toLowerCase()
        if (context!=null)
        OkGo.post<NetEntity<ArrayList<BannersBean>>>(Urls.banner)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ArrayList<BannersBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ArrayList<BannersBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ArrayList<BannersBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }/**
     * 首页_热点新闻(5条)
     * */
    fun topNews(context: Context?, listener: (userId: ArrayList<TopNewsBean>?) -> Unit) {
        val params = HashMap<String, String>()
        params["requestCheck"] = EncryptUtils.encryptMD5ToString("TopNews", salt).toLowerCase()
        if (context!=null)
        OkGo.post<NetEntity<ArrayList<TopNewsBean>>>(Urls.topNews)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ArrayList<TopNewsBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ArrayList<TopNewsBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ArrayList<TopNewsBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }/**
     * 省市列表
     * */
    fun areaList(context: Context?, listener: (userId: ArrayList<ProvincesBean>?) -> Unit) {
        val params = HashMap<String, String>()
        params["requestCheck"] = EncryptUtils.encryptMD5ToString("AreaList", salt).toLowerCase()
        if (context!=null)
        OkGo.post<NetEntity<ArrayList<ProvincesBean>>>(Urls.areaList)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ArrayList<ProvincesBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ArrayList<ProvincesBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ArrayList<ProvincesBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }/**
     * 首页_最新出售车源（4条）
     * */
    fun newCar(context: Context?, listener: (userId: ArrayList<GoodsBean>?) -> Unit) {
        val params = HashMap<String, String>()
        params["requestCheck"] = EncryptUtils.encryptMD5ToString("NewCar", salt).toLowerCase()
        if (context!=null)
        OkGo.post<NetEntity<ArrayList<GoodsBean>>>(Urls.newCar)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ArrayList<GoodsBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ArrayList<GoodsBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ArrayList<GoodsBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }/**
     * 出售车辆列表
     * */
    fun sellList(context: Context?,keyword:String,factoryYear:String,provinceId:String,cityId:String,sortType:String,currentPage: Int,listener: (userId: ArrayList<GoodsBean>?) -> Unit) {
//        keyword	string	选填	关键词
//        factoryYear	string	选填	出厂年限
//        provinceId	string	选填	省份id
//        cityId	string	选填	城市id
//        sortType	string	必填	排序方式（0综合排序 1发布时间由远到近 2发布时间由近到远）
//        page	string	必填	分页（从第1页开始,每页20条数据）
        val params = HashMap<String, String>()
        params["keyword"] =keyword
        params["factoryYear"] =factoryYear
        params["provinceId"] =provinceId
        params["cityId"] =cityId
        params["sortType"] =sortType
        params["page"] =currentPage.toString()
        params["requestCheck"] = EncryptUtils.encryptMD5ToString(currentPage.toString(), salt).toLowerCase()
        if (context!=null)
        OkGo.post<NetEntity<ArrayList<GoodsBean>>>(Urls.sellList)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ArrayList<GoodsBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ArrayList<GoodsBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ArrayList<GoodsBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }/**
     * 求购车辆列表
     * 出租车辆列表
     * */
    fun mainTabList(context: Context?,url:String,typeId:String,keyword:String,currentPage: Int,listener: (userId: ArrayList<GoodsBean>?) -> Unit) {
//        keyword	string	选填	关键词
//        typeId	string	必填	类型：1桩机 2钢板桩
//        page	string	必填	分页（从第1页开始,每页20条数据）
//        requestCheck	string	必填	验证
        val params = HashMap<String, String>()
        params["typeId"] =typeId
        params["keyword"] =keyword
        params["page"] =currentPage.toString()
        params["requestCheck"] = EncryptUtils.encryptMD5ToString(currentPage.toString(), salt).toLowerCase()
        if (context!=null)
        OkGo.post<NetEntity<ArrayList<GoodsBean>>>(url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ArrayList<GoodsBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ArrayList<GoodsBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ArrayList<GoodsBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 个人中心消息
     * */
    fun mineMsgData(context: Context,currentPage: Int, listener: (informationBeans: ArrayList<MsgBean>?) -> Unit) {
    //userId	string	必填	用户id
//        page	string	必填	分页（从第1页开始,每页20条数据）
//        requestCheck	string	必填	验证请求
        val params = HashMap<String, String>()
        params["userId"] = MyApplication.loginUserId
        params["page"] = currentPage.toString()
        params["requestCheck"] = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId+currentPage.toString(), salt).toLowerCase()
        OkGo.post<NetEntity<ArrayList<MsgBean>>>(Urls.message)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ArrayList<MsgBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ArrayList<MsgBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ArrayList<MsgBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    } /**
     * 热点新闻
     * */
    fun newsData(context: Context?,currentPage: Int, listener: (informationBeans: ArrayList<HotNewsBean>?) -> Unit) {

        val params = HashMap<String, String>()
        params["page"] = currentPage.toString()
        params["requestCheck"] = EncryptUtils.encryptMD5ToString(currentPage.toString(), salt).toLowerCase()
        if (context!=null)
        OkGo.post<NetEntity<ArrayList<HotNewsBean>>>(Urls.hotNews)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<ArrayList<HotNewsBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ArrayList<HotNewsBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ArrayList<HotNewsBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }


    /**
     * tab详情
     * tab详情
     * */
    fun getTabDetail(context: Context,url: String,key: String,id:String,  listener: (goodsBean: GoodsBean?) -> Unit) {
//        userId	string	必填	用户id
//        sellId	string	必填	出售id
//        requestCheck	string	必填	验证请求
        val params = HashMap<String, String>()
        params["userId"] = MyApplication.loginUserId
        params[key] = id
        params["requestCheck"] = EncryptUtils.encryptMD5ToString(id, salt).toLowerCase()
        OkGo.post<NetEntity<GoodsBean>>(url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<GoodsBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<GoodsBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<GoodsBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     *  push 编辑
     * */
    fun pushEdit(context: Context?,postRequest: Request<NetEntity<GoodsBean>, PostRequest<NetEntity<GoodsBean>>>, listener: (goodsBean: GoodsBean?) -> Unit) {
        if (context!=null)
        postRequest.tag(this)
                .execute(object : DialogCallback<NetEntity<GoodsBean>>(context) {
                    override fun onSuccess(response: Response<NetEntity<GoodsBean>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<GoodsBean>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     *  push 编辑
     * */
    fun pushList(context: Context?,postRequest: Request<NetEntity<ArrayList<GoodsBean>>, PostRequest<NetEntity<ArrayList<GoodsBean>>>>, listener: (goodsBean: ArrayList<GoodsBean>?) -> Unit) {
        if (context!=null)
        postRequest.tag(this)
                .execute(object : DialogCallback<NetEntity<ArrayList<GoodsBean>>>(context) {
                    override fun onSuccess(response: Response<NetEntity<ArrayList<GoodsBean>>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<ArrayList<GoodsBean>>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     *  push 删除
     * */
    fun pushDelete(context: Context?,postRequest: Request<NetEntity<Void>, PostRequest<NetEntity<Void>>>, listener: (goodsBean: Void?) -> Unit) {
        if (context!=null)
        postRequest.tag(this)
                .execute(object : DialogCallback<NetEntity<Void>>(context) {
                    override fun onSuccess(response: Response<NetEntity<Void>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<Void>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 发布
     * */
    fun push(context: Context,postRequest: Request<NetEntity<Void>, PostRequest<NetEntity<Void>>>, images:List<String>,listener: (goodsBean: NetEntity<Void>?) -> Unit) {
        CustomProgress.show(context, "加载中", false, null)
        Thread{
            pushImgData(images){
                if (it!=null) {
                    postRequest.params("carImageUrl",it).tag(this)
                            .execute(object : DialogCallback<NetEntity<Void>>(context) {
                                override fun onSuccess(response: Response<NetEntity<Void>>) {
                                    if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                                        listener.invoke(response.body())
                                    } else {
                                        listener.invoke(null)
                                    }
                                    context.toast(response.body().message)
                                }

                                override fun onError(response: Response<NetEntity<Void>>) {
                                    super.onError(response)
                                    listener.invoke(null)
                                }

                            })
                }
            }
        }.start()

    }
    /**
     * 公共方法： 上传图片
     * */
    fun pushImgData(images:List<String>,count:Int=0,listener: (imgName: String?) -> Unit){
        if (images.isNotEmpty()) {
            var str=""
            val params = HashMap<String, String>()
            params["userId"] = MyApplication.loginUserId
            params["carImg"] = EncodeUtils.base64Encode2String(FileIOUtils.readFile2BytesByStream(images[count].replace("file:///","")))
            params["requestCheck"] = EncryptUtils.encryptMD5ToString(  MyApplication.loginUserId, salt).toLowerCase()
            OkGo.post<NetEntity<String>>(Urls.uploadCarImg)
                    .params(params)
                    .tag(this)
                    .execute(object : JsonCallback<NetEntity<String>>() {
                        override fun onSuccess(response: Response<NetEntity<String>>) {
                            if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                                str+=response.body().data+","

                                if (count<images.size-1)
                                    pushImgData(images,count+1){
                                        str+=it
                                        listener.invoke(str)
                                    }
                                else{
                                    listener.invoke(str)
                                }
                            } else {
                                listener.invoke(null)
                            }
                        }

                        override fun onError(response: Response<NetEntity<String>>) {
                            super.onError(response)
                            listener.invoke(null)
                        }

                    })
        }else{
            listener.invoke("")
        }

    }
    /**
     * 【添加/取消】【关注/收藏】
     * */
    fun editFavoriteData(context: Context,id:String, idMark:String,type:String, listener: (goodsBean: NetEntity<Void>?) -> Unit) {
//        userId	string	必填	用户id
//        typeId	string	必填	类型：1出售信息 2求购信息 3出租信息 4求租信息 5招聘信息 6求职信息
//        objectId	string	必填	收藏对象id
//        collectType	string	必填	0:取消收藏 1收藏
//        requestCheck	string	必填	验证请求
        val params = HashMap<String, String>()
        params["userId"] = MyApplication.loginUserId
        params["objectId"] = id
        params["typeId"] = idMark
        params["collectType"] = type
        params["requestCheck"] = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId+id, salt).toLowerCase()
        OkGo.post<NetEntity<Void>>(Urls.collectAction)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<Void>>(context) {
                    override fun onSuccess(response: Response<NetEntity<Void>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body())
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<Void>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 编辑个人信息
     * */
    fun editPersonInfo(context: Context, key: String, value: String, listener: (data: String?) -> Unit) {

        val params = HashMap<String, String>()
        params["userId"] = MyApplication.loginUserId//教练id
        if (key == "header")
            params[key] = EncodeUtils.base64Encode2String(FileIOUtils.readFile2BytesByStream(value))
        else
            params[key] = value
        params["requestCheck"] = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase()
        OkGo.post<NetEntity<String>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data)
                        } else {
                            listener.invoke(null)
                            context.toast(response.message())
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
    /**
     * 修改登录密码
     */
    fun changeAccountPwd(context: Context,oldPwd:String ,newPwd:String ,listener: (data: String?) -> Unit) {
//        userId	/**/string	必填	用户ID
//                pwd	string	必填	旧密码
//                newPwd	string	必填	新密码
//                requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params["userId"] = loginUserId
        params["pwd"] = oldPwd
        params["newPwd"] = newPwd
        params["requestCheck"] = EncryptUtils.encryptMD5ToString(loginUserId +oldPwd+newPwd, MyApplication.salt).toLowerCase()
        OkGo.post<NetEntity<String>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<String>>(context) {
                    override fun onSuccess(response: Response<NetEntity<String>>) {
                        context.toast(response.body().message)
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body().data?:"")
                        } else {
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<String>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }
}