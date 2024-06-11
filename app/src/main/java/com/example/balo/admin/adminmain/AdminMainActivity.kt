package com.example.balo.admin.adminmain

import android.view.LayoutInflater
import com.example.balo.R
import com.example.balo.adapter.viewpager.AdminViewPagerAdapter
import com.example.balo.databinding.ActivityAdminBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Constants

class AdminMainActivity : BaseActivity<ActivityAdminBinding>() {

    private val viewPagerAdapter by lazy { AdminViewPagerAdapter(supportFragmentManager) }

    override fun viewBinding(inflate: LayoutInflater): ActivityAdminBinding =
        ActivityAdminBinding.inflate(inflate)

    override fun initView() {
        binding.viewPager.run {
            offscreenPageLimit = 2
            adapter = viewPagerAdapter
        }
    }

    override fun initData() {
    }

    override fun initListener() {
        binding.run {
            bottomNav.setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.admin_home -> {
                        changeCurrentFragment(Constants.ADMIN_HOME)
                    }

                    R.id.admin_account -> {
                        changeCurrentFragment(Constants.ADMIN_ACCOUNT)
                    }

                    R.id.admin_notification -> {
                        changeCurrentFragment(Constants.ADMIN_NOTIFICATION)
                    }

                    else -> false
                }
            }
        }
    }

    private fun changeCurrentFragment(type: Int): Boolean {
        binding.viewPager.setCurrentItem(type, true)
        return true
    }

    fun finishAct() {
        finish()
    }
}