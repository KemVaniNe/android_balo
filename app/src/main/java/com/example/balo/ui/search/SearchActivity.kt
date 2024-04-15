package com.example.balo.ui.search

import android.view.LayoutInflater
import com.example.balo.databinding.ActivitySearchBinding
import com.example.balo.ui.base.BaseActivity

class SearchActivity : BaseActivity<ActivitySearchBinding>() {

    override fun viewBinding(inflate: LayoutInflater): ActivitySearchBinding =
        ActivitySearchBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
    }

}