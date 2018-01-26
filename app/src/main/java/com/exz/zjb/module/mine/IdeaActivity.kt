package com.exz.zjb.module.mine

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.exz.zjb.DataCtrlClassX
import com.exz.zjb.R
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_idea.*
import org.jetbrains.anko.toast

/**
 * Created by weicao on 2017/8/10.
 * 意见反馈
 */

class IdeaActivity : BaseActivity(), View.OnClickListener {


    override fun initToolbar(): Boolean {
        mTitle.text = "意见反馈"
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
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

                var phone=ed_phone.text.toString().trim()
                if(phone.isEmpty()){
                    ed_phone.setShakeAnimation()
                    return
                }

                if(phone.length!=11){
                    toast("请输入11位手机号!")
                    return
                }
                DataCtrlClassX.submitFeedback(mContext,content,phone, {
                    if(it!=null){
                        finish()
                    }
                })
            }
        }
    }



}
