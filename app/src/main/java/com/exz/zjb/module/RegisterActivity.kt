package com.exz.zjb.module

import android.content.Intent
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import com.exz.zjb.DataCtrlClass
import com.exz.zjb.R
import com.exz.zjb.bean.User
import com.exz.zjb.widget.MyWebActivity
import com.exz.zjb.widget.MyWebActivity.Intent_Title
import com.exz.zjb.widget.MyWebActivity.Intent_Url
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.PreferencesService
import com.szw.framelibrary.observer.SmsContentObserver
import com.szw.framelibrary.utils.SZWUtils
import com.szw.framelibrary.utils.StatusBarUtil
import com.szw.framelibrary.utils.StringUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.toast

/**
 * Created by 史忠文
 * on 2018/1/9.
 */
class RegisterActivity: BaseActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
        when (p0) {
            bt_register -> {//注册
                checkRegister()
            }
            bt_code -> {//获取验证码
                getSecurityCode()
            }
            bt_protocol -> {
                //协议
                val intent = Intent(this, MyWebActivity::class.java)
                intent.putExtra(Intent_Url, "")
                intent.putExtra(Intent_Title,"用户使用协议")
                startActivity(intent)
            }
            else -> {
            }
        }
    }

    private var countDownTimer: CountDownTimer?=null
    private val time = 120000//倒计时时间
    private val downKey = "R"
    lateinit var smsContentObserver: SmsContentObserver
    override fun initToolbar(): Boolean {
        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)

        mTitle.text="用户注册"
        blurView.setOverlayColor(ContextCompat.getColor(this,R.color.White))
        toolbar.setNavigationOnClickListener { finish() }
        return false
    }

    override fun setInflateId()= R.layout.activity_register
    override fun init() {
        smsContentObserver = SZWUtils.registerSMS(this, SZWUtils.patternCode(this, ed_code,4))
        val currentTime = System.currentTimeMillis()
        if (PreferencesService.getDownTimer(this, downKey) in 1..(currentTime - 1)) {
            downTimer(time - (currentTime - PreferencesService.getDownTimer(this, downKey)))
        }
     initEvent()
    }

    private fun initEvent() {
        bt_register.setOnClickListener(this)
        bt_code.setOnClickListener(this)
        bt_protocol.setOnClickListener(this)
    }

    private fun downTimer(l: Long) {
        countDownTimer = object : CountDownTimer(l, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                resetTimer(false, millisUntilFinished)
            }

            override fun onFinish() {
                resetTimer(true, java.lang.Long.MIN_VALUE)
            }
        }
        countDownTimer?.start()
    }
    private fun resetTimer(b: Boolean, millisUntilFinished: Long) {
        if (b) {
            countDownTimer?.cancel()
            bt_code.text = "获取验证码"
            bt_code.isClickable = true
            bt_code.setTextColor(ContextCompat.getColor(this, R.color.MaterialRed700))
            PreferencesService.setDownTimer(this, downKey, 0)
        } else {
            bt_code.isClickable = false
            bt_code.setTextColor(ContextCompat.getColor(this, R.color.MaterialGrey400))
            bt_code.text = String.format("重发(%s)s", millisUntilFinished / 1000)
        }

    }
    private fun getSecurityCode() {
        if (TextUtils.isEmpty(ed_phone.text.toString().trim()) || !StringUtil.isPhone(ed_phone.text.toString())) {
            ed_phone.setShakeAnimation()
        } else {
            downTimer(time.toLong())
            PreferencesService.setDownTimer(this, downKey, System.currentTimeMillis())
            DataCtrlClass.getSecurityCode(this, ed_phone.text.toString(), "1") {
                if (it != null) {
                    ed_code.setText(it)
                } else {
                    resetTimer(true, java.lang.Long.MIN_VALUE)
                }
            }
        }
    }
    private fun checkRegister() {
        if (TextUtils.isEmpty(ed_phone.text.toString().trim())) {
            ed_phone.setShakeAnimation()
        } else if (!StringUtil.isPhone(ed_phone.text.toString())) {
            ed_phone.setShakeAnimation()
            toast("手机号码格式不正确")
        } else if (TextUtils.isEmpty(ed_code.text.toString().trim())) {
            ed_code.setShakeAnimation()
        } else if (TextUtils.isEmpty(ed_pwd.text.toString().trim())) {
            ed_pwd.setShakeAnimation()
            toast("请输入密码!")
        } else{
            DataCtrlClass.register(this, ed_phone.text.toString(), ed_code.text.toString(), ed_pwd.text.toString()) {
                if (it != null){
                    startActivity(Intent(this,IDProveActivity::class.java))
                    LoginActivity.loginSuccess(this, ed_phone.text.toString(), ed_pwd.text.toString(), User(it))
                    setResult(LoginActivity.RESULT_LOGIN_OK)
                    finish()
                }
            }
        }
        startActivity(Intent(this,IDProveActivity::class.java))
    }
    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}