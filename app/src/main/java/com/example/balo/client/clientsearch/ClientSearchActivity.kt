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
import com.example.balo.R
import com.example.balo.adapter.product.UserProductAdapter
import com.example.balo.client.clientdetail.ClientDetailActivity
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.BrandEntity
import com.example.balo.databinding.ActivitySearchBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Constants
import com.google.gson.Gson

class ClientSearchActivity : BaseActivity<ActivitySearchBinding>() {

    private lateinit var viewModel: ClientSearchVM

    private var brand: BrandEntity? = null

    private val products = mutableListOf<BaloEntity>()

    private val productAdapter by lazy {
        UserProductAdapter(products) { pos ->
            startActivity(ClientDetailActivity.newIntent(this, products[pos].id))
        }
    }

    companion object {
        const val EMPTY_BRAND = ""
        const val KEY_SEARCH = "search_act"
        fun newIntent(context: Context, brand: String): Intent {
            return Intent(context, ClientSearchActivity::class.java).apply {
                putExtra(KEY_SEARCH, brand)
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivitySearchBinding =
        ActivitySearchBinding.inflate(inflate)

    override fun initView() = binding.run {
        if (brand != null) {
            val brandName = "${getString(R.string.brand)} ${brand!!.name}"
            tvBrand.text = brandName
            tvBrand.visibility = View.VISIBLE
        }
        rvProduct.run {
            layoutManager = LinearLayoutManager(this@ClientSearchActivity)
            adapter = productAdapter
        }
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[ClientSearchVM::class.java]
        listenVM()
        val intent = intent
        val receive = intent.getStringExtra(KEY_SEARCH)
        if (intent.hasExtra(KEY_SEARCH) && receive != null) {
            var id = Constants.ID_BRAND_OTHER
            if (receive != EMPTY_BRAND) {
                brand = Gson().fromJson(intent.getStringExtra(KEY_SEARCH), BrandEntity::class.java)
                id = brand!!.id
            }
            getProduct(id)
        } else {
            finish()
        }
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        listenerEditText()
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
    }

    private fun getProduct(id: String) = binding.run {
        clLoading.visibility = View.VISIBLE
        viewModel.getProducts(id) {
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
        viewModel.searchProduct(s)
    }
}