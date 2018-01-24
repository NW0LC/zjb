package com.exz.zjb.module

import android.content.Context
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import com.exz.zjb.R
import com.exz.zjb.app.ToolApplication
import com.exz.zjb.bean.SearchBean
import com.exz.zjb.bean.SearchBean_
import com.exz.zjb.utils.DialogUtils
import com.exz.zjb.widget.TagAdapter
import com.exz.zjb.widget.TagFlowLayout
import com.szw.framelibrary.base.BaseActivity
import com.szw.framelibrary.utils.StatusBarUtil
import com.zhy.view.flowlayout.FlowLayout
import io.objectbox.Box
import kotlinx.android.synthetic.main.activity_search.*
import java.util.*

/**
 * Created by 史忠文
 * on 2017/6/5.
 */

class SearchActivity : BaseActivity(), View.OnClickListener {
    private lateinit var searchGoodsBeanBox: Box<SearchBean>
    override fun initToolbar(): Boolean {
        editText.setText(intent.getStringExtra(Intent_Search_Content))
        editText.setSelection(editText.text.length)

        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, lay_search)
        StatusBarUtil.setPaddingSmart(this, blurView)


        editText.postDelayed({val isShowSoft = intent.getBooleanExtra(Intent_isShowSoft, false)
            if (isShowSoft) {
                editText.isFocusable = true
                editText.isFocusableInTouchMode = true
                editText.requestFocus()
                val inputManager = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.showSoftInput(editText, 0)
            }},200)
        return false
    }
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideKeyboard(v, ev)) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                bt_cancel.isFocusable = true
                bt_cancel.isFocusableInTouchMode = true
                bt_cancel.requestFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    override fun setInflateId(): Int = R.layout.activity_search

    override fun init() {
        searchGoodsBeanBox = ToolApplication.getAPP(application).boxStore.boxFor(SearchBean::class.java)
        initHistoryTag(searchGoodsBeanBox.query().orderDesc(SearchBean_.date).build().find())
        editText.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // do something
                val searchContent = editText.text.toString().trim { it <= ' ' }
                if (!TextUtils.isEmpty(searchContent)) {
                    val searchGoodsBean = searchGoodsBeanBox.find(SearchBean_.searchContent, searchContent).firstOrNull()
                    searchGoodsBeanBox.put(if (searchGoodsBean == null) {
                        SearchBean(searchContent, Date())
                    } else {
                        searchGoodsBean.date = Date()
                        searchGoodsBean
                    })
                    search(searchContent)
                }
                return@OnEditorActionListener true
            }
            false
        })
        initEvent()
    }

    private fun initEvent() {
        bt_cancel.setOnClickListener(this)
        bt_delete.setOnClickListener(this)
    }

    override fun onClick(p0: View) {
        when (p0) {
            bt_cancel -> onBackPressed()
            bt_delete -> DialogUtils.deleteSearch(mContext, View.OnClickListener {
                searchGoodsBeanBox.removeAll()
                initHistoryTag(searchGoodsBeanBox.query().orderDesc(SearchBean_.date).build().find())
            })
        }
    }

    /**
     * @param list 初始化历史记录tag列表
     */
    private fun initHistoryTag(list: List<SearchBean>?) {
        if (list == null || list.isEmpty()) {
            historyLay.visibility = View.GONE
            return
        }
        mHistoryTagFlow.adapter = object : TagAdapter<SearchBean>(list) {
            override fun getView(parent: FlowLayout, position: Int, searchEntity: SearchBean): View {
                val layout = View.inflate(mContext, R.layout.tag_search, null) as RelativeLayout
                val textView = layout.getChildAt(0) as TextView
                textView.text = searchEntity.searchContent
                return layout
            }

        }
        mHistoryTagFlow.setOnTagClickListener(object : TagFlowLayout.OnTagClickListener {
            override fun onTagClick(view: View, position: Int, parent: FlowLayout): Boolean {
                val searchEntity = list[position]
                searchEntity.date = Date()
                searchGoodsBeanBox.put(searchEntity)
                search(list[position].searchContent)
                return false
            }

            override fun onTagLongClick(view: View, position: Int, parent: FlowLayout): Boolean {
                val layout = view as RelativeLayout
                val img = layout.getChildAt(1)
                img.visibility = View.VISIBLE
                img.setOnClickListener {
                    val searchEntity = list[position]
                    searchGoodsBeanBox.remove(searchEntity.id)
                    initHistoryTag(searchGoodsBeanBox.query().orderDesc(SearchBean_.date).build().find())
                }
                return true
            }
        })

    }

    /**
     * @param content 搜索内容
     */
    private fun search(content: String) {
//                val intent=Intent(this,SearchFilterActivity::class.java)
//                intent.putExtra(Intent_Search_Content,content )
//                startActivity(intent)
        finish()
    }

    companion object {
        val Intent_Search_Content = "Intent_Search_Content"
        val Intent_isShowSoft = "Intent_isShowSoft"
        // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
        fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
            if (v != null && (v is EditText)) {
                val l = intArrayOf(0, 0)
                v.getLocationInWindow(l)
                val left = l[0]
                val top = l[1]
                val bottom = top + v.getHeight()
                val right = left + v.getWidth()
                return !(event.x > left && event.x < right
                        && event.y > top && event.y < bottom)
            }
            return false
        }
    }

}
