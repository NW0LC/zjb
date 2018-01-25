package com.exz.zjb.module

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.exz.zjb.DataCtrlClass
import com.exz.zjb.R
import com.exz.zjb.adapter.MainAdapter
import com.exz.zjb.bean.BannersBean
import com.exz.zjb.bean.GoodsBean
import com.exz.zjb.imageloader.BannerImageLoader
import com.exz.zjb.module.MainActivity.Companion.checkPass
import com.exz.zjb.module.SearchActivity.Companion.Intent_isShowSoft
import com.exz.zjb.module.TabActivity.Companion.Intent_Tab
import com.exz.zjb.widget.MyWebActivity
import com.exz.zjb.widget.MyWebActivity.Intent_Title
import com.exz.zjb.widget.MyWebActivity.Intent_Url
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.scwang.smartrefresh.layout.util.DensityUtil
import com.szw.framelibrary.base.MyBaseFragment
import com.szw.framelibrary.utils.StatusBarUtil
import com.youth.banner.BannerConfig
import com.youth.banner.listener.OnBannerListener
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.header_main.*
import kotlinx.android.synthetic.main.header_main.view.*

/**
 * Created by 史忠文
 * on 2017/10/17.
 */

class MainFragment : MyBaseFragment(), OnRefreshListener, View.OnClickListener, OnBannerListener {
    var   newsStr=ArrayList<String>()
    private var mScrollY=0
    private lateinit var mAdapter: MainAdapter<GoodsBean>
    private lateinit var headerView: View
    private var banners = ArrayList<BannersBean>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_main, container, false)
        return rootView
    }

    override fun initView() {
        initBar()
        initRecycler()
        initHeaderAndFooter()
        initData()
    }

    private fun initData() {
        refreshLayout?.finishRefresh()
        DataCtrlClass.banner(context){
            if (it != null) {
                banners=it
                headerView.banner.setImages(banners).start()
            }
        }
        DataCtrlClass.topNews(context){
            if (it != null) {
                newsStr.clear()
                for(topNewsBean in it){

                    newsStr.add(topNewsBean.title)
                }
                headerView.marqueeView.startWithList(newsStr)
                headerView.notice.setOnClickListener { startActivity(Intent(context,NewsActivity::class.java)) }

                headerView.marqueeView.setOnItemClickListener { position, _ ->

                    val intent = Intent(context, MyWebActivity::class.java)
                    intent.putExtra(Intent_Url, it[position].url)
                    intent.putExtra(Intent_Title, it[position].title)
                    startActivity(intent)
                }
            }
        }
        DataCtrlClass.newCar(context){
            if (it != null) {
                mAdapter.setNewData(it)
            }
        }
    }

    override fun initEvent() {
        headerView.bt_tab_1.setOnClickListener(this)
        headerView.bt_tab_2.setOnClickListener(this)
        headerView.bt_tab_3.setOnClickListener(this)
        headerView.bt_tab_4.setOnClickListener(this)
        headerView.notice.setOnClickListener(this)
    }


    private fun initRecycler() {

        mAdapter = MainAdapter()
        headerView = View.inflate(context, R.layout.header_main, null)
        mAdapter.addHeaderView(headerView)
        mAdapter.setHeaderAndEmpty(true)
        mAdapter.bindToRecyclerView(mRecyclerView)
        mRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        refreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            override fun onHeaderPulling(header: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                toolbarLay.alpha = 1 - Math.min(percent, 1f)
            }

            override fun onHeaderReleasing(header: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                toolbarLay.alpha = 1 - Math.min(percent, 1f)
            }
        })
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private val h = DensityUtil.dp2px(170f)
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                mScrollY += dy
                if (mScrollY < h) {
//                    blurView.alpha = 1f * mScrollY / h
                    blurView.setBlurRadius((1f * mScrollY / h) * 10)
                    blurView.setOverlayColor(Color.argb(((1f * mScrollY / h) * 204).toInt(), 247, 196, 88))
                }

            }
        })
        refreshLayout.setOnRefreshListener(this)

        mRecyclerView.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                checkPass(context) {
                    startActivity(Intent(context,GoodsDetailActivity::class.java).putExtra("id",mAdapter.data[position].id))
                }
            }
        })
        blurView.setBlurRadius(0f)
        blurView.setOverlayColor(Color.argb(0, 247, 196, 88))
    }

    private fun initBar() {
        //状态栏透明和间距处理
        StatusBarUtil.setPaddingSmart(activity, blurView)
        StatusBarUtil.setPaddingSmart(activity, toolbar)
        mTitle.setOnClickListener {
            startActivity(Intent(context,SearchActivity::class.java).putExtra(Intent_isShowSoft,true))
        }
        mRightImg.setOnClickListener {
            startActivity(Intent(context,MsgActivity::class.java))
        }
    }

    private fun initHeaderAndFooter() {
        val array2=ArrayList<String>()
        array2.add(Uri.parse("android.resource://" + activity?.packageName + "/" +R.mipmap.icon_main_banner).toString())


        headerView.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        //设置图片加载器
        headerView.banner.setImageLoader(BannerImageLoader())
        //设置自动轮播，默认为true
        headerView.banner.isAutoPlay(true)
        //设置轮播时间
        headerView.banner.setDelayTime(3000)
        //设置指示器位置（当banner模式中有指示器时）
        headerView.banner.setIndicatorGravity(BannerConfig.CENTER)


        headerView.banner2.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        //设置图片加载器
        headerView.banner2.setImageLoader(BannerImageLoader())
        //设置自动轮播，默认为true
        headerView.banner2.isAutoPlay(true)
        //设置轮播时间
        headerView.banner2.setDelayTime(3000)
        //设置指示器位置（当banner模式中有指示器时）
        headerView.banner2.setIndicatorGravity(BannerConfig.CENTER)
        headerView.banner2.setImages(array2).start()
    }

    override fun OnBannerClick(position: Int) {
        val intent = Intent(context, MyWebActivity::class.java)
        intent.putExtra(Intent_Url, banners[position].url)
        intent.putExtra(Intent_Title, "")
        startActivity(intent)

    }

    override fun onClick(p0: View?) {
        when (p0) {
            notice-> startActivity(Intent(context,NewsActivity::class.java))
            bt_tab_1-> startActivity(Intent(context,TabActivity::class.java).putExtra(Intent_Tab,"1"))
            bt_tab_2-> startActivity(Intent(context,TabActivity::class.java).putExtra(Intent_Tab,"2"))
            bt_tab_3-> startActivity(Intent(context,TabActivity::class.java).putExtra(Intent_Tab,"3"))
            bt_tab_4-> startActivity(Intent(context,TabActivity::class.java).putExtra(Intent_Tab,"4"))
        }
    }


    override fun onRefresh(refreshLayout: RefreshLayout?) {

        initData()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden && headerView.marqueeView != null)  headerView.marqueeView .stopFlipping() else headerView.marqueeView .startFlipping()
    }
    companion object {
        fun newInstance(): MainFragment {
            val bundle = Bundle()
            val fragment = MainFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}