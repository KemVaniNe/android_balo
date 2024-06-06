package com.example.balo.client.clientmain

import android.view.LayoutInflater
import com.example.balo.R
import com.example.balo.adapter.viewpager.ClientViewPagerAdapter
import com.example.balo.databinding.ActivityMainBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Constants

class ClientMainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewPagerAdapter by lazy { ClientViewPagerAdapter(supportFragmentManager) }

    override fun viewBinding(inflate: LayoutInflater): ActivityMainBinding =
        ActivityMainBinding.inflate(inflate)

    override fun initView() {
        binding.viewPager.run {
            offscreenPageLimit = 1
            adapter = viewPagerAdapter
        }
    }

    override fun initData() {
    }

    override fun initListener() {
        binding.run {
            bottomNav.setOnNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_home -> {
                        changeCurrentFragment(Constants.USER_HOME)
                    }

                    R.id.menu_account -> {
                        changeCurrentFragment(Constants.USER_ACCOUNT)
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