package com.example.balo.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.balo.admin.adminacount.AdminAccountFragment
import com.example.balo.admin.adminhome.AdminHomeFragment
import com.example.balo.admin.adminbill.AdminBillFragment
import com.example.balo.admin.adminproduct.AdminProductFragment
import com.example.balo.utils.Constants

class AdminViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int = Constants.ADMIN_MAIN_NUMBER_FRAGMENT

    override fun getItem(position: Int): Fragment {
        return when (position) {
            Constants.ADMIN_HOME -> AdminHomeFragment()
            Constants.ADMIN_ORDER -> AdminBillFragment()
            Constants.ADMIN_ACCOUNT -> AdminAccountFragment()
            else -> AdminHomeFragment()
        }
    }
}