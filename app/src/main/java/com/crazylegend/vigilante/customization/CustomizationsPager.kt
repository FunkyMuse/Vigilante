package com.crazylegend.vigilante.customization

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by crazy on 11/8/20 to long live and prosper !
 */
class CustomizationsPager(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                Fragment()
            }
            1 -> {
                Fragment()
            }
            else -> throw IllegalStateException()
        }
    }
}