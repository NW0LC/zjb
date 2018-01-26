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
import com.exz.zjb.config.Urls
import com.exz.zjb.module.MainActivity.Companion.checkPass
import com.exz.zjb.module.SearchActivity.Companion.Intent_Search_Content
import com.exz.zjb.module.SearchActivity.Companion.Intent_isShowSoft
import com.exz.zjb.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.DialogUtils
import com.szw.framelibrary.utils.RecycleViewDivider
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_main_tab.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class MainTabFragment : MyBaseFragment(), OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: MainTabAdapter<GoodsBean>
    private var url = ""
    private var typeId = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main_tab, container, false)
        return rootView
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            onRefresh(refreshLayout)
        }
    }


    override fun initView() {
        editText.setText(arguments?.getString(Intent_Search_Content)?:"")
        initBar()
        initRecycler()

    }

    override fun initEvent() {
        when (arguments?.get(Intent_Type)) {
            "11" -> {
                typeId = "1";url = Urls.leaseList
            }
            "12" -> {
                typeId = "1";url = Urls.rentList
            }
            "21" -> {
                typeId = "2";url = Urls.leaseList
            }
            "22" -> {
                typeId = "2";url = Urls.rentList
            }
            "32" -> {
                url = Urls.buyList
            }
            "41" -> {
                url = Urls.recruiterList
            }
            "42" -> {
                url = Urls.jobWantedList
            }
            else -> {
            }
        }
    }


    private fun initRecycler() {
        mAdapter = MainTabAdapter()
        mAdapter.bindToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        refreshLayout.setOnRefreshListener(this)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.addItemDecoration(RecycleViewDivider(context!!, LinearLayoutManager.VERTICAL, SizeUtils.dp2px(1f), ContextCompat.getColor(context!!, R.color.MaterialGrey400)))
        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                checkPass(context) {
                    startActivity(Intent(context, MainTabDetailActivity::class.java).putExtra("id", mAdapter.data[position].id)
                            .putExtra(Intent_Type, arguments?.get(Intent_Type).toString()))
                }
            }

            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View, position: Int) {
                super.onItemChildClick(adapter, view, position)
                val mEntity = mAdapter.data[position]
                when (view.id) {
                    R.id.img -> {
                        checkPass(context) {
                            DialogUtils.Call(context as BaseActivity, mEntity.mobile)
                        }

                    }
                }
            }
        })
    }

    private fun initBar() {
        StatusBarUtil.setPaddingSmart(context, mRecyclerView)
        StatusBarUtil.setMargin(context, header)
        SZWUtils.setPaddingSmart(mRecyclerView, 45f)
        SZWUtils.setMargin(header, 45f)
        StatusBarUtil.setMargin(context, buttonBarLayout)
        SZWUtils.setRefreshAndHeaderCtrl(this, header, refreshLayout)
        editText.postDelayed({
            val isShowSoft = arguments?.getBoolean(Intent_isShowSoft, false)
            if (isShowSoft == true) {
                editText.isFocusable = true
                editText.isFocusableInTouchMode = true
                editText.requestFocus()
                val inputManager = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.showSoftInput(editText, 0)
            }
        }, 200)

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
        DataCtrlClass.mainTabList(context, url, typeId, editText.text.toString(), currentPage) {
            refreshLayout?.finishRefresh()
            if (it != null) {
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
        var Intent_Type = "Intent_Type"
        fun newInstance(type: String = ""): MainTabFragment {
            val bundle = Bundle()
            val fragment = MainTabFragment()
            bundle.putString(Intent_Type, type)
            fragment.arguments = bundle
            return fragment
        }
    }
}