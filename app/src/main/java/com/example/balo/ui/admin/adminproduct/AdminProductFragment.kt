package com.example.balo.ui.admin.adminproduct

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.balo.databinding.FragmentAdminProductBinding
import com.example.balo.ui.admin.adminbrand.AdminBrandActivity
import com.example.balo.ui.admin.adminbrand.AllBrandActivity
import com.example.balo.ui.admin.balo.AdminProductActivity
import com.example.balo.ui.admin.balo.AllProductActivity
import com.example.balo.ui.base.BaseFragment

class AdminProductFragment : BaseFragment<FragmentAdminProductBinding>() {
    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() = binding.run {
        imgAddBrand.setOnClickListener { goToAct(AdminBrandActivity()) }
        imgAddProduct.setOnClickListener { goToAct(AdminProductActivity()) }
        tvSeeProduct.setOnClickListener { goToAct(AllProductActivity()) }
        tvSeeBrand.setOnClickListener { goToAct(AllBrandActivity()) }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdminProductBinding = FragmentAdminProductBinding.inflate(inflater)

    private fun goToAct(act: FragmentActivity) {
        startActivity(Intent(context, act::class.java))
    }
}