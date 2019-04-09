package com.el.ariby.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 스와이프를 방지하기 위한 커스텀 뷰 페이저
 */
public class CustomViewPager extends ViewPager {

    private boolean enabled = false;

    public CustomViewPager(@NonNull Context context) {
        super(context);
    }

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.enabled && super.onTouchEvent(event) && performClick();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.enabled && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return this.enabled && super.performClick();
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
