package com.example.balo.ui.user.search

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.BrandEntity
import com.example.balo.databinding.ActivitySearchBinding
import com.example.balo.ui.base.BaseActivity
import com.example.balo.utils.Utils
import com.google.gson.Gson

class SearchActivity : BaseActivity<ActivitySearchBinding>() {

    private lateinit var dialog: AlertDialog
    private lateinit var viewModel: SearchVM
    private var brand: BrandEntity? = null
    private val products = mutableListOf<BaloEntity>()
    private val productAdapter by lazy {
        UserProductAdapter(products) { pos ->
            //TODO
        }
    }

    companion object {

        const val EMPTY_BRAND = ""
        const val KEY_SEARCH = "search_act"
        fun newIntent(context: Context, brand: String): Intent {
            return Intent(context, SearchActivity::class.java).apply {
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
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = productAdapter
        }
    }

    override fun initData() {
        dialog = Utils.showProgressDialog(this)
        viewModel = ViewModelProvider(this)[SearchVM::class.java]
        listenVM()
        val intent = intent
        if (intent.hasExtra(KEY_SEARCH) && intent.getStringExtra(KEY_SEARCH) != null) {
            if (intent.getStringExtra(KEY_SEARCH) == EMPTY_BRAND) {
                getProduct("")
            } else {
                brand = Gson().fromJson(intent.getStringExtra(KEY_SEARCH), BrandEntity::class.java)
                getProduct(brand!!.id)
            }
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
                if (dialog.isShowing) dialog.dismiss()
                products.run {
                    clear()
                    addAll(it)
                }
                val numProduct = "${products.size} sản phẩm"
                binding.tvNumProduct.text = numProduct
                productAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun getProduct(id: String) {
        if (!dialog.isShowing) dialog.show()
        viewModel.getProducts(id) { error ->
            if (dialog.isShowing) dialog.dismiss()
            toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
        }
    }

    private fun listenerEditText() {
        binding.edtSearch.run {
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, af: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    search(s.toString().trim())
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        }
    }

    private fun search(s: String) = binding.run {
        if (!dialog.isShowing) dialog.show()
        viewModel.searchProduct(s)
    }
}