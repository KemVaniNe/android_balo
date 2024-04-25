package com.example.balo.ui.admin.balo

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.balo.R
import com.example.balo.databinding.ActivityAdminProductBinding
import com.example.balo.ui.base.BaseActivity

class AdminProductActivity : BaseActivity<ActivityAdminProductBinding>() {
    override fun viewBinding(inflate: LayoutInflater): ActivityAdminProductBinding =
        ActivityAdminProductBinding.inflate(inflate)

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