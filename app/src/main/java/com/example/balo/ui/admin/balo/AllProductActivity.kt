package com.example.balo.ui.admin.balo

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import com.example.balo.databinding.ActivityAllProductBinding
import com.example.balo.ui.base.BaseActivity

class AllProductActivity : BaseActivity<ActivityAllProductBinding>() {

    companion object {

        const val REQUEST_CODE_ADD = 160
        fun newIntent(context: Context): Intent {
            return Intent(context, AllProductActivity::class.java)
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityAllProductBinding =
        ActivityAllProductBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        imgAdd.setOnClickListener { handleAdd() }
    }

    private fun handleAdd() {
        startActivityForResult(
            AdminProductActivity.newIntent(this, AdminProductActivity.KEY_ADD), REQUEST_CODE_ADD
        )
    }

}