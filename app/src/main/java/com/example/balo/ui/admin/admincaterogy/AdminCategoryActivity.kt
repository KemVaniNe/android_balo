package com.example.balo.ui.admin.admincaterogy

import android.view.LayoutInflater
import com.example.balo.databinding.ActivityAdminCategoryBinding
import com.example.balo.ui.base.BaseActivity

class AdminCategoryActivity : BaseActivity<ActivityAdminCategoryBinding>() {
    override fun viewBinding(inflate: LayoutInflater): ActivityAdminCategoryBinding =
        ActivityAdminCategoryBinding.inflate(inflate)

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