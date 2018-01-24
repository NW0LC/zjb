package com.exz.zjb.pop.pushBtn

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import com.blankj.utilcode.util.ScreenUtils
import com.exz.zjb.R
import kotlinx.android.synthetic.main.pop_menu.view.*
import razerdp.basepopup.BasePopupWindow

class MenuPop(context: Activity?, private val listener: (v: View) -> Unit) : BasePopupWindow(context), View.OnClickListener {


    private var isCanClose = false
    private lateinit var mHandler: Handler

    init {
        isDismissWhenTouchOutside = true

    }

    override fun initShowAnimation(): Animation {
        val set = AnimationSet(false)
        val showAnimation = TranslateAnimation(0f, 0f, -ScreenUtils.getScreenHeight().toFloat(), 0f)
//        shakeAnimation.interpolator = CycleInterpolator(5f)//循环几次
        showAnimation.duration = 400
//        set.addAnimation(defaultAlphaAnimation)
        set.addAnimation(showAnimation)
        return defaultAlphaAnimation
    }

    override fun initExitAnimation(): Animation {
        return getDefaultAlphaAnimation(false)
    }

    private fun showAnimation(layout: ViewGroup) {
        val fadeAnim = ObjectAnimator.ofFloat(layout.center_music_window_close, "rotation", 0f, 410f)
        fadeAnim.duration = 300
        val kickAnimator = KickBackAnimator()
        kickAnimator.setDuration(150f)
        fadeAnim.setEvaluator(kickAnimator)
        fadeAnim.start()

        for (i in 0 until layout.childCount) {
            val child = layout.getChildAt(i)
            if (child.id == R.id.center_music_window_close) {
                continue
            }
            child.setOnClickListener(this)
            child.visibility = View.INVISIBLE
            mHandler.postDelayed({
                child.visibility = View.VISIBLE
                val fadeAnim = ObjectAnimator.ofFloat(child, "translationY", ScreenUtils.getScreenHeight().toFloat(), 0f)
                fadeAnim.duration = 300
                val kickAnimator = KickBackAnimator()
                kickAnimator.setDuration(150f)
                fadeAnim.setEvaluator(kickAnimator)
                fadeAnim.start()
            }, (i * 50).toLong())
            if (child.id == R.id.bt_tab1) {
                mHandler.postDelayed({ isCanClose = true }, ((layout.childCount - i) * 50).toLong())
            }
        }

    }

    fun closeAnimation(layout: ViewGroup) {
        for (i in 0 until layout.childCount) {
            val child = layout.getChildAt(i)
            if (child.id == R.id.center_music_window_close) {
                continue
            }
            child.setOnClickListener(this)
            mHandler.postDelayed({
                child.visibility = View.VISIBLE
                val fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 0f, ScreenUtils.getScreenHeight().toFloat())
                fadeAnim.duration = 200
                val kickAnimator = KickBackAnimator()
                kickAnimator.setDuration(100f)
                fadeAnim.setEvaluator(kickAnimator)
                fadeAnim.start()
                fadeAnim.addListener(object : Animator.AnimatorListener {

                    override fun onAnimationStart(animation: Animator) {

                    }

                    override fun onAnimationRepeat(animation: Animator) {
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        child.visibility = View.INVISIBLE
                    }

                    override fun onAnimationCancel(animation: Animator) {

                    }
                })
            }, ((layout.childCount - i - 1) * 30).toLong())

            if (child.id == R.id.bt_tab1) {

                mHandler.postDelayed({
                    isCanClose = false
                    dismiss()
                }, ((layout.childCount - i) * 30 + 80).toLong())
            }

        }
        val fadeAnim = ObjectAnimator.ofFloat(popupWindowView.center_music_window_close, "rotation", 410f, 0f)
        fadeAnim.duration = 300
        val kickAnimator = KickBackAnimator()
        kickAnimator.setDuration(150f)
        fadeAnim.setEvaluator(kickAnimator)
        fadeAnim.start()
    }

    override fun onClick(p0: View) {
        if (p0 != popupWindowView.blurView) {
            listener.invoke(p0)
        }
        if (isCanClose)
        closeAnimation(popupWindowView.viewGroup)
    }

    override fun getClickToDismissView(): View = popupWindowView

    override fun onCreatePopupView(): View {
        mHandler = Handler()
        val view = createPopupById(R.layout.pop_menu)
        view.blurView.setOnClickListener(this)
        showAnimation(view.viewGroup)
        return view
    }

    override fun initAnimaView(): View = findViewById(R.id.popup_animation)


}