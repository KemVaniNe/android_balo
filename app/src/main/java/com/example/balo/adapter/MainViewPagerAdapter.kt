package com.example.balo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.balo.ui.user.account.AccountFragment
import com.example.balo.ui.user.favorite.FavoriteFragment
import com.example.balo.ui.user.home.HomeFragment
import com.example.balo.utils.Constants

class MainViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int = Constants.USER_MAIN_NUMBER_FRAGMENT

    override fun getItem(position: Int): Fragment {
        return when (position) {
            Constants.USER_HOME -> HomeFragment()
            Constants.USER_FAVORITE -> FavoriteFragment()
            Constants.USER_ACCOUNT -> AccountFragment()
            else -> HomeFragment()
        }
    }
}