package com.exz.zjb.module.mine

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import com.blankj.utilcode.util.ScreenUtils
import com.exz.zjb.DataCtrlClass
import com.exz.zjb.R
import com.exz.zjb.bean.User
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.ui.ImageGridActivity
import com.lzy.imagepicker.view.CropImageView
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.imageloder.GlideImageLoader
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_person_info.*
import java.io.Serializable
import java.net.URLDecoder
import java.util.*

/**
 * Created by 史忠文
 * on 2017/10/18.
 */

class PersonInfoActivity : BaseActivity(), View.OnClickListener {
    class OpenTextBen(var className: String, var content: String, var length: Int, var warn: String) : Serializable
    private lateinit var mTextEntity: OpenTextBen
    private var textType = 0
    private var mUserInfo: User?=null
    override fun initToolbar(): Boolean {
        toolbar.setNavigationOnClickListener { finish() }

        //状态栏透明和间距处理
        mTitle.text = "个人信息"
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, header)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        return false
    }

    override fun init() {
        initCamera()
        initEvent()
        initUserInfo()
    }

    private fun initUserInfo() {
    }



    private fun initEvent() {
        bt_header.setOnClickListener(this)
        bt_address.setOnClickListener(this)
        bt_nicename.setOnClickListener(this)
    }

    private fun initCamera() {
        val w = ScreenUtils.getScreenWidth() * 0.2
        val layoutParams = iv_header.layoutParams
        layoutParams.width = w.toInt()
        layoutParams.height = w.toInt()
        iv_header.layoutParams = layoutParams
        val imagePicker = ImagePicker.getInstance()
        imagePicker.imageLoader = GlideImageLoader()
        //显示相机
        imagePicker.isShowCamera = true
        //是否裁剪
        imagePicker.isCrop = true
        //是否按矩形区域保存裁剪图片
        imagePicker.isSaveRectangle = true
        //圖片緩存
        imagePicker.imageLoader = GlideImageLoader()
        imagePicker.isMultiMode = false//单选
        //矩形尺寸
        imagePicker.style = CropImageView.Style.RECTANGLE
        val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300f, resources.displayMetrics).toInt()
        imagePicker.focusWidth = width
        imagePicker.focusHeight = width
        //圖片輸出尺寸
        imagePicker.outPutX = width
        imagePicker.outPutY = width
    }

    override fun setInflateId(): Int = R.layout.activity_person_info

    private fun editInfo(key: String, value: String) {
        DataCtrlClass.editPersonInfo(this, key, value) {
            if (it != null) {
                if (key == "header")
                    iv_header.setImageURI(it)
            }
            if (key == "nickname")
                tv_nickname.text = value
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) { //图片选择
            val images = data?.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<*>
            editInfo("header", (images[0] as ImageItem).path)
        } else if (resultCode ==100) {
            when (textType) {
                1 -> {//公司
                    mTextEntity = data?.getSerializableExtra("text") as OpenTextBen
                    mUserInfo?.nickname=mTextEntity.content
                    editInfo("nickname", mTextEntity.content)
                }
                2 -> {//公司地址
                    mTextEntity = data?.getSerializableExtra("text") as OpenTextBen
                    mUserInfo?.companyAddress=mTextEntity.content
                    editInfo("wechat", mTextEntity.content)
                }
            }
        }
    }


    override fun onClick(p0: View?) {
        val b = Bundle()
        when (p0) {
            bt_header -> {
                PermissionCameraWithCheck(Intent(this, ImageGridActivity::class.java), false)
            }
            bt_company -> {
                textType = 1
                if (mUserInfo != null) mTextEntity = OpenTextBen("修改昵称", URLDecoder.decode(mUserInfo?.nickname, "utf-8"), 15, "*名称请控制长度不要超过15字")
                b.putSerializable("text", mTextEntity)
                startActivityForResult(Intent(mContext, OpenShopInputTextActivity::class.java).putExtras(b), 100)
            }
            bt_address -> {
                textType = 2
                if (mUserInfo != null) mTextEntity = OpenTextBen("修改微信号", URLDecoder.decode(mUserInfo?.companyAddress, "utf-8"), 15, "*公司地址请控制长度不要超过15字")
                b.putSerializable("text", mTextEntity)
                startActivityForResult(Intent(mContext, OpenShopInputTextActivity::class.java).putExtras(b), 100)
            }

            else -> {
            }
        }
    }


}