package com.exz.zjb.module.push

import android.view.View
import com.exz.zjb.R
import com.exz.zjb.module.push.PushActivity.Companion.Intent_Push_Device
import com.exz.zjb.module.push.PushActivity.Companion.Intent_Push_Type
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_push_lease.*


/**
 * Created by 史忠文
 * on 2017/10/18.
 */

class PushDeviceChooseActivity : BaseActivity(), View.OnClickListener {
    override fun initToolbar(): Boolean {
        toolbar.setNavigationOnClickListener { finish() }

        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, header)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        return false
    }

    override fun init() {
        initView()
        initEvent()

    }

    private fun initEvent() {
        bt_device_1.setOnClickListener(this)
        bt_device_2.setOnClickListener(this)
    }

    private fun initView() {
        when (intent.getStringExtra(Intent_Push_Type)) {
            "3" -> {//发布-发布出租
                mTitle.text="选择出租设备"
            }
            "4" -> {//发布-发布求租
                mTitle.text="选择求租设备"
            }
            else -> {
            }
        }
    }

    override fun setInflateId(): Int = R.layout.activity_push_lease

    override fun onClick(p0: View?) {
        when (p0) {
            bt_device_1 -> {
                startActivity(intent.setClass(this,PushActivity::class.java).putExtra(Intent_Push_Device,"1"))
            }
            bt_device_2 -> {
                startActivity(intent.setClass(this,PushActivity::class.java).putExtra(Intent_Push_Device,"2"))
            }
            else -> {
            }
        }
    }


}