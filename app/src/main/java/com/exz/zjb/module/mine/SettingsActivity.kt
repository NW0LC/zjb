package com.exz.zjb.module.mine

import android.content.Intent
import android.view.View
import com.blankj.utilcode.util.AppUtils
import com.exz.zjb.DataCtrlClassX
import com.exz.zjb.R
import com.exz.zjb.config.Urls
import com.exz.zjb.module.LoginActivity
import com.exz.zjb.utils.DataCleanManager
import com.exz.zjb.widget.MyWebActivity
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.PreferencesService
import com.szw.framelibrary.utils.DialogUtils
import com.szw.framelibrary.utils.StatusBarUtil
import ezy.boost.update.UpdateUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_settings.*
import me.drakeet.materialdialog.MaterialDialog

/**
 * Created by 史忠文
 * on 2017/10/18.
 */

class SettingsActivity : BaseActivity(), View.OnClickListener {
    override fun initToolbar(): Boolean {
        toolbar.setNavigationOnClickListener { finish() }

        //状态栏透明和间距处理
        mTitle.text = "设置"
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, header)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        return false
    }

    override fun init() {
        tv_version.text="V"+AppUtils.getAppVersionName()
        try {
            tv_clear.text=(DataCleanManager.getTotalCacheSize(this))
        } catch (e: Exception) {
            e.printStackTrace()
        }

//     bt_personInfo.setOnClickListener(this)
        bt_changePwd.setOnClickListener(this)
        bt_clear.setOnClickListener(this)
        bt_help.setOnClickListener(this)
        bt_protocol.setOnClickListener(this)
        bt_about.setOnClickListener(this)
        bt_return.setOnClickListener(this)
        bt_service.setOnClickListener(this)
        bt_exit.setOnClickListener(this)
    }

    override fun setInflateId(): Int = R.layout.activity_settings


    override fun onClick(p0: View?) {
        when (p0) {
            bt_changePwd -> {
                startActivity(Intent(this,ReAccountPwdActivity::class.java))
            }
            bt_clear -> {
                val materialDialog = MaterialDialog(this)
                materialDialog.setTitle("提示").setPositiveButton("确定", {
                    materialDialog.dismiss()
                    DataCleanManager.clearAllCache(this@SettingsActivity)
                    UpdateUtil.clean(this@SettingsActivity)
                    try {
                        tv_clear.text = DataCleanManager.getTotalCacheSize(this@SettingsActivity)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }).setNegativeButton("取消", { materialDialog.dismiss() }).setMessage("要清楚缓存吗？").show()
                }

            bt_help -> {
                val intent = Intent(this, MyWebActivity::class.java)
                intent.putExtra(MyWebActivity.Intent_Title, "使用帮助")
                intent.putExtra(MyWebActivity.Intent_Url, Urls.Help)
                startActivity(intent)
            }
            bt_protocol -> {
                val intent = Intent(this, MyWebActivity::class.java)
                intent.putExtra(MyWebActivity.Intent_Title, "用户协议")
                intent.putExtra(MyWebActivity.Intent_Url,  Urls.Information)
                startActivity(intent)
            }
            bt_about -> {
                val intent = Intent(this, MyWebActivity::class.java)
                intent.putExtra(MyWebActivity.Intent_Title,"关于我们")
                intent.putExtra(MyWebActivity.Intent_Url, Urls.Information2)
                startActivity(intent)
            }
            bt_return -> {
                    startActivity(Intent(this,IdeaActivity::class.java))
            }
            bt_service -> {
                DialogUtils.Call(this,"")
            }
            bt_exit -> {
                DataCtrlClassX.exit(mContext,{
                    if(it!=null){
                        PreferencesService.saveAccount(this, PreferencesService.getAccountKey(this) ?: "", "")
                        MyApplication.user = null
                        setResult(LoginActivity.RESULT_LOGIN_CANCELED)
                        onBackPressed()
                    }
                })

            }
            else -> {
            }
        }
    }

}