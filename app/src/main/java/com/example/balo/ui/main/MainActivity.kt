package com.example.balo.ui.main

import android.view.LayoutInflater
import com.example.balo.databinding.ActivityMainBinding
import com.example.balo.ui.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewPagerAdapter by lazy { MainViewPagerAdapter(supportFragmentManager) }

    override fun viewBinding(inflate: LayoutInflater): ActivityMainBinding =
        ActivityMainBinding.inflate(inflate)

    override fun initView() {
        binding.viewPager.run {
            offscreenPageLimit = 3
            adapter = viewPagerAdapter
        }
    }

    override fun initData() {
    }

    override fun initListener() {
    }

}