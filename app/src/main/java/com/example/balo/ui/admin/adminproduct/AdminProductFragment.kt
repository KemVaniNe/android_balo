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
import com.example.balo.ui.admin.managerbrand.AdminBrandActivity
import com.example.balo.ui.admin.managerbrand.AllBrandActivity
import com.example.balo.ui.admin.managerproduct.detail.AdminDetailProductActivity
import com.example.balo.ui.admin.managerproduct.ManagerProductActivity
import com.example.balo.ui.base.BaseFragment
import com.example.balo.adapter.brand.BrandAdapter
import com.example.balo.adapter.product.AdminProductAdapter
import com.example.balo.data.model.BaloEntity
import com.example.balo.utils.Utils

class AdminProductFragment : BaseFragment<FragmentAdminProductBinding>() {

    private lateinit var dialog: AlertDialog

    private lateinit var viewModel: AdminProductVM

    private val brands = mutableListOf<BrandEntity>()

    private val products = mutableListOf<BaloEntity>()

    private val brandAdapter by lazy {
        BrandAdapter(brands) { pos ->
            startActivityForResult(
                context?.let {
                    AdminBrandActivity.newIntent(it, brands[pos].id)
                }, REQUEST_BRAND
            )
        }
    }

    private val productAdapter by lazy {
        AdminProductAdapter(products) { pos ->
            startActivityForResult(
                context?.let {
                    AdminDetailProductActivity.newIntent(it, products[pos].id)
                }, REQUEST_PRODUCT
            )
        }
    }

    companion object {
        const val REQUEST_BRAND = 151

        const val REQUEST_PRODUCT = 152
    }

    override fun initView() = binding.run {
        context?.let {
            dialog = Utils.showProgressDialog(it)
        }
        updateProduct()
        updateBrands()
        rvBrand.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvBrand.adapter = brandAdapter
        rvProduct.layoutManager = LinearLayoutManager(context)
        rvProduct.adapter = productAdapter
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[AdminProductVM::class.java]
        listenData()
    }

    override fun initListener() = binding.run {
        imgAddBrand.setOnClickListener { handleAddBrand() }
        imgAddProduct.setOnClickListener { handleAddProduct() }
        tvSeeProduct.setOnClickListener {
            context?.let {
                startActivityForResult(ManagerProductActivity.newIntent(it), REQUEST_PRODUCT)
            }
        }
        tvSeeBrand.setOnClickListener {
            context?.let {
                startActivityForResult(AllBrandActivity.newIntent(it), REQUEST_BRAND)
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdminProductBinding = FragmentAdminProductBinding.inflate(inflater)

    private fun updateBrands() {
        if (!dialog.isShowing) dialog.show()
        viewModel.getAllBrands(
            handleFail = { error ->
                if (dialog.isShowing) dialog.dismiss()
                toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
            })
    }

    private fun updateProduct() {
        if (!dialog.isShowing) dialog.show()
        viewModel.getAllProducts(
            handleFail = { error ->
                if (dialog.isShowing) dialog.dismiss()
                toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
            })
    }

    private fun handleAddBrand() {
        context?.let {
            startActivityForResult(
                AdminBrandActivity.newIntent(it, AdminBrandActivity.KEY_ADD), REQUEST_BRAND
            )
        }
    }

    private fun handleAddProduct() {
        context?.let {
            startActivityForResult(
                AdminDetailProductActivity.newIntent(it, AdminDetailProductActivity.KEY_ADD),
                REQUEST_PRODUCT
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

        viewModel.products.observe(this) {
            if (it != null) {
                products.run {
                    clear()
                    addAll(it)
                }
                productAdapter.notifyDataSetChanged()
                if (dialog.isShowing) dialog.dismiss()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_BRAND) {
                updateBrands()
            } else if (requestCode == REQUEST_PRODUCT) {
                updateProduct()
            }
        }
    }
}