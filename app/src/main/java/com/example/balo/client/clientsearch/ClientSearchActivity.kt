package com.example.balo.client.clientsearch

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.adapter.product.UserProductAdapter
import com.example.balo.client.clientdetail.ClientDetailActivity
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.BrandEntity
import com.example.balo.databinding.ActivitySearchBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Constants
import com.example.balo.utils.Utils

class ClientSearchActivity : BaseActivity<ActivitySearchBinding>() {

    private lateinit var viewModel: ClientSearchVM

    private val products = mutableListOf<BaloEntity>()

    private var sort = Constants.TYPE_NONE

    private var brand = Utils.brandAll()

    private var list: List<BrandEntity> = emptyList()

    private val productAdapter by lazy {
        UserProductAdapter(products) { pos ->
            startActivity(ClientDetailActivity.newIntent(this, products[pos].id))
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ClientSearchActivity::class.java)
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivitySearchBinding =
        ActivitySearchBinding.inflate(inflate)

    override fun initView() = binding.run {
        rvProduct.run {
            layoutManager = LinearLayoutManager(this@ClientSearchActivity)
            adapter = productAdapter
        }
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[ClientSearchVM::class.java]
        listenVM()
        getProduct()
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        listenerEditText()
        tvFilter.setOnClickListener { handleFilter() }
    }

    private fun handleFilter() {
        if (binding.clLoading.visibility == View.GONE) {
            Utils.bottomFilter(
                context = this,
                soft = sort,
                brand = brand,
                list = list,
                listener = {
                    binding.edtSearch.setText("")
                    sort = it.first
                    brand = it.second
                    search("")
                }
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listenVM() {
        viewModel.product.observe(this) {
            if (it != null) {
                products.run {
                    clear()
                    addAll(it)
                }
                val numProduct = "${products.size} sản phẩm"
                binding.run {
                    tvNumProduct.text = numProduct
                    clLoading.visibility = View.GONE
                    rvProduct.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                    llNone.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                }
                productAdapter.notifyDataSetChanged()
            }
        }

        viewModel.isLoading.observe(this) {
            binding.clLoading.visibility = if (it) View.VISIBLE else View.GONE
            list = viewModel.listBrand
        }
    }

    private fun getProduct() = binding.run {
        clLoading.visibility = View.VISIBLE
        viewModel.getProduct {
            clLoading.visibility = View.GONE
            toast(it)
        }
    }

    private fun listenerEditText() {
        binding.edtSearch.run {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, af: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    search(s.toString().trim())
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }

    private fun search(s: String) = binding.run {
        clLoading.visibility = View.VISIBLE
        viewModel.searchProduct(s, sort, brand.id)
    }
}