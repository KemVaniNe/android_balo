package com.example.balo.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.balo.client.clientaccout.ClientAccountFragment
import com.example.balo.client.clienthome.ClientHomeFragment
import com.example.balo.client.clientnotification.ClientNotificationFragment
import com.example.balo.utils.Constants

class ClientViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int = Constants.USER_MAIN_NUMBER_FRAGMENT

    override fun getItem(position: Int): Fragment {
        return when (position) {
            Constants.USER_HOME -> ClientHomeFragment()
            Constants.USER_NOTIFICATION -> ClientNotificationFragment()
            Constants.USER_ACCOUNT -> ClientAccountFragment()
            else -> ClientHomeFragment()
        }
    }
}