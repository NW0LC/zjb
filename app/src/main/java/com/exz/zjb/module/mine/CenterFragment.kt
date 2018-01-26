package com.exz.zjb.module.mine

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.SizeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.exz.zjb.DataCtrlClass
import com.exz.zjb.R
import com.exz.zjb.adapter.MainTabAdapter
import com.exz.zjb.bean.GoodsBean
import com.exz.zjb.config.Urls
import com.exz.zjb.module.GoodsDetailActivity
import com.exz.zjb.module.MainActivity.Companion.checkPass
import com.exz.zjb.module.MainTabDetailActivity
import com.exz.zjb.module.MainTabFragment
import com.exz.zjb.module.push.PushActivity
import com.exz.zjb.module.push.PushActivity.Companion.Intent_Push_Type
import com.exz.zjb.utils.SZWUtils
import com.lzy.okgo.OkGo
import com.lzy.okgo.request.PostRequest
import com.lzy.okgo.request.base.Request
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.app.MyApplication.Companion.salt
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.DialogUtils
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
    private lateinit var postRequest: Request<NetEntity<ArrayList<GoodsBean>>, PostRequest<NetEntity<ArrayList<GoodsBean>>>>
    private lateinit var postRequestVoid: Request<NetEntity<Void>, PostRequest<NetEntity<Void>>>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_mine_center, container, false)
        return rootView
    }

    override fun initView() {
        initBar()
        initRecycler()
    }

    override fun initEvent() {
        val params = HashMap<String, String>()
        mAdapter.isDelete = true
        mAdapter.isEdit = true

        params["userId"] = MyApplication.loginUserId
        params["page"] = currentPage.toString()
        params["requestCheck"] = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId + currentPage.toString(), salt).toLowerCase()
        when (arguments?.get(Intent_Type) ?: "") {
            "7" -> {//收藏
                mAdapter.isDelete = false
                mAdapter.isEdit = false
                params["typeId"] = arguments?.get(Intent_TypeId).toString()
                postRequest = OkGo.post<NetEntity<ArrayList<GoodsBean>>>(Urls.myCollection).params(params)
            }
            "8" -> {//浏览记录
                mAdapter.isDelete = false
                mAdapter.isEdit = false
                params["typeId"] = arguments?.get(Intent_TypeId).toString()
                postRequest = OkGo.post<NetEntity<ArrayList<GoodsBean>>>(Urls.MyBrowse).params(params)
            }
            "1" -> {//我的出售
                postRequest = OkGo.post<NetEntity<ArrayList<GoodsBean>>>(Urls.mySellList).params(params)
            }
            "2" -> {//我的求购
                postRequest = OkGo.post<NetEntity<ArrayList<GoodsBean>>>(Urls.myBuyList).params(params)
            }
            "3" -> {//我的出租
                postRequest = OkGo.post<NetEntity<ArrayList<GoodsBean>>>(Urls.myLeaseList).params(params)
            }
            "4" -> {//我的求租
                postRequest = OkGo.post<NetEntity<ArrayList<GoodsBean>>>(Urls.myRentList).params(params)
            }
            "5" -> {//我的招聘
                postRequest = OkGo.post<NetEntity<ArrayList<GoodsBean>>>(Urls.myRecruiterList).params(params)
            }
            "6" -> {//我的求职
                postRequest = OkGo.post<NetEntity<ArrayList<GoodsBean>>>(Urls.MyJobWantedList).params(params)
            }
            else -> {
            }
        }

    }
    override fun onResume() {
        super.onResume()
        onRefresh(refreshLayout)
    }

    private fun initRecycler() {

        mAdapter = MainTabAdapter()
        mAdapter.bindToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        refreshLayout.setOnRefreshListener(this)
        mAdapter.setOnLoadMoreListener(this, mRecyclerView)
        mRecyclerView.addItemDecoration(RecycleViewDivider(context!!, LinearLayoutManager.VERTICAL, SizeUtils.dp2px(1f), ContextCompat.getColor(context!!, R.color.MaterialGrey400)))
        mRecyclerView.addOnItemTouchListener(object : OnItemChildClickListener() {
            override fun onSimpleItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                checkPass(context) {
                    when (view?.id) {
                        R.id.tv_left -> {//删除
                            val params = HashMap<String, String>()
                            params["userId"] = MyApplication.loginUserId
                            com.exz.zjb.utils.DialogUtils.delete(context) {
                                when (arguments?.get(Intent_Type) ?: "") {
//                            "7" -> {//收藏
//                                params["typeId"] = "0"
//                                params["objectId"] = mAdapter.data[position].id
//                                params["collectType"] = "0"
//                                params["requestCheck"] = EncryptUtils.encryptMD5ToString(MyApplication.loginUserId + "0", salt).toLowerCase()
//                                postRequestVoid = OkGo.post<NetEntity<Void>>(Urls.collectAction).params(params)
//                            }
//                            "8" -> {//浏览记录
//                                postRequestVoid = OkGo.post<NetEntity<Void>>(Urls.url).params(params)
//                            }
                                    "1" -> {//我的出售
                                        postRequestVoid = OkGo.post<NetEntity<Void>>(Urls.deleteSell).params(params)
                                    }
                                    "2" -> {//我的求购
                                        postRequestVoid = OkGo.post<NetEntity<Void>>(Urls.deleteBuy).params(params)
                                    }
                                    "3" -> {//我的出租
                                        postRequestVoid = OkGo.post<NetEntity<Void>>(Urls.deleteLease).params(params)
                                    }
                                    "4" -> {//我的求租
                                        postRequestVoid = OkGo.post<NetEntity<Void>>(Urls.deleteRent).params(params)
                                    }
                                    "5" -> {//我的招聘
                                        postRequestVoid = OkGo.post<NetEntity<Void>>(Urls.deleteRecruiter).params(params)
                                    }
                                    "6" -> {//我的求职
                                        postRequestVoid = OkGo.post<NetEntity<Void>>(Urls.deleteJobWanted).params(params)
                                    }
                                    else -> {
                                    }
                                }
                                DataCtrlClass.pushDelete(context, postRequestVoid) {
                                    if (it != null)
                                        onRefresh(refreshLayout)
                                }
                            }
                        }
                        R.id.tv_right -> {
                            startActivity(Intent(context, PushActivity::class.java).putExtra(Intent_Push_Type, arguments?.getString(Intent_Type)).putExtra("id", mAdapter.data[position].id))
                        }
                        R.id.img -> {
                            DialogUtils.Call(context as BaseActivity, mAdapter.data[position].mobile)
                        }
                    }
                }
            }

            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                checkPass(context) {
                    when (arguments?.get(Intent_TypeId).toString()) {
                        "1" -> {//我的出售
                            startActivity(Intent(context, GoodsDetailActivity::class.java).putExtra("id", mAdapter.data[position].id))
                        }
                        "2" -> {//我的求购
                            startActivity(Intent(context, MainTabDetailActivity::class.java).putExtra("id", mAdapter.data[position].id)
                                    .putExtra(MainTabFragment.Intent_Type, "32"))
                        }
                        "3" -> {//我的出租
                            startActivity(Intent(context, MainTabDetailActivity::class.java).putExtra("id", mAdapter.data[position].id)
                                    .putExtra(MainTabFragment.Intent_Type, "11"))
                        }
                        "4" -> {//我的求租
                            startActivity(Intent(context, MainTabDetailActivity::class.java).putExtra("id", mAdapter.data[position].id)
                                    .putExtra(MainTabFragment.Intent_Type, "12"))
                        }
                        "5" -> {//我的招聘
                            startActivity(Intent(context, MainTabDetailActivity::class.java).putExtra("id", mAdapter.data[position].id)
                                    .putExtra(MainTabFragment.Intent_Type, "41"))
                        }
                        "6" -> {//我的求职
                            startActivity(Intent(context, MainTabDetailActivity::class.java).putExtra("id", mAdapter.data[position].id)
                                    .putExtra(MainTabFragment.Intent_Type, "42"))
                        }
                    }

                }
            }


        })
    }
    private fun initBar() {

        mTitle.text = when (arguments?.get(Intent_Type).toString()) {
            "7" -> {
                "我的收藏"
            }
            "8" -> {
                "浏览记录"
            }
            "1" -> {
                "我的出售"
            }
            "2" -> {
                "我的求购"
            }
            "3" -> {
                "我的出租"
            }
            "4" -> {
                "我的求租"
            }
            "5" -> {
                "我的招聘"
            }
            "6" -> {
                "我的求职"
            }
            else -> {
                ""
            }
        }




        StatusBarUtil.setPaddingSmart(context, toolbar)
        StatusBarUtil.setPaddingSmart(context, blurView)
        StatusBarUtil.setPaddingSmart(context, mRecyclerView)
        StatusBarUtil.setMargin(context, header)
        toolbar.setNavigationOnClickListener { activity?.finish() }

        if (arguments?.get(Intent_Type).toString() == "7" || arguments?.get(Intent_Type).toString() == "8") {

            SZWUtils.setPaddingSmart(mRecyclerView, 55f)
            SZWUtils.setMargin(header, 55f)
        }
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
        postRequest.params.put("page", currentPage.toString())
        postRequest.params.put("requestCheck", EncryptUtils.encryptMD5ToString(MyApplication.loginUserId + currentPage.toString(), salt).toLowerCase())
        DataCtrlClass.pushList(context, postRequest) {
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
        var Intent_TypeId = "Intent_TypeId"
        fun newInstance(type: String = "", typeId: String): CenterFragment {
            val bundle = Bundle()
            val fragment = CenterFragment()
            bundle.putString(Intent_Type, type)
            bundle.putString(Intent_TypeId, typeId)
            fragment.arguments = bundle
            return fragment
        }
    }
}