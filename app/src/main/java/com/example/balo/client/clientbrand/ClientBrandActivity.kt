package com.example.balo.client.clientbrand

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.adapter.product.UserProductAdapter
import com.example.balo.admin.managerbrand.detail.AdminBrandDetailActivity
import com.example.balo.client.clientdetail.ClientDetailActivity
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.BrandEntity
import com.example.balo.databinding.ActivityClientBrandBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Utils

class ClientBrandActivity : BaseActivity<ActivityClientBrandBinding>() {

    private lateinit var viewModel: ClientBrandVM

    private var currentBrand: BrandEntity? = null

    private val products = mutableListOf<BaloEntity>()

    private var id = ""

    private val productAdapter by lazy {
        UserProductAdapter(products) {
            startActivity(ClientDetailActivity.newIntent(this, products[it].id))
        }
    }

    companion object {

        const val ID = "id"
        fun newIntent(context: Context, id: String): Intent {
            return Intent(context, ClientBrandActivity::class.java).apply {
                putExtra(ID, id)
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityClientBrandBinding =
        ActivityClientBrandBinding.inflate(inflate)

    override fun initView() = binding.rvProduct.run {
        layoutManager = LinearLayoutManager(this@ClientBrandActivity)
        adapter = productAdapter
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[ClientBrandVM::class.java]
        listenVM()
        val intent = intent
        if (intent.hasExtra(AdminBrandDetailActivity.ID) && intent.getStringExtra(
                AdminBrandDetailActivity.ID
            ) != null
        ) {
            id = intent.getStringExtra(AdminBrandDetailActivity.ID)!!
            getBrand()
        } else {
            finish()
        }
    }

    override fun initListener() {
        binding.imgBack.setOnClickListener { finish() }
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
    }

    private fun toastDialog(notification: String) {
        toast(notification)
        binding.clLoading.visibility = View.GONE
    }

}