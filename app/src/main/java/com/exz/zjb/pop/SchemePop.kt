package com.exz.zjb.pop

import android.app.Activity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.exz.zjb.R
import kotlinx.android.synthetic.main.pop_scheme.view.*
import razerdp.basepopup.BasePopupWindow

/**
 * Created by pc on 2018/1/24.
 */

class SchemePop(context: Activity) : BasePopupWindow(context) {
    private lateinit var inflate: View
    var data = ""
        set(value) {
            field = value
            inflate.tv_content.text = data
        }

    init {
        popupWindow.isClippingEnabled = false
        inflate.close.setOnClickListener { dismiss() }
    }


    override fun getClickToDismissView(): View = popupWindowView

    override fun onCreatePopupView(): View? {
        inflate = View.inflate(context, R.layout.pop_scheme, null)
        return inflate
    }

    override fun initAnimaView(): View = findViewById(R.id.mAnimation)

    override fun initShowAnimation(): Animation {
        val shakeAnimate = AnimationUtils.loadAnimation(context, R.anim.translate_show_start)
        return shakeAnimate
    }

}
