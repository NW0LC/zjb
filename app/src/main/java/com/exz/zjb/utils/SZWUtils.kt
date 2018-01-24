package com.exz.zjb.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.NestedScrollView
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.exz.zjb.R
import com.exz.zjb.bean.ListFilterBean
import com.exz.zjb.module.LoginActivity
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener
import com.szw.framelibrary.app.MyApplication
import com.szw.framelibrary.config.Constants.Result.Intent_ClassName

/**
 * Created by 史忠文
 * on 2017/10/17.
 */
object SZWUtils {
    /**
     * @param phoneNum 电话号码
     * @return 有隐藏中间
     */
    fun hideMidPhone(phoneNum: String): String {

        return if (TextUtils.isEmpty(phoneNum))
            "暂无电话"
        else if (phoneNum.length != 11)
            phoneNum
        else
            phoneNum.substring(0, 3) + "****" + phoneNum.substring(phoneNum.length - 4, phoneNum.length)
    }

    /**
     * @param mContext 上下文
     * @param intent   事件
     * @return true登录
     */
    fun checkLogin(mContext: Fragment, intent: Intent = Intent(), clazzName: String = ""): Boolean {
        return if (!MyApplication.checkUserLogin()) {
            val login = Intent(mContext.context, LoginActivity::class.java)
            if (clazzName.isNotEmpty()) {
                login.putExtra(Intent_ClassName, clazzName)
            }
            login.putExtras(intent)
            mContext.startActivityForResult(login, 0xc8)
            mContext.activity?.overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out)
            false
        } else {
            try {
                mContext.startActivityForResult(intent, 0xc8)
            } catch (e: Exception) {
            }
            true
        }
    }
    /**
     * @param mContext 上下文
     * @param intent   事件
     * @return true登录
     */
    fun checkLogin(mContext: Activity, intent: Intent = Intent(), clazzName: String = ""): Boolean {
        return if (!MyApplication.checkUserLogin()) {
            val login = Intent(mContext, LoginActivity::class.java)
            if (clazzName.isNotEmpty()) {
                login.putExtra(Intent_ClassName, clazzName)
            }
            login.putExtras(intent)
            mContext.startActivityForResult(login, 0xc8)
            mContext.overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out)
            false
        } else {
            try {
                mContext.startActivityForResult(intent, 0xc8)
            } catch (e: Exception) {
            }
            true
        }
    }


    fun matcherSearchTitle(textView: TextView, textStart: String, start: Int, end: Int, color: Int) {
        var builder = SpannableStringBuilder(textStart)
        var span = ForegroundColorSpan(color)
        builder.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.setText(builder)
    }

    /**
     * string base64
     *  base64 转bitmap
     */
    fun stringtoBitmap(string: String): Bitmap? {
        //将字符串转换成Bitmap类型
        var bitmap: Bitmap? = null
        try {
            val bitmapArray: ByteArray
            bitmapArray = Base64.decode(string, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return bitmap
    }




    /**
     * 增加固定外边距
     */
    fun setMargin(view: View, size: Float) {
        val lp = view.layoutParams
        if (lp is ViewGroup.MarginLayoutParams) {
            lp.topMargin += SizeUtils.dp2px(size)
        }

        view.layoutParams = lp

    }

    /**
     * 增加固定内边距
     */
    fun setPaddingSmart(view: View, size: Float) {
        val lp = view.layoutParams
        if (lp != null && lp.height > 0) {
            lp.height += SizeUtils.dp2px(size)
        }
        view.setPadding(view.paddingLeft, view.paddingTop + SizeUtils.dp2px(size), view.paddingRight, view.paddingBottom)

    }

    /**
     * 减少固定外边距
     */
    fun minusMargin(view: View, size: Float) {
        val lp = view.layoutParams
        if (lp is ViewGroup.MarginLayoutParams) {
            lp.topMargin -= SizeUtils.dp2px(size)
        }

        view.layoutParams = lp

    }

    /**
     * 减少固定内边距
     */
    fun minusPaddingSmart(view: View, size: Float) {
        val lp = view.layoutParams
        if (lp != null && lp.height > 0) {
            lp.height -= SizeUtils.dp2px(size)
        }
        view.setPadding(view.paddingLeft, view.paddingTop - SizeUtils.dp2px(size), view.paddingRight, view.paddingBottom)

    }

    /**
     * 设置灰色还是yellow 箭头
     *
     * @param b true grey  ; false yellow
     */
    fun setGreyOrYellow(context: Context?, view: RadioButton, b: Boolean) {
        if (context!=null)
        if (b) {
            view.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.drawable.selector_tab_triangle_grey), null)
            view.setTextColor(ContextCompat.getColor(context, R.color.MaterialGrey600))
        } else {
            view.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context, R.drawable.selector_tab_triangle_yellow), null)
            view.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        }
    }

    /**
     * 底部button布局滑动从底部,出现or隐藏
     */
    class MyNestedScrollListener(private var bottom_bar: View, private var h: Int) : NestedScrollView.OnScrollChangeListener {

        private var differY = 0f
        private var bottomBarY = (ScreenUtils.getScreenHeight() - h).toFloat()
        override fun onScrollChange(v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
            val differ = scrollY - oldScrollY
            differY = if ((differY + differ) in 0..h) {
                differY + differ
            } else {
                when {differY + differ > h -> h.toFloat()
                    differY + differ < 0 -> 0f
                    else -> 0f
                }
            }
            val nextY = bottomBarY + differY
            val y = ObjectAnimator.ofFloat(bottom_bar, "y", bottom_bar.y, nextY)
            val animatorSet = AnimatorSet()
            animatorSet.play(y)
            animatorSet.duration = 0
            animatorSet.start()

        }

    }

    /**
     * 设置刷新 及控制 刷新头 的显示和隐藏
     *
     */
    fun setRefreshAndHeaderCtrl(listener: OnRefreshListener, header: View, refreshLayout: SmartRefreshLayout) {

        refreshLayout.setOnMultiPurposeListener(object : SimpleMultiPurposeListener() {
            override fun onHeaderPulling(headerView: RefreshHeader?, percent: Float, offset: Int, bottomHeight: Int, extendHeight: Int) {
                if (offset == 0)
                    header.visibility = View.GONE
                else
                    header.visibility = View.VISIBLE
            }

            override fun onHeaderReleasing(headerView: RefreshHeader?, percent: Float, offset: Int, footerHeight: Int, extendHeight: Int) {
                if (offset == 0)
                    header.visibility = View.GONE
            }
        })
        refreshLayout.setOnRefreshListener(listener)
    }
    /**
     * 将个人中心 数字单位换成黑色
     */
    fun setUnitTextColor(context: Context,msg:String):SpannableString{
        val msp = SpannableString(msg)
        msp.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.MaterialGrey600)), msg.length-1, msg.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return msp
    }

    /**
     * 获取日期排序
     */
    fun getTimeSortData(): ArrayList<ListFilterBean> {
        val filterBeans = ArrayList<ListFilterBean>()
        filterBeans.add(ListFilterBean("1", "默认"))
        filterBeans.add(ListFilterBean("2", "截止日期由远到近"))
        filterBeans.add(ListFilterBean("3", "截止日期由近到远"))
        return filterBeans
    }
    /**
     * 获取年排序
     */
    fun getYearSortData(): ArrayList<ListFilterBean> {
        val filterBeans = ArrayList<ListFilterBean>()
        filterBeans.add(ListFilterBean("1", "不限"))
        filterBeans.add(ListFilterBean("2", "一年以下"))
        filterBeans.add(ListFilterBean("3", "1(含)-2年"))
        filterBeans.add(ListFilterBean("4", "2(含)-3年"))
        filterBeans.add(ListFilterBean("5", "3年以上"))
        return filterBeans
    }
}