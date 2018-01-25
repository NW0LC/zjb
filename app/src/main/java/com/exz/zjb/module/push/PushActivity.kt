package com.exz.zjb.module.push

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import com.bigkoo.pickerview.OptionsPickerView
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.zjb.DataCtrlClass
import com.exz.zjb.R
import com.exz.zjb.adapter.PushAdapter
import com.exz.zjb.bean.GoodsBean
import com.exz.zjb.bean.ProvincesBean
import com.exz.zjb.config.Urls.editBuy
import com.exz.zjb.config.Urls.editJobWanted
import com.exz.zjb.config.Urls.editLease
import com.exz.zjb.config.Urls.editRecruiter
import com.exz.zjb.config.Urls.editRent
import com.exz.zjb.config.Urls.editSell
import com.exz.zjb.config.Urls.jobWantedInfo
import com.exz.zjb.config.Urls.machineBuyInfo
import com.exz.zjb.config.Urls.machineLeaseInfo
import com.exz.zjb.config.Urls.machineRentInfo
import com.exz.zjb.config.Urls.machineSellInfo
import com.exz.zjb.config.Urls.publishBuy
import com.exz.zjb.config.Urls.publishJobWanted
import com.exz.zjb.config.Urls.publishLease
import com.exz.zjb.config.Urls.publishRecruiter
import com.exz.zjb.config.Urls.publishRent
import com.exz.zjb.config.Urls.publishSell
import com.exz.zjb.config.Urls.recruiterInfo
import com.lzy.imagepicker.ImagePicker
import com.lzy.imagepicker.bean.ImageItem
import com.lzy.imagepicker.ui.ImageGridActivity
import com.lzy.imagepicker.view.CropImageView
import com.lzy.okgo.OkGo
import com.lzy.okgo.request.PostRequest
import com.lzy.okgo.request.base.Request
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.app.MyApplication.Companion.salt
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.imageloder.GlideImageLoader
import com.szw.framelibrary.utils.StatusBarUtil
import com.szw.framelibrary.utils.net.NetEntity
import com.szw.framelibrary.view.preview.PreviewActivity
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_IMAGES
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_IS_CAN_DELETE
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_POSITION
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_RESULT
import com.szw.framelibrary.view.preview.PreviewActivity.Companion.PREVIEW_INTENT_SHOW_NUM
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_push.*


/**
 * Created by 史忠文
 * on 2017/10/18.
 */

class PushActivity : BaseActivity(), View.OnClickListener {
    private lateinit var pvOptionsAddress: OptionsPickerView<String>
    private var listAddress: List<ProvincesBean>? = null
    private val optionsProvinces = ArrayList<String>()
    private val optionsCities = ArrayList<ArrayList<String>>()
    private var optionsAddress1 = 0
    private var optionsAddress2 = 0
    private var provinceId = ""
    private var cityId = ""

    private lateinit var imagePicker: ImagePicker
    private lateinit var mAdapter: PushAdapter
    private var photos = ArrayList<String>()
    private var deleteHttpPhotos = ArrayList<String>()

    private lateinit var postRequest: Request<NetEntity<Void>, PostRequest<NetEntity<Void>>>
    private lateinit var postRequestGoodsBean: Request<NetEntity<GoodsBean>, PostRequest<NetEntity<GoodsBean>>>
    private var isEdit = false
    override fun initToolbar(): Boolean {
        toolbar.setNavigationOnClickListener { finish() }

        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, header)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setPaddingSmart(this, scrollView)
        return false
    }

    override fun init() {
        initView()
        initCamera()
        initImgRecycler()
        initEvent()
        initPicker()
    }

    private fun initData() {
        val id = intent.getStringExtra("id") ?: ""
        if (id.isNotEmpty()) {
            val params = HashMap<String, String>()
            params["userId"] = MyApplication.loginUserId
            when (intent.getStringExtra(Intent_Push_Type)) {
                "1" -> {//发布-发布出售
                    params["sellId"] = id
                    postRequestGoodsBean = OkGo.post<NetEntity<GoodsBean>>(machineSellInfo).params(params)
                }
                "2" -> {//发布-发布求购
                    params["buyId"] = id
                    postRequestGoodsBean = OkGo.post<NetEntity<GoodsBean>>(machineBuyInfo).params(params)
                }
                "3" -> {//发布-发布出租
                    params["leaseId"] = id
                    postRequestGoodsBean = OkGo.post<NetEntity<GoodsBean>>(machineLeaseInfo).params(params)
                }
                "4" -> {//发布-发布求租
                    params["rentId"] = id
                    postRequestGoodsBean = OkGo.post<NetEntity<GoodsBean>>(machineRentInfo).params(params)
                }
                "5" -> {//发布-招聘
                    params["recruiterId"] = id
                    postRequestGoodsBean = OkGo.post<NetEntity<GoodsBean>>(recruiterInfo).params(params)
                }
                "6" -> {//发布-求职
                    params["jobWantedId"] = id
                    postRequestGoodsBean = OkGo.post<NetEntity<GoodsBean>>(jobWantedInfo).params(params)
                }
                else -> {
                }
            }
            DataCtrlClass.pushEdit(this, postRequestGoodsBean) {
                if (it != null) {
                    photos.addAll(it.carImageUrl ?: ArrayList())
                    ed_title.setText(it.title)
                    provinceId = it.provinceId
                    cityId = it.cityId
                    optionsAddress1 = listAddress?.indexOfFirst { it.key == provinceId } ?: 0
                    optionsAddress2 = listAddress?.get(optionsAddress1)?.CityList?.indexOfFirst { it.key == cityId } ?: 0
                    tv_address.text = String.format(listAddress?.get(optionsAddress1)?.value + "-" + listAddress?.get(optionsAddress1)?.CityList?.get(optionsAddress2)?.value)
                    ed_date.setText(it.factoryYear)
                    ed_type.setText(it.modelName)
                    ed_description.setText(it.description)
                    ed_phone.setText(it.mobile)
                    intent.putExtra(Intent_Push_Type, it.typeId)
                    val salarys = it.salary.split("-")
                    if (salarys.size == 2) {
                        ed_pay.setText(salarys[0])
                        ed_pay_height.setText(salarys[1])
                    } else
                        ed_pay.setText(it.salary)


                }
            }
        }
    }


    private fun initView() {
        ed_description.setClearIconVisible(false)
        isEdit = (intent.getStringExtra("id") ?: "").isNotEmpty()
        bt_push.text = if (isEdit) {"确定"} else "发布"
        when (intent.getStringExtra(Intent_Push_Type)) {
            "1" -> {//发布-发布出售
                mTitle.text = if (isEdit) {"编辑出售"} else "发布出售"
                lay_pay.visibility = View.GONE
            }
            "2" -> {//发布-发布求购
                mTitle.text = if (isEdit) {"编辑求购"} else "发布求购"
                mPhotoRecyclerView.visibility = View.GONE
                lay_type.visibility = View.GONE
                lay_date.visibility = View.GONE
                lay_pay.visibility = View.GONE
            }
            "3" -> {//发布-发布出租
                mTitle.text = if (isEdit) {"编辑出租"} else "发布出租"
                mPhotoRecyclerView.visibility = View.GONE
                lay_type.visibility = View.GONE
                lay_date.visibility = View.GONE
                lay_pay.visibility = View.GONE
            }
            "4" -> {//发布-发布求租
                mTitle.text = if (isEdit) {"编辑求租"} else "发布求租"
                mPhotoRecyclerView.visibility = View.GONE
                lay_type.visibility = View.GONE
                lay_date.visibility = View.GONE
                lay_pay.visibility = View.GONE
            }
            "5" -> {//发布-招聘
                mTitle.text = if (isEdit) {"编辑招聘"} else "发布招聘"
                mPhotoRecyclerView.visibility = View.GONE
                lay_type.visibility = View.GONE
                lay_date.visibility = View.GONE
            }
            "6" -> {//发布-求职
                mTitle.text = if (isEdit) {"编辑求职"} else "发布求职"
                mPhotoRecyclerView.visibility = View.GONE
                lay_type.visibility = View.GONE
                lay_date.visibility = View.GONE
            }
            else -> {
            }
        }
    }

    private fun initImgRecycler() {
        photos.add(0, Uri.parse("android.resource://" + applicationContext.packageName + "/" + R.mipmap.icon_push_default_add).toString())
        mAdapter = PushAdapter()
        mAdapter.setNewData(photos)
        mAdapter.bindToRecyclerView(mPhotoRecyclerView)
        mPhotoRecyclerView.layoutManager = GridLayoutManager(mContext, 3)
        mPhotoRecyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        mPhotoRecyclerView.isNestedScrollingEnabled = false
        mPhotoRecyclerView.isFocusable = false
        mPhotoRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                if (position == mAdapter.data.size - 1) {
                    imagePicker.selectLimit = 7 - mAdapter.data.size
                    PermissionCameraWithCheck(Intent(this@PushActivity, ImageGridActivity::class.java), false)
                } else {
                    val intent = Intent(mContext, PreviewActivity::class.java)
                    val imgs = ArrayList<String>()
                    imgs.addAll(photos)
                    imgs.removeAt(imgs.lastIndex)
                    intent.putExtra(PREVIEW_INTENT_IMAGES, imgs)
                    intent.putExtra(PREVIEW_INTENT_SHOW_NUM, true)
                    intent.putExtra(PREVIEW_INTENT_IS_CAN_DELETE, true)
                    intent.putExtra(PREVIEW_INTENT_POSITION, position)
                    startActivityForResult(intent, 100)
                }
            }

            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                photos.removeAt(position)
                mAdapter.notifyItemRemoved(position)
            }
        })
    }


    private fun initEvent() {
        lay_address.setOnClickListener(this)
        bt_push.setOnClickListener(this)
    }


    private fun initPicker() {
        pvOptionsAddress = OptionsPickerView(this)
        DataCtrlClass.areaList(this) {
            if (it != null) {
                Thread {
                    var city: ArrayList<String>

                    listAddress = it
                    for (p in listAddress ?: ArrayList()) {
                        city = ArrayList()
                        optionsProvinces.add(p.ProvinceName ?: "")
                        p.CityList?.forEach { city.add(it.CityName ?: "") }
                        optionsCities.add(city)
                    }
                    initData()
                }.start()
            }
        }
        pvOptionsAddress.setOnoptionsSelectListener { options1, option2, _ ->
            try {
                optionsAddress1 = options1
                optionsAddress2 = option2
                tv_address.text = String.format(listAddress?.get(options1)?.value + "-" + listAddress?.get(options1)?.CityList?.get(option2)?.value)
                provinceId = listAddress?.get(options1)?.key ?: ""
                cityId = listAddress?.get(options1)?.CityList?.get(option2)?.key ?: ""
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun initCamera() {
        imagePicker = ImagePicker.getInstance()
        imagePicker.imageLoader = GlideImageLoader()
        //显示相机
        imagePicker.isShowCamera = true
        //是否裁剪
        imagePicker.isCrop = true
        //是否按矩形区域保存裁剪图片
        imagePicker.isSaveRectangle = true
        //圖片緩存
        imagePicker.imageLoader = GlideImageLoader()
        imagePicker.isMultiMode = true//多选
        //矩形尺寸
        imagePicker.style = CropImageView.Style.RECTANGLE
        val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300f, resources.displayMetrics).toInt()
        imagePicker.focusWidth = width
        imagePicker.focusHeight = width
        //圖片輸出尺寸
        imagePicker.outPutX = width
        imagePicker.outPutY = width
    }

    override fun setInflateId(): Int = R.layout.activity_push

    override fun onClick(p0: View?) {
        when (p0) {
            lay_address -> {
                KeyboardUtils.hideSoftInput(this)
                pvOptionsAddress.setPicker(optionsProvinces, optionsCities, true)
                pvOptionsAddress.setSelectOptions(optionsAddress1, optionsAddress2)
                //三级选择器
                pvOptionsAddress.setCyclic(false)
                pvOptionsAddress.show()
            }
            bt_push -> {
                val images = ArrayList<String>()
                photos.forEach {
                    if (it.contains("file://")) {
                        images.add(it)
                    }
                }
                var salary: String
                if (ed_pay.text.toString().isEmpty()) {
                    salary = ed_pay_height.text.toString()
                } else {
                    salary = ed_pay.text.toString()
                    if (salary.isNotEmpty() && ed_pay_height.text.toString().isNotEmpty())
                        if (salary.toLong() > ed_pay_height.text.toString().toLong())
                            salary = ed_pay_height.text.toString() + "-" + salary
                        else
                            salary += "-" + ed_pay_height.text.toString()
                }
                val params = HashMap<String, String>()
                params["userId"] = MyApplication.loginUserId
                params["title"] = if (ed_title.text.toString().isEmpty() && !isEdit) {
                    ed_title.setShakeAnimation();return
                } else ed_title.text.toString()
                params["mobile"] = if (ed_phone.text.toString().isEmpty() && !isEdit) {
                    ed_phone.setShakeAnimation();return
                } else ed_phone.text.toString()
                params["description"] = if (ed_description.text.toString().isEmpty() && !isEdit) {
                    ed_description.setShakeAnimation();return
                } else ed_description.text.toString()
                params["provinceId"] = provinceId
                params["cityId"] = cityId
                params["requestCheck"] = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId + if (isEdit) intent.getStringExtra("id") else ed_phone.text.toString(), salt).toLowerCase()
                when (intent.getStringExtra(Intent_Push_Type)) {
                    "1" -> {//发布-发布出售
                        params["modelName"] = ed_type.text.toString()
                        params["factoryYear"] = ed_date.text.toString()
                        postRequest = OkGo.post<NetEntity<Void>>(if (isEdit) editSell else publishSell).params(params)
                    }
                    "2" -> {//发布-发布求购
                        postRequest = OkGo.post<NetEntity<Void>>(if (isEdit) editBuy else publishBuy).params(params)
                    }
                    "3" -> {//发布-发布出租
                        params["typeId"] = intent.getStringExtra(Intent_Push_Device) ?: "1"
                        postRequest = OkGo.post<NetEntity<Void>>(if (isEdit) editLease else publishLease).params(params)
                    }
                    "4" -> {//发布-发布求租
                        params["typeId"] = intent.getStringExtra(Intent_Push_Device) ?: "1"
                        postRequest = OkGo.post<NetEntity<Void>>(if (isEdit) editRent else publishRent).params(params)
                    }
                    "5" -> {//发布-招聘
                        params["salary"] = salary
                        postRequest = OkGo.post<NetEntity<Void>>(if (isEdit) editRecruiter else publishRecruiter).params(params)
                    }
                    "6" -> {//发布-求职
                        params["salary"] = salary
                        postRequest = OkGo.post<NetEntity<Void>>(if (isEdit) editJobWanted else publishJobWanted).params(params)
                    }
                    else -> {
                    }
                }
                DataCtrlClass.push(this, postRequest, images) {
                    if (it!=null) {
                        finish()
                    }
                }
            }
            else -> {
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) { //图片选择
            val images = data?.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS) as ArrayList<*>
            for (image in images) {
                photos.add(photos.size - 1, "file://" + (image as ImageItem).path)
                mAdapter.notifyItemChanged(photos.size - 1)
                mAdapter.notifyItemChanged(photos.size - 2)
            }
        } else if (Activity.RESULT_OK == resultCode) {
            val array = data?.getStringArrayListExtra(PREVIEW_INTENT_RESULT)
            array?.forEach {
                photos.remove(it)
                deleteHttpPhotos.add(it)
            }
            mAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        var Intent_Push_Type = "Intent_Push_Type"
        var Intent_Push_Device = "Intent_Push_Device"
    }
}