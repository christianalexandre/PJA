package com.example.todo.modules.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainViewPagerAdapter(fragmentActivity: FragmentActivity, fragmentList: List<Fragment?>) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = fragmentList

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position] ?: Fragment()

}