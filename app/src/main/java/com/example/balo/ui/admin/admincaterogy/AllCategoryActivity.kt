package com.example.balo.ui.admin.admincaterogy

import android.content.Intent
import android.view.LayoutInflater
import com.example.balo.databinding.ActivityAllCategoryBinding
import com.example.balo.ui.base.BaseActivity

class AllCategoryActivity : BaseActivity<ActivityAllCategoryBinding>() {
    override fun viewBinding(inflate: LayoutInflater): ActivityAllCategoryBinding =
        ActivityAllCategoryBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        imgAddBrand.setOnClickListener {
            startActivity(Intent(this@AllCategoryActivity, AdminCategoryActivity::class.java))
        }
    }

}