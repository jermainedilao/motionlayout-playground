package com.jermaine.gigglemotionlayout

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import androidx.viewpager.widget.ViewPager

/**
 * Custom view pager that allows enabling swipe action.
 *
 * https://stackoverflow.com/a/56597897/5285687
 */

open class SlowScrollViewPager : ViewPager {

    var pagingEnabled = true
    private val scroller: FixedSpeedScroller
    private var touchListener: (action: Int) -> Unit = {}

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context) : super(context)

    init {
        // Override scroll speed of viewpager.
        // https://stackoverflow.com/a/28297483/5285687
        val viewPager = ViewPager::class.java
        val scroller = viewPager.getDeclaredField("mScroller")
        scroller.isAccessible = true
        this.scroller = FixedSpeedScroller(context, getInterpolator())
        scroller.set(this, this.scroller)
    }

    open fun getInterpolator(): Interpolator = AccelerateDecelerateInterpolator()

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        touchListener.invoke(ev.action)
        return if (pagingEnabled) {
            super.onTouchEvent(ev)
        } else {
            false
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        touchListener.invoke(ev.action)
        return if (pagingEnabled) {
            super.onInterceptTouchEvent(ev)
        } else {
            false
        }
    }

    fun setScrollDuration(duration: Int) {
        scroller.setScrollDuration(duration)
    }

    fun addTouchListener(listener: (action: Int) -> Unit) {
        this.touchListener = listener
    }
}