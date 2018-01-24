package com.exz.zjb.module.mine

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.blankj.utilcode.util.EncryptUtils
import com.exz.zjb.R
import com.exz.zjb.config.Urls
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.Response
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.StatusBarUtil
import com.szw.framelibrary.utils.net.NetEntity
import com.szw.framelibrary.utils.net.callback.DialogCallback
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_idea.*
import java.util.*

/**
 * Created by weicao on 2017/8/10.
 * 意见反馈
 */

class IdeaActivity : BaseActivity(), View.OnClickListener {


    override fun initToolbar(): Boolean {
        mTitle.text = "意见反馈"
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, header)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        toolbar.setNavigationOnClickListener { finish() }
        return true
    }

    override fun setInflateId(): Int = R.layout.activity_idea

    override fun init() {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                val length = editable.length

                number.text = String.format("%s", length)
            }
        })
        submit.setOnClickListener(this)
    }
    override fun onClick(view: View) {
        when (view.id) {
            R.id.submit -> initPort()
        }
    }


    private fun initPort() {
        val params = HashMap<String, String>()
        params["userId"] = MyApplication.loginUserId
        params["content"] = editText.text.toString()
        params["requestCheck"] = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId + MyApplication.salt).toLowerCase()
        OkGo.post<NetEntity<Void>>(Urls.url)
                .params(params)
                .tag(this)
                .execute(object : DialogCallback<NetEntity<Void>>(mContext) {

                    override fun onSuccess(response: Response<NetEntity<Void>>) {
                        if (response.body().getCode() == Constants.NetCode.SUCCESS) {
                            finish()
                        }

                    }

                })
    }

}
