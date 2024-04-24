package com.example.balo.ui.admin.adminbrand

import android.content.Intent
import android.view.LayoutInflater
import com.example.balo.databinding.ActivityAllBrandBinding
import com.example.balo.ui.base.BaseActivity

class AllBrandActivity : BaseActivity<ActivityAllBrandBinding>() {
    override fun viewBinding(inflate: LayoutInflater): ActivityAllBrandBinding =
        ActivityAllBrandBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        imgAdd.setOnClickListener {
            startActivity(Intent(this@AllBrandActivity, AdminBrandActivity::class.java))
        }
    }

}