package com.example.balo.ui.admin.adminbrand

import android.view.LayoutInflater
import com.example.balo.databinding.ActivityAdminBrandBinding
import com.example.balo.ui.base.BaseActivity

class AdminBrandActivity : BaseActivity<ActivityAdminBrandBinding>() {
    override fun viewBinding(inflate: LayoutInflater): ActivityAdminBrandBinding =
        ActivityAdminBrandBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        btnAdd.setOnClickListener { handleAdd() }
        tvImport.setOnClickListener { handleImport() }
    }

    private fun handleAdd() {

    }

    private fun handleImport() {

    }
}