package com.jermaine.gigglemotionlayout

import android.view.View
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager

/**
 * ViewPager page transformer to create parallax effect.
 *
 * https://stackoverflow.com/a/39538738/5285687
 */

class ParallaxPageTransformer : ViewPager.PageTransformer {
    companion object {
        private const val TAG = "ParallaxPageTransformer"
    }

    override fun transformPage(page: View, position: Float) {
        // -1 is the position when the screen is at the left most part of the screen (not visible anymore).
        // 1 is the position when the screen is at the right most part of the screen (not visible anymore).
        // Values between them means the page is still visible on the device screen.
        if (position >= -1 && position <= 1) {
            // Set translation of image slower than the normal swipe speed.
            page.findViewById<ImageView>(R.id.imageView)
                .translationX = ((-position * page.width) / 2)
        } else {
            page.alpha = 1f
        }
    }
}