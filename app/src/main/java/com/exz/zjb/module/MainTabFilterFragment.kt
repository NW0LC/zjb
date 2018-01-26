package com.exz.zjb.module

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.zjb.DataCtrlClass
import com.exz.zjb.R
import com.exz.zjb.adapter.MainTabAdapter
import com.exz.zjb.bean.GoodsBean
import com.exz.zjb.bean.ListFilterBean
import com.exz.zjb.module.MainActivity.Companion.checkPass
import com.exz.zjb.module.SearchActivity.Companion.Intent_Search_Content
import com.exz.zjb.module.SearchActivity.Companion.Intent_isShowSoft
import com.exz.zjb.pop.AddressPop
import com.exz.zjb.pop.ListSortPop
import com.exz.zjb.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.DialogUtils
import com.szw.framelibrary.utils.RecycleViewDivider
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_main_tab_filter.*
import razerdp.basepopup.BasePopupWindow
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class MainTabFilterFragment : MyBaseFragment(), OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {
    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: MainTabAdapter<GoodsBean>

    private lateinit var listPop: ListSortPop
    private  var addressPop: AddressPop?=null
    private lateinit var timeSort: ArrayList<ListFilterBean>
    private lateinit var yearSort: ArrayList<ListFilterBean>
    private var sortTimeId=""
    private var sortYearId=""
    private var provinceId=""
    private var cityId=""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main_tab_filter, container, false)
        return rootView
    }

    override fun initView() {
        editText.setText(arguments?.getString(Intent_Search_Content)?:"")
        initBar()
        initRecycler()
        initFilterPop()

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            onRefresh(refreshLayout)
        }
    }
    private fun initFilterPop() {
        listPop = ListSortPop(activity) { title, id, position ->
            when (position) {
                0 -> {
                    sortTimeId=id
                    radioButton1.text = title
                    SZWUtils.setGreyOrYellow(context,radioButton1, timeSort.indexOfFirst { it.id ==id }==0)
                }
                1 -> {
                    sortYearId=id
                    radioButton2.text = title
                    SZWUtils.setGreyOrYellow(context,radioButton2, yearSort.indexOfFirst { it.id ==id }==0)
                }
                else -> {
                }
            }
            onRefresh(refreshLayout)
        }
        timeSort = SZWUtils.getTimeSortData()
        timeSort[0].isCheck = true
        sortTimeId = timeSort[0].key
        yearSort = SZWUtils.getYearSortData()
        yearSort[0].isCheck = true
        sortYearId = yearSort[0].key


        listPop.data = timeSort
        listPop.position=0
        listPop.default="发布时间"

//        val cityBean = Convert.fromJson<CityBean>(getJson(), object : TypeToken<CityBean>(){}.type)
        DataCtrlClass.areaList(context){
            if (it != null) {
                addressPop= AddressPop(activity){title, pr, cty,_ ->
                    radioButton3.text = title
                    provinceId=pr
                    cityId=cty
                    SZWUtils.setGreyOrYellow(context,radioButton3, it.indexOfFirst { it.ProvinceId ==pr }==0)

                    onRefresh(refreshLayout)
                }
                addressPop?.data=it
            }
        }




        val dismissListener: BasePopupWindow.OnDismissListener = object : BasePopupWindow.OnDismissListener() {
            override fun onDismiss() {
                radioGroup.clearCheck()
            }
        }
        listPop.onDismissListener = dismissListener
        addressPop?.onDismissListener = dismissListener
    }
    private fun getJson(): String {

        val stringBuilder = StringBuilder()
        try {
            val assetManager = context?.assets
            val bf = BufferedReader(InputStreamReader(assetManager?.open("city.json")))
            var b = true
            while (b) {
                val line = bf.readLine()
                if (line != null) {
                    stringBuilder.append(line)
                } else {
                    b = false
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return stringBuilder.toString()
    }
    override fun initEvent() {
        radioButton1.setOnClickListener(this)
        radioButton2.setOnClickListener(this)
        radioButton3.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.radioButton1 ->
                if (!listPop.isShowing ) {
                    listPop.data = timeSort
                    listPop.position=0
                    listPop.default="发布时间"
                    listPop.showPopupWindow(radioGroup)
                } else
                    radioGroup.clearCheck()
            R.id.radioButton2 -> {
                if (!listPop.isShowing ) {
                    listPop.data = yearSort
                    listPop.position=1
                    listPop.default="出厂年限"
                    listPop.showPopupWindow(radioGroup)
                } else
                    radioGroup.clearCheck()
            }

            R.id.radioButton3 -> {
                if (addressPop?.isShowing ==false) {
                    addressPop?.showPopupWindow(radioGroup)
                } else
                    radioGroup.clearCheck()
            }
        }
    }
    private fun initRecycler() {

        mAdapter = MainTabAdapter()
        mAdapter.bindToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        refreshLayout.setOnRefreshListener(this)
        mAdapter.setOnLoadMoreListener(this,mRecyclerView)
        mRecyclerView.addItemDecoration(RecycleViewDivider(context!!, LinearLayoutManager.VERTICAL, SizeUtils.dp2px(1f), ContextCompat.getColor(context!!, R.color.MaterialGrey800)))
        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                checkPass(context) {
                    startActivity(Intent(context,GoodsDetailActivity::class.java).putExtra("id",mAdapter.data[position].id))
                }

            }
            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View, position: Int) {
                val mEntity= mAdapter.data[position]
                when (view.id) {
                    R.id.img -> {
                        checkPass(context) {
                            DialogUtils.Call(context as BaseActivity,mEntity.mobile)
                        }
                    }
                }
            }
        })
    }

    private fun initBar() {
        StatusBarUtil.setPaddingSmart(context,mRecyclerView)
        StatusBarUtil.setMargin(context,header)
        SZWUtils.setPaddingSmart(mRecyclerView,80f)
        SZWUtils.setMargin(header,80f)
        StatusBarUtil.setMargin(context, buttonBarLayout)
        SZWUtils.setRefreshAndHeaderCtrl(this,header,refreshLayout)
        editText.postDelayed({val isShowSoft = arguments?.getBoolean(Intent_isShowSoft, false)
            if (isShowSoft==true) {
                editText.isFocusable = true
                editText.isFocusableInTouchMode = true
                editText.requestFocus()
                val inputManager = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.showSoftInput(editText, 0)
            }},200)

        editText.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // do something
                onRefresh(refreshLayout)
                return@OnEditorActionListener true
            }
            false
        })
    }

    override fun onRefresh(refreshLayout: RefreshLayout?) {
        currentPage = 1
        refreshState = Constants.RefreshState.STATE_REFRESH
        iniData()

    }

    override fun onLoadMoreRequested() {
        refreshState = Constants.RefreshState.STATE_LOAD_MORE
        iniData()
    }

    private fun iniData() {
        DataCtrlClass.sellList(context,editText.text.toString(),sortYearId,provinceId,cityId ,sortTimeId,currentPage) {
            refreshLayout?.finishRefresh()
            if (it != null) {
                it.forEach { it.type=GoodsBean.TYPE_2 }
                if (refreshState == Constants.RefreshState.STATE_REFRESH) {
                    mAdapter.setNewData(it)
                } else {
                    mAdapter.addData(it)
                }
                if (it.isNotEmpty()) {
                    mAdapter.loadMoreComplete()
                    currentPage++
                } else {
                    mAdapter.loadMoreEnd()
                }
            } else {
                mAdapter.loadMoreFail()
            }
        }

    }
    companion object {
        fun newInstance(): MainTabFilterFragment {
            val bundle = Bundle()
            val fragment = MainTabFilterFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}