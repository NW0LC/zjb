package com.exz.zjb.module.mine

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.blankj.utilcode.util.EncryptUtils
import com.exz.zjb.DataCtrlClassX
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
import org.jetbrains.anko.toast
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

            R.id.submit -> {
                var content=editText.text.toString().trim()
                if(content.isEmpty()){
                    editText.setShakeAnimation()
                    return
                }
                if(content.length<11){
                    toast("请输入至少11个字符的宝贵意见!")
                    return
                }
                DataCtrlClassX.submitFeedback(mContext,content, {
                    if(it!=null){
                        finish()
                    }
                })
            }
        }
    }



}
