package com.exz.zjb.module

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.RegexUtils
import com.exz.zjb.DataCtrlClass
import com.exz.zjb.R
import com.exz.zjb.bean.CheckAuthenticationBean
import com.exz.zjb.pop.SchemePop
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.ui.ImageGridActivity
import com.lzy.imagepicker.view.CropImageView
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.imageloder.GlideApp
import com.szw.framelibrary.imageloder.GlideImageLoader
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_idprove.*
import org.jetbrains.anko.toast

/**
 * Created by 史忠文
 * on 2018/1/10.
 */
class IDProveActivity : BaseActivity(), View.OnClickListener {
    private var imgOn = ""
    private var imgOff = ""
    private var checkAuthenticationBean: CheckAuthenticationBean? = null
    override fun onClick(p0: View?) {
        if (p0 == img_on) {
            PermissionCameraWithCheck(Intent(this, ImageGridActivity::class.java), 100, false)
        } else {
            PermissionCameraWithCheck(Intent(this, ImageGridActivity::class.java), 200, false)
        }
    }

    override fun initToolbar(): Boolean {
        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)

        mTitle.text = "实名认证"
        blurView.setOverlayColor(ContextCompat.getColor(this, R.color.White))
        toolbar.setNavigationOnClickListener { finish() }
        return false
    }

    private fun initCamera() {
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

    override fun setInflateId() = R.layout.activity_idprove
    override fun init() {
        initCamera()
        img_on.setOnClickListener(this)
        img_off.setOnClickListener(this)
        DataCtrlClass.checkAuthentication(this) {
            if (it != null) {
                checkAuthenticationBean = it
                when (it.checkState) {//'-1:未提交审核信息 0未审核(审核中) 1审核通过 2拒绝
                    "-1" -> {

                    }
                    "0" -> {
                    }
                    "1" -> {

                    }
                    "2" -> {

                        if (it.checkResult?.reason?.isEmpty() != true) {
                            val pop = SchemePop(this@IDProveActivity)
                            pop.data = it.checkResult?.reason.toString()
                            pop.showPopupWindow()
                        }
                        if (it.checkResult?.userName?.check != "1") {
                            ed_name.setTextColor(ContextCompat.getColor(this, R.color.MaterialRed400))
                        }
                        if (it.checkResult?.IDNumber?.check != "1") {
                            ed_id.setTextColor(ContextCompat.getColor(this, R.color.MaterialRed400))
                        }
                        ed_name.setText(it.checkResult?.userName?.value ?: "")
                        ed_id.setText(it.checkResult?.IDNumber?.value ?: "")
                        GlideApp.with(this).load(it.checkResult?.IDCardPositive?.value
                                ?: "").into(img_on)
                        GlideApp.with(this).load(it.checkResult?.IDCardReverse?.value
                                ?: "").into(img_off)
                        tv_imgOnPass.visibility = if (it.checkResult?.IDCardPositive?.check == "2") View.VISIBLE else View.GONE
                        tv_imgOffPass.visibility = if (it.checkResult?.IDCardReverse?.check == "2") View.VISIBLE else View.GONE
                        ed_name.addTextChangedListener(object : TextWatcher {
                            override fun afterTextChanged(p0: Editable?) {
                                ed_name.setTextColor(ContextCompat.getColor(this@IDProveActivity, R.color.MaterialGrey700))
                            }

                            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            }

                            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            }
                        })
                        ed_id.addTextChangedListener(object : TextWatcher {
                            override fun afterTextChanged(p0: Editable?) {
                                ed_id.setTextColor(ContextCompat.getColor(this@IDProveActivity, R.color.MaterialGrey700))
                            }

                            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            }

                            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            }
                        })
                    }
                    else -> {
                    }
                }
            }
        }
    }

    fun submit(view: View) {
        when {
            ed_name.text.isEmpty()||ed_name.currentTextColor ==ContextCompat.getColor(this, R.color.MaterialRed400) -> ed_name.setShakeAnimation()
            ed_id.text.isEmpty()||ed_name.currentTextColor ==ContextCompat.getColor(this, R.color.MaterialRed400) -> ed_id.setShakeAnimation()
            RegexUtils.isIDCard18(ed_id.text.toString())-> toast("请输入正确的身份证号码")
            checkAuthenticationBean == null && imgOn.isEmpty() || checkAuthenticationBean?.checkResult?.IDCardPositive?.check != "1" && imgOn.isEmpty() -> toast("请传入身份证正面")
            checkAuthenticationBean == null && imgOff.isEmpty() || checkAuthenticationBean?.checkResult?.IDCardReverse?.check != "1" && imgOff.isEmpty() -> toast("请传入身份证正面")
            else -> {
                if (checkAuthenticationBean?.checkState == "2") {

                    DataCtrlClass.editAuthentication(this, if (checkAuthenticationBean?.checkResult?.userName?.check == "2") ed_name.text.toString() else "", if (checkAuthenticationBean?.checkResult?.IDNumber?.check == "2") ed_id.text.toString() else "",
                            if (imgOn.isEmpty()) "" else EncodeUtils.base64Encode2String(FileIOUtils.readFile2BytesByStream(imgOn.replace("file://", ""))),
                            if (imgOff.isEmpty()) "" else EncodeUtils.base64Encode2String(FileIOUtils.readFile2BytesByStream(imgOff.replace("file://", "")))) {
                        if (it != null) {
                            finish()
                        }
                    }
                } else {
                    DataCtrlClass.submitAuthentication(this, ed_name.text.toString(), ed_id.text.toString(),
                            EncodeUtils.base64Encode2String(FileIOUtils.readFile2BytesByStream(imgOn.replace("file://", ""))),
                            EncodeUtils.base64Encode2String(FileIOUtils.readFile2BytesByStream(imgOff.replace("file://", "")))) {
                        if (it != null) {
                            finish()
                        }
                    }
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (resultCode == ImagePicker.RESULT_CODE_ITEMS && data != null) { //图片选择
                val images = data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<*>
                if (requestCode == 100) {
                    imgOn = ("file:///" + (images[0] as ImageItem).path)
                    GlideApp.with(this).load(imgOn).into(img_on)
                    tv_imgOnPass.visibility = View.GONE

                } else if (requestCode == 200) {
                    imgOff = ("file:///" + (images[0] as ImageItem).path)
                    GlideApp.with(this).load(imgOff).into(img_off)
                    tv_imgOffPass.visibility =View.GONE
                }
            }
        } catch (e: Exception) {
        }
    }
}