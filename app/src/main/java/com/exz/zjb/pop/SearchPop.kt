package com.exz.zjb.pop

import android.app.Activity
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.TextView
import com.exz.zjb.R
import kotlinx.android.synthetic.main.pop_search.view.*
import razerdp.basepopup.BasePopupWindow

/**
 * Created by pc on 2018/1/24.
 */

class SearchPop(context: Activity, private val listener: (str: String,title:String) -> Unit) : BasePopupWindow(context), View.OnClickListener {

    private lateinit var inflate: View

    init {
        popupWindow.isClippingEnabled = false
        isDismissWhenTouchOutside = true

    }


    override fun getClickToDismissView(): View = popupWindowView

    override fun onCreatePopupView(): View? {
        inflate = View.inflate(context, R.layout.pop_search, null)
        inflate.tab_1.setOnClickListener(this)
        inflate.tab_2.setOnClickListener(this)
        inflate.tab_3.setOnClickListener(this)
        inflate.tab_4.setOnClickListener(this)
        inflate.tab_5.setOnClickListener(this)
        inflate.tab_6.setOnClickListener(this)
        return inflate
    }

    override fun onClick(p0: View?) {
        listener.invoke(when (p0) {
            inflate.tab_1 -> {
                "31"
            }
            inflate.tab_2 -> {
                "32"
            }
            inflate.tab_3 -> {
                "11"
            }
            inflate.tab_4 -> {
                "12"
            }
            inflate.tab_5 -> {
                "21"
            }
            inflate.tab_6 -> {
                "22"
            }
            inflate.tab_7-> {
                "41"
            }
            inflate.tab_8 -> {
                "42"
            }
            else -> {
                "31"
            }
        },(p0 as TextView).text.toString())
        dismiss()
    }

    override fun initAnimaView(): View = findViewById(R.id.popup_animation)

    override fun initShowAnimation(): Animation {
        val scaleAnimation = ScaleAnimation(0.5f, 1f, 0.5f, 1f)
        scaleAnimation.duration = 200
        return scaleAnimation
    }

    override fun initExitAnimation(): Animation {
        val scaleAnimation = ScaleAnimation(1f, 0.5f, 1f, 0f)
        scaleAnimation.duration = 200
        return scaleAnimation
    }

}
