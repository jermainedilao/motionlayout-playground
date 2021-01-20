package com.jermaine.gigglemotionlayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> WalkthroughFragment1()
            1 -> WalkthroughFragment2()
            2 -> WalkthroughFragment3()
            else -> {
                throw IllegalArgumentException("Invalid position!")
            }
        }
    }
}