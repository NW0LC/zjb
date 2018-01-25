package com.exz.zjb.module.mine

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import com.blankj.utilcode.util.ScreenUtils
import com.exz.zjb.DataCtrlClass
import com.exz.zjb.DataCtrlClassX
import com.exz.zjb.R
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
    private  var mTextEntity: OpenTextBen?=null
    private var textType = 0
    override fun initToolbar(): Boolean {
        toolbar.setNavigationOnClickListener { finish() }

        //状态栏透明和间距处理
        mTitle.text = "个人信息"
        StatusBarUtil.darkMode(this)
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

        DataCtrlClassX.getUserInfo(mContext, {
            refreshLayout.finishRefresh()
            if (it != null) {
                iv_header.setImageURI(it.data!!.headImg)
                tv_nickname.text = it.data!!.nickname
                tv_phone.text= it.data!!.mobile
                tv_company.text= it.data!!.company
                tv_address.text= it.data!!.companyAddress
            }
        })
    }



    private fun initEvent() {
        bt_header.setOnClickListener(this)
        bt_nicename.setOnClickListener(this)
        bt_company.setOnClickListener(this)
        bt_address.setOnClickListener(this)
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
            initUserInfo()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) { //图片选择
            val images = data?.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<*>
            editInfo("headImg", (images[0] as ImageItem).path)
        } else if (resultCode ==100) {
            when (textType) {
                1 -> {//修改昵称
                    mTextEntity = data?.getSerializableExtra("text") as OpenTextBen
                    editInfo("nickname", mTextEntity!!.content)
                }
                2 -> {//修改公司名称
                    mTextEntity = data?.getSerializableExtra("text") as OpenTextBen
                    editInfo("company", mTextEntity!!.content)
                }
                3 -> {//修改公司名称
                    mTextEntity = data?.getSerializableExtra("text") as OpenTextBen
                    editInfo("companyAddress", mTextEntity!!.content)
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
            bt_nicename -> {
                textType = 1
                mTextEntity = OpenTextBen("修改昵称", URLDecoder.decode(tv_nickname.text.toString().trim(), "utf-8"), 10, "*昵称请控制长度不要超过10字")
                b.putSerializable("text", mTextEntity)
                startActivityForResult(Intent(mContext, OpenShopInputTextActivity::class.java).putExtras(b), 100)
            }
            bt_company -> {
                textType = 2
                 mTextEntity = OpenTextBen("修改公司名称", URLDecoder.decode(tv_company.text.toString().trim(), "utf-8"), 15, "*公司地址请控制长度不要超过15字")
                b.putSerializable("text", mTextEntity)
                startActivityForResult(Intent(mContext, OpenShopInputTextActivity::class.java).putExtras(b), 100)
            }

            bt_address -> {
                textType = 3
                mTextEntity = OpenTextBen("修改公司地址", URLDecoder.decode(tv_address.text.toString().trim(), "utf-8"), 60, "*公司地址请控制长度不要超过60字")
                b.putSerializable("text", mTextEntity)
                startActivityForResult(Intent(mContext, OpenShopInputTextActivity::class.java).putExtras(b), 100)
            }
        }
    }


}