package com.exz.zjb.module.mine

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.zjb.DataCtrlClass
import com.exz.zjb.R
import com.exz.zjb.adapter.MainTabAdapter
import com.exz.zjb.bean.GoodsBean
import com.exz.zjb.config.Urls
import com.exz.zjb.utils.SZWUtils
import com.lzy.okgo.OkGo
import com.lzy.okgo.request.PostRequest
import com.lzy.okgo.request.base.Request
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.app.MyApplication.Companion.salt
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.RecycleViewDivider
import com.szw.framelibrary.utils.StatusBarUtil
import com.szw.framelibrary.utils.net.NetEntity
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.fragment_mine_center.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class CenterFragment : MyBaseFragment(), OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: MainTabAdapter<GoodsBean>
    private lateinit var postRequest: Request<NetEntity<ArrayList<String>>, PostRequest<NetEntity<ArrayList<String>>>>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_mine_center, container, false)
        return rootView
    }

    override fun initView() {
        initBar()
        initRecycler()

    }

    override fun initEvent() {
        when (arguments?.get(Intent_Type) ?: "") {
            "1" -> {//收藏
                mAdapter.isDelete = true
                val params = HashMap<String, String>()
                params["userId"] = MyApplication.loginUserId
                params["page"] = currentPage.toString()
                params["requestCheck"] = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase()
                postRequest = OkGo.post<NetEntity<ArrayList<String>>>(Urls.url).params(params)
                mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
                    override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                    }
                })
            }
            "2" -> {//浏览记录
                mAdapter.isDelete = true
                val params = HashMap<String, String>()
                params["userId"] = MyApplication.loginUserId
                params["page"] = currentPage.toString()
                params["requestCheck"] = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase()
                postRequest = OkGo.post<NetEntity<ArrayList<String>>>(Urls.url).params(params)
                mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
                    override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                    }
                })
            }
            "3" -> {//我的出售
                mAdapter.isDelete = true
                mAdapter.isEdit = true
                val params = HashMap<String, String>()
                params["userId"] = MyApplication.loginUserId
                params["page"] = currentPage.toString()
                params["requestCheck"] = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase()
                postRequest = OkGo.post<NetEntity<ArrayList<String>>>(Urls.url).params(params)
                mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
                    override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                    }
                })
            }
            "4" -> {//我的求购
                mAdapter.isDelete = true
                mAdapter.isEdit = true
                val params = HashMap<String, String>()
                params["userId"] = MyApplication.loginUserId
                params["page"] = currentPage.toString()
                params["requestCheck"] = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase()
                postRequest = OkGo.post<NetEntity<ArrayList<String>>>(Urls.url).params(params)
                mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
                    override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                    }
                })
            }
            "5" -> {//我的出租
                mAdapter.isDelete = true
                mAdapter.isEdit = true
                val params = HashMap<String, String>()
                params["userId"] = MyApplication.loginUserId
                params["page"] = currentPage.toString()
                params["requestCheck"] = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase()
                postRequest = OkGo.post<NetEntity<ArrayList<String>>>(Urls.url).params(params)
                mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
                    override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                    }
                })
            }
            "6" -> {//我的求租
                mAdapter.isDelete = true
                mAdapter.isEdit = true
                val params = HashMap<String, String>()
                params["userId"] = MyApplication.loginUserId
                params["page"] = currentPage.toString()
                params["requestCheck"] = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase()
                postRequest = OkGo.post<NetEntity<ArrayList<String>>>(Urls.url).params(params)
                mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
                    override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                    }
                })
            }
            "7" -> {//我的招聘
                mAdapter.isDelete = true
                mAdapter.isEdit = true
                val params = HashMap<String, String>()
                params["userId"] = MyApplication.loginUserId
                params["page"] = currentPage.toString()
                params["requestCheck"] = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase()
                postRequest = OkGo.post<NetEntity<ArrayList<String>>>(Urls.url).params(params)
                mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
                    override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                    }
                })
            }
            "8" -> {//我的求职
                mAdapter.isDelete = true
                mAdapter.isEdit = true
                val params = HashMap<String, String>()
                params["userId"] = MyApplication.loginUserId
                params["page"] = currentPage.toString()
                params["requestCheck"] = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId, salt).toLowerCase()
                postRequest = OkGo.post<NetEntity<ArrayList<String>>>(Urls.url).params(params)
                mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
                    override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                    }
                })
            }
            else -> {
            }
        }
    }


    private fun initRecycler() {

        mAdapter = MainTabAdapter()
        mAdapter.setNewData(listOf(GoodsBean(1), GoodsBean(2)))
        mAdapter.bindToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        refreshLayout.setOnRefreshListener(this)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.addItemDecoration(RecycleViewDivider(context!!, LinearLayoutManager.VERTICAL, SizeUtils.dp2px(1f), ContextCompat.getColor(context!!, R.color.MaterialGrey400)))

    }

    private fun initBar() {
        mTitle.text = when (arguments?.get(Intent_Type).toString()) {
            "1" -> {
                "我的收藏"
            }
            "2" -> {
                "浏览记录"
            }
            "3" -> {
                "我的出售"
            }
            "4" -> {
                "我的求购"
            }
            "5" -> {
                "我的出租"
            }
            "6" -> {
                "我的求租"
            }
            "7" -> {
                "我的招聘"
            }
            "8" -> {
                "我的求职"
            }
            else -> {
                ""
            }
        }
        StatusBarUtil.setPaddingSmart(context, mRecyclerView)
        StatusBarUtil.setMargin(context, header)
        SZWUtils.setRefreshAndHeaderCtrl(this, header, refreshLayout)
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
        DataCtrlClass.newsData(context, currentPage) {
            refreshLayout?.finishRefresh()
//            if (it != null) {
//                if (refreshState == Constants.RefreshState.STATE_REFRESH) {
//                    mAdapter.setNewData(it)
//                } else {
//                    mAdapter.addData(it)
//                }
//                if (it.isNotEmpty()) {
//                    mAdapter.loadMoreComplete()
//                    currentPage++
//                } else {
//                    mAdapter.loadMoreEnd()
//                }
//            } else {
//                mAdapter.loadMoreFail()
//            }
        }

    }

    companion object {
        var Intent_Type = "Intent_Type"
        fun newInstance(type: String = ""): CenterFragment {
            val bundle = Bundle()
            val fragment = CenterFragment()
            bundle.putString(Intent_Type, type)
            fragment.arguments = bundle
            return fragment
        }
    }
}