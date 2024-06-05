package com.example.balo.admin.adminproduct

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.R
import com.example.balo.data.model.BrandEntity
import com.example.balo.databinding.FragmentAdminProductBinding
import com.example.balo.admin.managerbrand.AdminBrandDetailActivity
import com.example.balo.admin.managerbrand.ManagerBrandActivity
import com.example.balo.admin.managerproduct.ManagerProductActivity
import com.example.balo.shareview.base.BaseFragment
import com.example.balo.adapter.brand.BrandAdapter
import com.example.balo.adapter.product.AdminProductAdapter
import com.example.balo.admin.managerproduct.detailproduct.AdminProductDetailActivity
import com.example.balo.data.model.BaloEntity

class AdminProductFragment : BaseFragment<FragmentAdminProductBinding>() {
    private lateinit var viewModel: AdminProductVM

    private val brands = mutableListOf<BrandEntity>()

    private val products = mutableListOf<BaloEntity>()

    private val brandAdapter by lazy {
        BrandAdapter(brands) { pos ->
            startActivityForResult(
                context?.let {
                    AdminBrandDetailActivity.newIntent(it, brands[pos].id)
                }, REQUEST_BRAND
            )
        }
    }

    private val productAdapter by lazy {
        AdminProductAdapter(products) { pos ->
            startActivityForResult(
                context?.let {
                    AdminProductDetailActivity.newIntent(it, products[pos].id)
                }, REQUEST_PRODUCT
            )
        }
    }

    companion object {
        const val REQUEST_BRAND = 151

        const val REQUEST_PRODUCT = 152
    }

    override fun initView() = binding.run {
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
                startActivityForResult(ManagerBrandActivity.newIntent(it), REQUEST_BRAND)
            }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdminProductBinding = FragmentAdminProductBinding.inflate(inflater)

    private fun updateBrands() = binding.run {
        clLoadingBrand.visibility = View.VISIBLE
        viewModel.getAllBrands(
            handleFail = { error ->
                clLoadingBrand.visibility = View.GONE
                toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
            })
    }

    private fun updateProduct() = binding.run {
        clLoadingProduct.visibility = View.VISIBLE
        viewModel.getAllProducts(
            handleFail = { error ->
                clLoadingProduct.visibility = View.GONE
                toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
            })
    }

    private fun handleAddBrand() {
        context?.let {
            startActivityForResult(
                AdminBrandDetailActivity.newIntent(it, AdminBrandDetailActivity.KEY_ADD), REQUEST_BRAND
            )
        }
    }

    private fun handleAddProduct() {
        context?.let {
            startActivityForResult(
                AdminProductDetailActivity.newIntent(it, AdminProductDetailActivity.ID),
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
                binding.clLoadingBrand.visibility = View.GONE
            }
        }

        viewModel.products.observe(this) {
            if (it != null) {
                products.run {
                    clear()
                    addAll(it)
                }
                productAdapter.notifyDataSetChanged()
                binding.clLoadingProduct.visibility = View.GONE
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