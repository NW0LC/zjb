package com.exz.zjb.utils

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import com.blankj.utilcode.util.KeyboardUtils
import com.common.controls.dialog.CommonDialogFactory
import com.common.controls.dialog.CoreDialog
import com.common.controls.dialog.DialogUtil
import com.common.controls.dialog.ICommonDialog
import com.exz.zjb.R
import kotlinx.android.synthetic.main.dialog_change_num.view.*
import kotlinx.android.synthetic.main.dialog_check_ing.view.*
import kotlinx.android.synthetic.main.dialog_check_unpass.view.*

/**
 * Created by 史忠文
 * on 2017/10/24.
 */
object DialogUtils {
    private lateinit var dialog: ICommonDialog
    /**
     * 清除提醒
     */
    fun delete(context: Context?, listener: () -> Unit) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_103)
        dialog.setTitleText("删除")
        dialog.setContentText("确定删除？")
        dialog.setCancelBtn("取消") { dialog.dismiss() }
        dialog.setOkBtn("确定") {
            dialog.dismiss()
            listener.invoke()
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }/**
     * 取消提醒
     */
    fun cancel(context: Context, listener: () -> Unit) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_103)
        dialog.setTitleText("取消")
        dialog.setContentText("确定取消？")
        dialog.setCancelBtn("取消") { dialog.dismiss() }
        dialog.setOkBtn("确定") {
            dialog.dismiss()
            listener.invoke()
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    /**
     *
     */
    fun updateApk(context: Context, title: String,isMust:String,listener: () -> Unit) {
        if(isMust == "0"){//可以忽略升级
            dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_103)
            dialog.setTitleText("发现新版本")
            dialog.setCancelBtn("取消",{v ->
                dialog.dismiss()
            })

        }else{ //必须升级
            dialog = CommonDialogFactory.createDialogByType(context, 4)
            dialog.setTitleText("发现新版本\n\n"+title)
            dialog.setCancelable(false)
        }

        dialog.setContentText(title)
        dialog.setOkBtn("更新") { v ->
            dialog.dismiss()
            listener.invoke()
        }
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    /**
     * 清除搜索记录
     */
    fun deleteSearch(context: Context, listener: View.OnClickListener) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_103)
        dialog.setTitleText("删除")
        dialog.setContentText("确定清除记录？")
        dialog.setCancelBtn("取消") { dialog.dismiss() }
        dialog.setOkBtn("确定") { v ->
            dialog.dismiss()
            listener.onClick(v)
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    /**
     * 支付返回
     */
    fun payBack(context: Activity) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_103)
        dialog.setTitleText("返回")
        dialog.setContentText("您确定放弃支付?")
        dialog.setCancelBtn("支付", { dialog.dismiss() })
        dialog.setOkBtn("确定", {
            dialog.dismiss()
            context.finish()
        })
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    /**
     * 余额支付 没支付密码
     */
    fun payNoPwd(context: Activity, listener: (v: View) -> Unit) {
        dialog = CommonDialogFactory.createDialogByType(context, DialogUtil.DIALOG_TYPE_103)
        dialog.setTitleText("啊哦")
        dialog.setContentText("未设置支付密码！")
        dialog.setCancelBtn("取消") { dialog.dismiss() }
        dialog.setOkBtn("去设置") { v ->
            dialog.dismiss()
            listener.invoke(v)
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

    /**
     * 数量更改弹窗
     */
    fun changeNum(context: Context, count: Long, listener: (num: Long) -> Unit) {
        dialog = DialogType104(context)
        val view = View.inflate(context, R.layout.dialog_change_num, null)
        ViewHolder(view)
        view.count.setText(String.format("%s", count))
        view.count.setSelection(view.count.text.length)
        countIndex = view.count.text.toString().toLong()
        dialog.setTitleText("修改数量")
        dialog.setContentView(view)
        dialog.setOkBtn("确定") {
            val trim = view.count.text.toString().trim()
            if (!TextUtils.isEmpty(trim)) {
                listener.invoke(trim.toLong())
            }
            dialog.dismiss()
            countIndex = 1
        }
        dialog.setCancelBtn("取消") {
            dialog.dismiss()
            countIndex = 1
        }
        dialog.setOnShowListener { KeyboardUtils.toggleSoftInput() }
        (dialog as DialogType104).setOnBeforeDismiss {
            KeyboardUtils.hideSoftInput(view.count)
            true
        }
        dialog.show()
    }
    private var countIndex = 1.toLong()

    internal class ViewHolder(private var view: View) : View.OnClickListener {
        init {
            view.count.setSelection(view.count.text.length)
            view.add.setOnClickListener(this)
            view.minus.setOnClickListener(this)
        }

        override fun onClick(p0: View) {
            when (p0.id) {
                R.id.minus -> countIndex = if (countIndex <= 1) 1 else --countIndex
                R.id.add -> countIndex += 1
            }
            view.count.setText(String.format("%s", countIndex))
            view.count.setSelection(view.count.text.length)
        }

    }









    /***
     *认证失败
     */
    fun checkUnpass(context: Context?,listener: () -> Unit) {

        val inflate = View.inflate(context, R.layout.dialog_check_unpass, null)
        val dlg = CoreDialog(context, com.common.alertpop.R.style.dialog, inflate, true)
        dlg.setPosition(Gravity.CENTER, 0, 0)
        dlg.setCanceledOnTouchOutside(true)
        inflate.bt_check_unpass_close.setOnClickListener {
            dlg.dismiss()
        }
        inflate.bt_check_unpass_confirm.setOnClickListener {
            listener.invoke()
            dlg.dismiss()
        }
        dlg.show()
    }


    /***
     *认证审核中
     */
    fun checking(context: Context?) {

        val inflate = View.inflate(context, R.layout.dialog_check_ing, null)
        val dlg = CoreDialog(context, com.common.alertpop.R.style.dialog, inflate, true)
        dlg.setPosition(Gravity.CENTER, 0, 0)
        dlg.setCanceledOnTouchOutside(true)
        inflate.bt_check_ing_close.setOnClickListener {
            dlg.dismiss()
        }
        dlg.show()
    }
    /***
     *未认证
     */
    fun unCheck(context: Context?,listener: () -> Unit) {

        val inflate = View.inflate(context, R.layout.dialog_check_uncheck, null)
        val dlg = CoreDialog(context, com.common.alertpop.R.style.dialog, inflate, true)
        dlg.setPosition(Gravity.CENTER, 0, 0)
        dlg.setCanceledOnTouchOutside(true)
        inflate.bt_check_unpass_close.setOnClickListener {
            dlg.dismiss()
        }
        inflate.bt_check_unpass_confirm.setOnClickListener {
            listener.invoke()
            dlg.dismiss()
        }
        dlg.show()
    }
}