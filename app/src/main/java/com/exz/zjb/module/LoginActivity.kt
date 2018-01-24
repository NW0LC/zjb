package com.exz.zjb.module

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.exz.zjb.DataCtrlClass
import com.exz.zjb.R
import com.exz.zjb.bean.User
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants.Result.Intent_ClassName
import com.szw.framelibrary.config.PreferencesService
import com.szw.framelibrary.utils.StatusBarUtil
import com.szw.framelibrary.utils.StringUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast



/**
 * Created by 史忠文
 * on 2018/1/9.
 */
class LoginActivity : BaseActivity() {
    override fun initToolbar(): Boolean {
        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        toolbar.setNavigationIcon(R.drawable.vector_black_close)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        buttonBarLayout.alpha = 0f
        blurView.alpha = 0f
        toolbar.inflateMenu(R.menu.menu_text)
        val actionView = toolbar.menu.getItem(0).actionView
        (actionView as TextView).text ="注册"
        actionView.setOnClickListener {
            startActivityForResult(Intent(this,RegisterActivity::class.java),100)
        }
        return false
    }

    override fun setInflateId() = R.layout.activity_login


    fun forgetPwd(v: View) {
        startActivity(Intent(this,ForgetPwdActivity::class.java))
    }
    fun checkLogin(v: View) {
        if (TextUtils.isEmpty(ed_phone.text.toString().trim())) {
            ed_phone.setShakeAnimation()
        } else if (!StringUtil.isPhone(ed_phone.text.toString())) {
            ed_phone.setShakeAnimation()
            toast("手机号码格式不正确")
        } else if (TextUtils.isEmpty(ed_pwd.text.toString())) {
            ed_pwd.setShakeAnimation()
            toast("请输入密码!")
        } else {
            DataCtrlClass.login(this, ed_phone.text.toString(), ed_pwd.text.toString()) {
                if (it != null) {
                    ed_phone.postDelayed({
                        LoginActivity.loginSuccess(this, ed_phone.text.toString(), ed_pwd.text.toString(), User(it))
                    }, 500)
                }

            }
        }
    }
    override fun onBackPressed() {
        setResult(RESULT_LOGIN_CANCELED)
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_bottom)
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_LOGIN_OK) {
            setResult(RESULT_LOGIN_OK, data)
            finish()
        }
    }
    companion object {
        val RESULT_LOGIN_OK = 2000
        val RESULT_LOGIN_CANCELED = 3000

        fun loginSuccess(context: Activity, mobile: String, pwd: String, user: User?) {
            PreferencesService.saveAccount(context, mobile, pwd)
            MyApplication.user = user
//            PreferencesService.saveAutoLoginToken(context, user?.autoLoginToken ?: "")
            val intent = context.intent
            val className = intent.getStringExtra(Intent_ClassName)
            if (!TextUtils.isEmpty(className)) {
                intent.setClassName(context, className)
                context.startActivityForResult(intent, 100)
            } else {
                context.setResult(RESULT_LOGIN_OK, intent)
                context.finish()
                context.overridePendingTransition(R.anim.fade_in, R.anim.slide_out_bottom)
            }
        }
    }
}