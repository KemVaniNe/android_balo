package com.example.balo.admin.managerbrand.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.adapter.product.AdminMapsProductAdapter
import com.example.balo.admin.adminhome.AdminHomeFragment
import com.example.balo.admin.managerbrand.ManagerBrandVM
import com.example.balo.admin.managerproduct.detailproduct.AdminProductDetailActivity
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.BrandEntity
import com.example.balo.databinding.ActivityAdminBrandDetailBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Utils

class AdminBrandDetailActivity : BaseActivity<ActivityAdminBrandDetailBinding>() {

    private lateinit var viewModel: ManagerBrandVM

    private var currentBrand: BrandEntity? = null

    private val products = mutableListOf<BaloEntity>()

    private var id = ""

    private val productAdapter by lazy {
        AdminMapsProductAdapter(products) {
            goToDetail(products[it].id)
        }
    }

    companion object {

        const val ID = "id"
        const val REQUEST_CODE_CHANGE = 123
        fun newIntent(context: Context, id: String): Intent {
            return Intent(context, AdminBrandDetailActivity::class.java).apply {
                putExtra(ID, id)
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityAdminBrandDetailBinding =
        ActivityAdminBrandDetailBinding.inflate(inflate)

    override fun initView() = binding.rvProduct.run {
        layoutManager = LinearLayoutManager(this@AdminBrandDetailActivity)
        adapter = productAdapter
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[ManagerBrandVM::class.java]
        listenVM()
        val intent = intent
        if (intent.hasExtra(ID) && intent.getStringExtra(ID) != null) {
            id = intent.getStringExtra(ID)!!
            getBrand()
        } else {
            finish()
        }
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        btnEdit.setOnClickListener { goToDetailBrand() }
    }

    private fun goToDetail(id: String) {
        startActivityForResult(AdminProductDetailActivity.newIntent(this, id), REQUEST_CODE_CHANGE)
    }

    private fun goToDetailBrand() {
        if (binding.clLoading.visibility == View.GONE) {
            startActivityForResult(
                AdminBrandEditActivity.newIntent(this, currentBrand!!.id),
                REQUEST_CODE_CHANGE
            )
        }
    }

    private fun getBrand() {
        binding.clLoading.visibility = View.VISIBLE
        viewModel.getBrandById(id) {
            toastDialog(it)
            finish()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listenVM() {
        viewModel.currentBrand.observe(this) {
            if (it != null) {
                binding.clLoading.visibility = View.GONE
                currentBrand = it
                getProductsBaseId()
                setView(it)
            }
        }

        viewModel.products.observe(this) {
            if (it != null) {
                binding.clLoading.visibility = View.GONE
                products.run {
                    clear()
                    addAll(it)
                }
                productAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun goneProduct() = binding.run {
        rvProduct.visibility = View.GONE
        tvRateTitle.visibility = View.GONE
    }

    private fun getProductsBaseId() {
        binding.clLoading.visibility = View.VISIBLE
        viewModel.getProductsBaseBrand(id = currentBrand!!.id) {
            toastDialog(it)
            goneProduct()
        }
    }

    private fun setView(item: BrandEntity) = binding.run {
        Utils.displayBase64Image(item.pic, imgPic)
        tvName.text = item.name
        tvValueDes.text = item.des
        btnEdit.visibility = View.VISIBLE
    }

    private fun toastDialog(notification: String) {
        toast(notification)
        binding.clLoading.visibility = View.GONE
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AdminHomeFragment.REQUEST_CODE_CHANGE && resultCode == Activity.RESULT_OK) {
            setResult(RESULT_OK)
            getBrand()
        }
    }
}