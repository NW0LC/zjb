package com.exz.zjb.module

import android.content.Intent
import android.support.v4.app.Fragment
import com.exz.zjb.DataCtrlClassX
import com.exz.zjb.R
import com.exz.zjb.bean.TabEntity
import com.exz.zjb.module.LoginActivity.Companion.RESULT_LOGIN_CANCELED
import com.exz.zjb.module.LoginActivity.Companion.RESULT_LOGIN_OK
import com.exz.zjb.module.push.PushActivity
import com.exz.zjb.module.push.PushActivity.Companion.Intent_Push_Type
import com.exz.zjb.module.push.PushDeviceChooseActivity
import com.exz.zjb.pop.pushBtn.MenuPop
import com.exz.zjb.utils.SZWUtils
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main_mine.*
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {
    private val mTitles = arrayOf("首页", "发布", "我的")
    private val mIconUnSelectIds = intArrayOf(R.mipmap.icon_main_off, R.mipmap.icon_main_push, R.mipmap.icon_mine_off)
    private val mIconSelectIds = intArrayOf(R.mipmap.icon_main_on, R.mipmap.icon_main_push, R.mipmap.icon_mine_on)
    private val mTabEntities = ArrayList<CustomTabEntity>()
    private val mFragments = ArrayList<Fragment>()
    private var oldPosition = 0
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

                        mainTabBar.currentTab=oldPosition
                        if (SZWUtils.checkLogin(this@MainActivity))
                        pop = MenuPop(this@MainActivity) {
                            val viewId = it.id
                            DataCtrlClassX.getUserInfo(this@MainActivity, {
                                refreshLayout.finishRefresh()
                                if (it != null) {
                                    //实名认证：-1未申请 0审核中，1已通过 2未通过"
                                    when (it.data?.authenticationState) {
                                        "-1", "2" -> {
                                            startActivity(Intent(this@MainActivity, IDProveActivity::class.java))
                                        }
                                        "0" -> {
                                            mContext.toast("实名认证审核中")
                                        }
                                        "1" -> {
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

                                    }
                                }
                            })

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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_LOGIN_OK && requestCode == 0xc8) {
            mainTabBar.currentTab = newPosition
        } else if (resultCode == RESULT_LOGIN_CANCELED && requestCode == 0xc8)
            mainTabBar.currentTab = oldPosition


    }
}
