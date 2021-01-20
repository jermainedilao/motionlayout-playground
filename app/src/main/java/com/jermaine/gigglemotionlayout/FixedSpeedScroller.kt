package com.jermaine.gigglemotionlayout

import android.content.Context
import android.view.animation.Interpolator
import android.widget.Scroller

/**
 * Use this to override scroll speed of viewpager.
 *
 * https://stackoverflow.com/a/28297483/5285687
 */

class FixedSpeedScroller(context: Context, interpolator: Interpolator) : Scroller(context, interpolator) {

    /**
     * Scroll duration in milliseconds.
     */
    private var scrollDuration = 500

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int) {
        super.startScroll(startX, startY, dx, dy, scrollDuration)
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, scrollDuration)
    }

    fun setScrollDuration(duration: Int) {
        scrollDuration = duration
    }
}