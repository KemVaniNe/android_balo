package com.example.balo.ui.admin.adminproduct

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.R
import com.example.balo.data.model.BrandEntity
import com.example.balo.databinding.FragmentAdminProductBinding
import com.example.balo.ui.admin.adminbrand.AdminBrandActivity
import com.example.balo.ui.admin.adminbrand.AllBrandActivity
import com.example.balo.ui.admin.balo.AdminProductActivity
import com.example.balo.ui.admin.balo.AllProductActivity
import com.example.balo.ui.base.BaseFragment
import com.example.balo.ui.share.BrandAdapter
import com.example.balo.utils.Utils
import com.google.gson.Gson

class AdminProductFragment : BaseFragment<FragmentAdminProductBinding>() {

    private lateinit var dialog: AlertDialog

    private lateinit var viewModel: AdminProductVM

    private val brands = mutableListOf<BrandEntity>()

    private val brandAdapter by lazy {
        BrandAdapter(brands) { pos ->
            startActivityForResult(
                context?.let {
                    AdminBrandActivity.newIntent(it, brands[pos].id)
                }, REQUEST_CODE_ALL_BRAND
            )
        }
    }

    companion object {

        const val REQUEST_CODE_ALL_BRAND = 151

        const val REQUEST_CODE_ADD_BRAND = 152
    }

    override fun initView() = binding.run {
        context?.let {
            dialog = Utils.showProgressDialog(it)
        }
        viewModel.getAllBrands(
            handleFail = { error ->
                if (dialog.isShowing) dialog.dismiss()
                toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
            })
        rvBrand.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvBrand.adapter = brandAdapter
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[AdminProductVM::class.java]
        listenData()
    }

    override fun initListener() = binding.run {
        imgAddBrand.setOnClickListener { handleAddBrand() }
        imgAddProduct.setOnClickListener { goToAct(AdminProductActivity()) }
        tvSeeProduct.setOnClickListener { goToAct(AllProductActivity()) }
        tvSeeBrand.setOnClickListener {
            context?.let {
                startActivityForResult(AllBrandActivity.newIntent(it), REQUEST_CODE_ALL_BRAND)
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdminProductBinding = FragmentAdminProductBinding.inflate(inflater)

    private fun updateBrands() {
        viewModel.getAllBrands(
            handleFail = { error ->
                if (dialog.isShowing) dialog.dismiss()
                toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
            })
    }

    private fun handleAddBrand() {
        context?.let {
            startActivityForResult(
                AdminBrandActivity.newIntent(it, AdminBrandActivity.KEY_ADD), REQUEST_CODE_ADD_BRAND
            )
        }
    }

    private fun goToAct(act: FragmentActivity) {
        startActivity(Intent(context, act::class.java))
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADD_BRAND || requestCode == REQUEST_CODE_ALL_BRAND) {
                updateBrands()
            }
        }
    }
}