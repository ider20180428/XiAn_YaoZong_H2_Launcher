package com.box.views;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
//动画效果
@SuppressLint("NewApi")
public class FlyImageView extends ImageView {
	ViewPropertyAnimator anim;
	int resource;
	int animDuration = 250;


	public FlyImageView(Context context) {
		super(context);
		this.anim = animate();
		if(Build.MODEL.equals("IDER_BBA41") || Build.MODEL.equals("IDER_BBA43")) {
			animDuration = 500;
		}
	}

	public FlyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.anim = animate();
		if(Build.MODEL.equals("IDER_BBA41") || Build.MODEL.equals("IDER_BBA43")) {
			animDuration = 500;
		}
	}

	public FlyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.anim = animate();
		if(Build.MODEL.equals("IDER_BBA41") || Build.MODEL.equals("IDER_BBA43")) {
			animDuration = 500;
		}
	}


	/**
	 * 平移动画
	 * @param width 目标宽度
	 * @param height 目标高度
	 * @param toX 目标x坐标
	 * @param toY 目标y坐标
	 * @param resource 目标图片资源
	 */
	public void flyTo(final int width, final int height, final int toX, final int toY, final int resource, final int changeValue) {
		ValueAnimator animator = ValueAnimator.ofInt(0, 100);
		final RelativeLayout.LayoutParams params = (LayoutParams) getLayoutParams();
		final int currentWidth = params.width;
		final int currentHeight = params.height;
		final int currentLeftMargin = params.leftMargin;
		final int currentTopMargin = params.topMargin;
		animator.addUpdateListener(new AnimatorUpdateListener() {
			// 设置一个估值计算器，稍后用
			private IntEvaluator mEvaluator = new IntEvaluator();
			@Override
			public void onAnimationUpdate(ValueAnimator animator) {

				int value = (Integer) animator.getAnimatedValue();
				float fraction = value / 100f;
				params.width = mEvaluator.evaluate(fraction, currentWidth, width);
				params.height = mEvaluator.evaluate(fraction, currentHeight, height);
				params.leftMargin = mEvaluator.evaluate(fraction, currentLeftMargin, toX);
				params.topMargin = mEvaluator.evaluate(fraction, currentTopMargin, toY);
				requestLayout();

				if(value >= changeValue) {
					setBackgroundResource(resource);
				}

			}
		});

		animator.setDuration(animDuration);
		animator.setInterpolator(new DecelerateInterpolator());
		animator.start();
	}

	public void flyTo(final int width, final int height, final int toX, final int toY) {
		ValueAnimator animator = ValueAnimator.ofInt(1, 100);
		final RelativeLayout.LayoutParams params = (LayoutParams) getLayoutParams();
		final int currentWidth = params.width;
		final int currentHeight = params.height;
		final int currentLeftMargin = params.leftMargin;
		final int currentTopMargin = params.topMargin;
		animator.addUpdateListener(new AnimatorUpdateListener() {
			// 设置一个估值计算器，稍后用
			private IntEvaluator mEvaluator = new IntEvaluator();
			@Override
			public void onAnimationUpdate(ValueAnimator animator) {

				int value = (Integer) animator.getAnimatedValue();
				float fraction = value / 100f;
				params.width = mEvaluator.evaluate(fraction, currentWidth, width);
				params.height = mEvaluator.evaluate(fraction, currentHeight, height);
				params.leftMargin = mEvaluator.evaluate(fraction, currentLeftMargin, toX);
				params.topMargin = mEvaluator.evaluate(fraction, currentTopMargin, toY);
				requestLayout();

			}
		});

		animator.setDuration(animDuration);
		animator.setInterpolator(new DecelerateInterpolator());
		animator.start();
	}

	public void hide() {
		setVisibility(View.INVISIBLE);
	}

	public boolean isHidden() {
		return getVisibility() == View.INVISIBLE || getVisibility() == View.GONE;
	}

	public void show() {
		setVisibility(View.VISIBLE);
	}

}
