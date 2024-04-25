package com.example.balo.ui.admin.balo

import android.content.Intent
import android.view.LayoutInflater
import com.example.balo.databinding.ActivityAllProductBinding
import com.example.balo.ui.base.BaseActivity

class AllProductActivity : BaseActivity<ActivityAllProductBinding>() {
    override fun viewBinding(inflate: LayoutInflater): ActivityAllProductBinding =
        ActivityAllProductBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        imgAdd.setOnClickListener {
            startActivity(Intent(this@AllProductActivity, AdminProductActivity::class.java))
        }
    }

}