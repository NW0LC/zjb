package com.exz.zjb

import android.content.Context
import com.blankj.utilcode.util.EncryptUtils
import com.exz.zjb.bean.UserInfo
import com.exz.zjb.config.Urls
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.config.PreferencesService
import com.szw.framelibrary.utils.net.NetEntity
import com.szw.framelibrary.utils.net.callback.DialogCallback
import org.jetbrains.anko.toast

/**
 * Created by pc on 2018/1/24.
 */

object DataCtrlClassX {

    /**
     * 获取用户信息
     * */
    fun getUserInfo(context: Context?, listener: (userInfo: NetEntity<UserInfo>?) -> Unit) {
//        mobile	string	必填	手机号
//                password	string	必填	密码
//                jpushToken	string	选填	极光推送令牌
//                deviceType	string	必填	设备类型：1 Android；2 iOS
//        requestCheck	string	必填	验证请求

        val params = HashMap<String, String>()
        params["userId"] = MyApplication.loginUserId
        params["requestCheck"] = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, MyApplication.salt).toLowerCase()
        if (context!=null)
        OkGo.post<NetEntity<UserInfo>>(Urls.GetUserInfo)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<UserInfo>>(context) {
                    override fun onSuccess(response: Response<NetEntity<UserInfo>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body())
                        } else {
                            context.toast(response.body().message)
                            listener.invoke(null)
                        }
                    }

                    override fun onError(response: Response<NetEntity<UserInfo>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }


    /**
     * 获取用户信息
     * */
    fun submitFeedback(context: Context, content: String, listener: (userInfo: NetEntity<Void>?) -> Unit) {
        val params = HashMap<String, String>()
        params["userId"] = MyApplication.loginUserId
        params["content"] = content
        params["mobile"] = PreferencesService.getAccountKey(context) ?: ""
        params["requestCheck"] = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId + MyApplication.salt).toLowerCase()
        OkGo.post<NetEntity<Void>>(Urls.SubmitFeedback)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<Void>>(context) {

                    override fun onSuccess(response: Response<NetEntity<Void>>) {
                        context.toast(response.body().message)
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
     * 获取用户信息
     * */
    fun exit(context: Context, listener: (userInfo: NetEntity<Void>?) -> Unit) {
        val params = HashMap<String, String>()
        params["userId"] = MyApplication.loginUserId
        params["requestCheck"] = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId + MyApplication.salt).toLowerCase()
        OkGo.post<NetEntity<Void>>(Urls.Exit)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<Void>>(context) {

                    override fun onSuccess(response: Response<NetEntity<Void>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            listener.invoke(response.body())
                        } else {
                            context.toast(response.body().message)
                            listener.invoke(null)
                        }

                    }

                    override fun onError(response: Response<NetEntity<Void>>) {
                        super.onError(response)
                        listener.invoke(null)
                    }

                })
    }



}
