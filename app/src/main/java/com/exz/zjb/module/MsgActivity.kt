package com.exz.zjb.module

import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.zjb.DataCtrlClass
import com.exz.zjb.R
import com.exz.zjb.adapter.MsgAdapter
import com.exz.zjb.bean.MsgBean
import com.exz.zjb.utils.SZWUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.config.Constants
import com.szw.framelibrary.utils.RecycleViewDivider
import com.szw.framelibrary.utils.StatusBarUtil
import kotlinx.android.synthetic.main.action_bar_custom.*
import kotlinx.android.synthetic.main.activity_hot_news.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */
class MsgActivity : BaseActivity(), OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private var refreshState = Constants.RefreshState.STATE_REFRESH
    private var currentPage = 1
    private lateinit var mAdapter: MsgAdapter<MsgBean>
    override fun initToolbar(): Boolean {
        mTitle.text = "消息"
        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)
        StatusBarUtil.setPaddingSmart(this, blurView)
        StatusBarUtil.setMargin(this, header)
        SZWUtils.setPaddingSmart(mRecyclerView,10f)
        return false
    }

    override fun setInflateId(): Int= R.layout.activity_hot_news

    override fun init() {
        SZWUtils.setRefreshAndHeaderCtrl(this,header,refreshLayout)
        initRecycler()
        initEvent()
//        refreshLayout.autoRefresh()
    }

    private fun initEvent() {
        toolbar.setNavigationOnClickListener { finish() }

    }

    private fun initRecycler() {
        mAdapter = MsgAdapter()
        mAdapter.bindToRecyclerView(mRecyclerView)
        mAdapter.setOnLoadMoreListener(this,mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addItemDecoration(RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(mContext, R.color.app_bg)))
        mRecyclerView.addOnItemTouchListener(object :OnItemClickListener(){
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
//                val intent = Intent(mContext, MyWebActivity::class.java)
//                intent.putExtra(Intent_Url, mAdapter.data[position].url)
//                intent.putExtra(Intent_Title, "")
//                startActivity(intent)
            }
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
        DataCtrlClass.mineMsgData(this, currentPage) {
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
        fun <T :MsgBean>removeItem(adapter: MsgAdapter<T>,id:String){
            val iterator = adapter.data.iterator()
                while (iterator.hasNext()) {
                    val temp = iterator.next()
                    if (temp.id == id) {
                        val indexOf = adapter.data.indexOf(temp)
                        iterator.remove()
                        if (adapter.data.size <= 0) {
                            adapter.notifyDataSetChanged()
                        } else {
                            adapter.notifyItemRemoved(indexOf)
                        }
                    }
                }
        }
    }
}