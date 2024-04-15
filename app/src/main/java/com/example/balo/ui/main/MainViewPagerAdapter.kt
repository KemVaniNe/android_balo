package com.example.balo.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.balo.ui.account.AccountFragment
import com.example.balo.ui.favorite.FavoriteFragment
import com.example.balo.ui.home.HomeFragment

class MainViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int = 3

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> FavoriteFragment()
            2 -> AccountFragment()
            else -> HomeFragment()
        }
    }
}