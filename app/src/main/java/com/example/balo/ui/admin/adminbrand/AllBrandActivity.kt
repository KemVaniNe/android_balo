package com.example.balo.ui.admin.adminbrand

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.R
import com.example.balo.data.model.BrandEntity
import com.example.balo.databinding.ActivityAllBrandBinding
import com.example.balo.ui.base.BaseActivity
import com.example.balo.ui.share.BrandAdapter
import com.example.balo.utils.Utils
import com.google.gson.Gson

class AllBrandActivity : BaseActivity<ActivityAllBrandBinding>() {

    private lateinit var viewModel: AdminBrandVM

    private lateinit var dialog: AlertDialog

    private val brands = mutableListOf<BrandEntity>()

    private val brandAdapter by lazy {
        BrandAdapter(brands) { pos ->
            startActivity(
                AdminBrandActivity.newIntent(this@AllBrandActivity, Gson().toJson(brands[pos]))
            )
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityAllBrandBinding =
        ActivityAllBrandBinding.inflate(inflate)

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() = binding.run {
        if (!dialog.isShowing) dialog.show()
        viewModel.getAllBrands(
            handleFail = { error ->
                if (dialog.isShowing) dialog.dismiss()
                toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
            })
        rvBrand.layoutManager = LinearLayoutManager(this@AllBrandActivity)
        rvBrand.adapter = brandAdapter
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[AdminBrandVM::class.java]
        dialog = Utils.showProgressDialog(this@AllBrandActivity)
        listenData()
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        imgAdd.setOnClickListener {
            startActivity(
                AdminBrandActivity.newIntent(this@AllBrandActivity, AdminBrandActivity.KEY_ADD)
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listenData() {
        viewModel.brands.observe(this) {
            if (it != null) {
                brands.run {
                    clear()
                    addAll(it)
                }
                brandAdapter.notifyDataSetChanged()
                if (dialog.isShowing) dialog.dismiss()
            }
        }
    }

}