package com.example.balo.ui.admin.mainadmin

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.balo.ui.admin.adminacount.AdminAccountFragment
import com.example.balo.ui.admin.adminhome.AdminHomeFragment
import com.example.balo.ui.admin.adminorder.AdminOrderFragment
import com.example.balo.ui.admin.adminproduct.AdminProductFragment
import com.example.balo.utils.Constants

class AdminViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int = Constants.ADMIN_MAIN_NUMBER_FRAGMENT

    override fun getItem(position: Int): Fragment {
        return when (position) {
            Constants.ADMIN_HOME -> AdminHomeFragment()
            Constants.ADMIN_ORDER -> AdminOrderFragment()
            Constants.ADMIN_PRODUCT -> AdminProductFragment()
            Constants.ADMIN_ACCOUNT -> AdminAccountFragment()
            else -> AdminHomeFragment()
        }
    }
}