package com.example.balo.ui.main

import android.view.LayoutInflater
import com.example.balo.databinding.ActivityMainBinding
import com.example.balo.ui.base.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun viewBinding(inflate: LayoutInflater): ActivityMainBinding =
        ActivityMainBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() {
    }

}