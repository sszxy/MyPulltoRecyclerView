package com.example.myprac;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RefreshView extends LinearLayout {
    ImageView mImageView;
    Context mContext;
    ObjectAnimator animator;

    public RefreshView(Context context) {
        super(context);
        init(context);
    }

    public RefreshView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(Context context) {
        mContext = context;
        this.setGravity(VERTICAL);
        mImageView = new ImageView(mContext);
        mImageView.setImageResource(R.drawable.redflower);
        LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        addView(mImageView, params);
    }

    public void startAnimation() {
        Log.d("tag", "执行动画");
        animator = ObjectAnimator.ofFloat(mImageView, "rotation", 0, 359);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(10);
        animator.start();
    }

    public void stopAnimation() {
        animator.cancel();
    }

}
