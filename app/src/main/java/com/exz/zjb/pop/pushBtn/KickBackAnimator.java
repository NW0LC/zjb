package com.exz.zjb.pop.pushBtn;

import android.animation.TypeEvaluator;

public class KickBackAnimator implements TypeEvaluator<Float> {
	private float mDuration = 0f;
	private float s = 1.70158f;
	public void setDuration(float duration) {
		mDuration = duration;
	}

	public Float evaluate(float fraction, Float startValue, Float endValue) {
		float t = mDuration * fraction;
		float b = startValue;
		float c = endValue - startValue;
		float d = mDuration;
		return calculate(t, b, c, d);
	}

	private Float calculate(float t, float b, float c, float d) {
		return c * ((t = t / d - 1) * t * ((s + 1) * t + s) + 1) + b;
	}
}