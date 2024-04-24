package com.example.balo.ui.admin.adminproduct

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.balo.databinding.FragmentAdminProductBinding
import com.example.balo.ui.admin.admincaterogy.AdminCategoryActivity
import com.example.balo.ui.base.BaseFragment

class AdminProductFragment : BaseFragment<FragmentAdminProductBinding>() {
    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() = binding.run {
        imgAddBrand.setOnClickListener {
            startActivity(Intent(context, AdminProductFragment::class.java))
        }
        imgAddProduct.setOnClickListener {
            //   startActivity(Intent(context, AdminProductFragment::class.java))
        }
        imgAddCategory.setOnClickListener {
            startActivity(Intent(context, AdminCategoryActivity::class.java))
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdminProductBinding = FragmentAdminProductBinding.inflate(inflater)

}