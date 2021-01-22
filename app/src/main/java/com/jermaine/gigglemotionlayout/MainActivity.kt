package com.jermaine.gigglemotionlayout

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.jermaine.gigglemotionlayout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    private var isInitialAnimationDone = false
    private var previousPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupViewPager()
        setupMotionLayout()
    }

    private fun setupAutoScroll() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(
            object : Runnable {
                override fun run() {
                    Log.d(TAG, "run: RUN")
                    binding.viewPager.setCurrentItem(
                        when (binding.viewPager.currentItem) {
                            0 -> 1
                            1 -> 2
                            2 -> 0
                            else -> 0
                        },
                        true
                    )
                    handler.postDelayed(this, 3000)
                }
            },
            3000
        )
    }

    private fun setupMotionLayout() {
        binding.motionLayout.addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) = Unit

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) =
                Unit

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                Log.d(TAG, "onTransitionCompleted: RESET")
                if (p1 == R.id.initialEnd) {
                    p0?.setTransition(R.id.transitionSwipe)
                    p0?.progress = 0f
                    setupAutoScroll()
                    isInitialAnimationDone = true
                } else {
                    p0?.progress = 0f
                }
            }

            override fun onTransitionTrigger(
                p0: MotionLayout?,
                p1: Int,
                p2: Boolean,
                p3: Float
            ) = Unit
        })

        binding.motionLayoutDescription.addTransitionListener(object :
            MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) =
                Unit

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                Log.d(TAG, "onTransitionCompleted: RESET TEXT")
                p0?.progress = 0f
                binding.txtDescription1.text = getCurrentPageText(binding.viewPager.currentItem)
            }

            override fun onTransitionTrigger(
                p0: MotionLayout?,
                p1: Int,
                p2: Boolean,
                p3: Float
            ) = Unit
        })
    }

    private fun setupViewPager() {
        binding.viewPager.apply {
            setScrollDuration(800)
            setPageTransformer(false, ParallaxPageTransformer())
            adapter = ViewPagerAdapter(supportFragmentManager)
            offscreenPageLimit = 3

            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                // Reference: https://stackoverflow.com/a/34356483
                var sumPositionAndPositionOffset = 0f

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    if (isInitialAnimationDone) {
                        if (position + positionOffset > sumPositionAndPositionOffset) {
                            // Right to left.
                            // binding.motionLayoutDescription.progress = positionOffset
                        } else {
                            // Left to right
                            // binding.motionLayoutDescription.progress = 1.0f - positionOffset
                        }
                        binding.motionLayout.progress = positionOffset

                        sumPositionAndPositionOffset = position + positionOffset
                    }
                }

                override fun onPageSelected(position: Int) {
                    changeText(position)
                    changeIndicator(position)
                    binding.motionLayoutDescription.transitionToEnd()
                }

                override fun onPageScrollStateChanged(state: Int) = Unit
            })
        }

        binding.motionLayoutIndicator1.progress = 1f  // Selected by default.
    }

    private fun changeIndicator(position: Int) {
        val currentIndicator = getIndicatorForPosition(position)
        val previousIndicator = getIndicatorForPosition(previousPage)

        currentIndicator.transitionToEnd()
        previousIndicator.transitionToStart()

        previousPage = position
    }

    private fun getIndicatorForPosition(position: Int) = when (position) {
        0 -> binding.motionLayoutIndicator1
        1 -> binding.motionLayoutIndicator2
        2 -> binding.motionLayoutIndicator3
        else -> {
            throw IllegalArgumentException("Invalid position!")
        }
    }

    private fun changeText(position: Int) {
        val currentPageText = getCurrentPageText(position)
        binding.txtDescription2.text = currentPageText
    }

    private fun getCurrentPageText(position: Int) = when (position) {
        0 -> {
            getString(R.string.a_social_media_platform)
        }
        1 -> {
            getString(R.string.say_something_publicly)
        }
        2 -> {
            getString(R.string.connect_privately)
        }
        else -> {
            throw IllegalArgumentException("Invalid position!")
        }
    }
}