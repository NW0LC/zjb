package com.exz.zjb.module

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
import com.blankj.utilcode.util.AppUtils
import com.exz.zjb.DataCtrlClassX
import com.exz.zjb.R
import com.exz.zjb.bean.TabEntity
import com.exz.zjb.module.LoginActivity.Companion.RESULT_LOGIN_CANCELED
import com.exz.zjb.module.LoginActivity.Companion.RESULT_LOGIN_OK
import com.exz.zjb.module.push.PushActivity
import com.exz.zjb.module.push.PushActivity.Companion.Intent_Push_Type
import com.exz.zjb.module.push.PushDeviceChooseActivity
import com.exz.zjb.pop.pushBtn.MenuPop
import com.exz.zjb.utils.DialogUtils
import com.exz.zjb.utils.SZWUtils
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private val mTitles = arrayOf("首页", "发布", "我的")
    private val mIconUnSelectIds = intArrayOf(R.mipmap.icon_main_off, R.mipmap.icon_main_push, R.mipmap.icon_mine_off)
    private val mIconSelectIds = intArrayOf(R.mipmap.icon_main_on, R.mipmap.icon_main_push, R.mipmap.icon_mine_on)
    private val mTabEntities = ArrayList<CustomTabEntity>()
    private val mFragments = ArrayList<Fragment>()
    var oldPosition = 0
    private var newPosition = 0

    private var pop: MenuPop? = null
    override fun initToolbar(): Boolean {
        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        return false
    }

    override fun setInflateId(): Int = R.layout.activity_main
    override fun init() {
        mTitles.indices.mapTo(mTabEntities) { TabEntity(mTitles[it], mIconSelectIds[it], mIconUnSelectIds[it]) }
        mFragments.add(MainFragment.newInstance())
        mFragments.add(Fragment())
        mFragments.add(MineFragment.newInstance())
        mainTabBar.setTabData(mTabEntities, this, R.id.frameLayout, mFragments)
        mainTabBar.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                when (position) {
                    0 -> {
                        oldPosition = position
                    }
                    1 -> {

                        mainTabBar.currentTab = oldPosition
                        if (SZWUtils.checkLogin(this@MainActivity))
                            pop = MenuPop(this@MainActivity) {
                                val viewId = it.id
                                if (SZWUtils.checkLogin(this@MainActivity))
                                checkPass(this@MainActivity) {
                                    when (viewId) {
                                        R.id.bt_tab1 -> {
                                            startActivity(Intent(this@MainActivity, PushDeviceChooseActivity::class.java).putExtra(Intent_Push_Type, "3"))
                                        }
                                        R.id.bt_tab2 -> {
                                            startActivity(Intent(this@MainActivity, PushDeviceChooseActivity::class.java).putExtra(Intent_Push_Type, "4"))
                                        }
                                        R.id.bt_tab3 -> {
                                            startActivity(Intent(this@MainActivity, PushActivity::class.java).putExtra(Intent_Push_Type, "1"))
                                        }
                                        R.id.bt_tab4 -> {
                                            startActivity(Intent(this@MainActivity, PushActivity::class.java).putExtra(Intent_Push_Type, "2"))
                                        }
                                        R.id.bt_tab5 -> {
                                            startActivity(Intent(this@MainActivity, PushActivity::class.java).putExtra(Intent_Push_Type, "5"))
                                        }
                                        R.id.bt_tab6 -> {
                                            startActivity(Intent(this@MainActivity, PushActivity::class.java).putExtra(Intent_Push_Type, "6"))
                                        }
                                        else -> {
                                        }
                                    }
                                }

                                pop?.dismiss()
                            }
                        if (pop?.isShowing == false) {
                            pop?.showPopupWindow()
                        }
                    }
                    2 -> {
                        newPosition = position
                        if (!SZWUtils.checkLogin(this@MainActivity)) {
                            mainTabBar.currentTab = oldPosition
                        } else {
                            oldPosition = position

                        }
                    }
                }
            }

            override fun onTabReselect(position: Int) {
            }
        })



        DataCtrlClassX.updateApk(mContext, AppUtils.getAppVersionName(),{
            if (it != null) {
                if (it.data?.isUpgrade.equals("1")) {
                    DialogUtils.updateApk(mContext, it.data?.tip ?: "", it.data?.isMust ?: "", {
                        val uri = Uri.parse(it.data?.loadUrl)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        if(it.data?.isMust.equals("0")){
                            startActivity(Intent.createChooser(intent, "请选择浏览器"));
                        }else{
                            startActivity(intent)
                            finishAffinity()
                        }
                    })
                }

            }
        })
    }



    companion object {
        fun checkPass(context: Context?, listener: () -> Unit) {
            DataCtrlClassX.getUserInfo(context, {
                if (it != null) {
                    //实名认证：-1未申请 0审核中，1已通过 2未通过"
                    when (it.data?.authenticationState) {
                        "-1" -> {
                            DialogUtils.unCheck(context) {
                                context?.startActivity(Intent(context, IDProveActivity::class.java))
                            }
                        }
                        "2" -> {
                            DialogUtils.checkUnpass(context) {
                                context?.startActivity(Intent(context, IDProveActivity::class.java))
                            }
                        }
                        "0" -> {
                            DialogUtils.checking(context)
                        }
                        "1" -> {
                            listener.invoke()
                        }

                    }
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_LOGIN_OK && requestCode == 0xc8) {
            mainTabBar.currentTab = newPosition
        } else if (resultCode == RESULT_LOGIN_CANCELED && requestCode == 0xc8)
            mainTabBar.currentTab = oldPosition


    }
}
