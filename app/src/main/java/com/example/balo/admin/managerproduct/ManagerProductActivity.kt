package com.example.balo.admin.managerproduct

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
import com.example.balo.adapter.product.AdminProductEditAdapter
import com.example.balo.data.model.BaloEntity
import com.example.balo.databinding.ActivityAllProductBinding
import com.example.balo.admin.managerproduct.detailproduct.AdminProductDetailActivity
import com.example.balo.admin.managerproduct.detailproduct.AdminProductEditActivity
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Option
import com.example.balo.utils.Utils

class ManagerProductActivity : BaseActivity<ActivityAllProductBinding>() {

    private val products = mutableListOf<BaloEntity>()

    private lateinit var viewModel: ManagerProductVM

    private val chooseDelete = mutableListOf<BaloEntity>()

    private val productAdapter by lazy {
        AdminProductEditAdapter(products,
            listener = { pos ->
                startActivityForResult(
                    AdminProductDetailActivity.newIntent(this, products[pos].id),
                    REQUEST_CODE_CHANGE
                )
            },
            onCheckBox = {
                handleChooseCheckbox(it)
            })
    }

    companion object {

        const val REQUEST_CODE_CHANGE = 160
        fun newIntent(context: Context): Intent {
            return Intent(context, ManagerProductActivity::class.java)
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityAllProductBinding =
        ActivityAllProductBinding.inflate(inflate)

    override fun initView() = binding.rvProduct.run {
        layoutManager = LinearLayoutManager(this@ManagerProductActivity)
        adapter = productAdapter
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[ManagerProductVM::class.java]
        listenVM()
        updateProduct()
    }

    private fun handleChooseCheckbox(pair: Pair<Boolean, Int>) = binding.run {
        val cart = products[pair.second]
        products[pair.second].isSelected = pair.first
        if (pair.first) {
            chooseDelete.add(cart)
        } else {
            chooseDelete.remove(cart)
        }
        btnDelete.visibility = if (chooseDelete.size > 0) View.VISIBLE else View.GONE
    }

    private fun updateProduct() {
        binding.clLoading.visibility = View.VISIBLE
        viewModel.getAllProducts { showToast(it) }
    }

    override fun initListener() = binding.run {
        listenerEditText()
        tvTitle.setOnClickListener { finish() }
        imgAdd.setOnClickListener { handleAdd() }
        btnDelete.setOnClickListener { handleDelete() }
    }

    private fun handleAdd() {
        if(binding.clLoading.visibility == View.GONE) {
            startActivityForResult(
                AdminProductEditActivity.newIntent(this, AdminProductEditActivity.KEY_ADD),
                REQUEST_CODE_CHANGE
            )
        }
    }

    private fun handleDelete() {
        if(binding.clLoading.visibility == View.GONE) {
            Utils.showOption(this, Option.DELETE) {
                binding.clLoading.visibility = View.VISIBLE
                viewModel.deleteProducts(
                    chooseDelete,
                    handleSuccess = {
                        showToast(getString(R.string.delete_suceess))
                        setResult(RESULT_OK)
                        updateProduct()
                        binding.btnDelete.visibility =
                            if (chooseDelete.size > 0) View.VISIBLE else View.GONE
                    },
                    handleFail = { showToast(it) }
                )
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listenVM() {
        viewModel.products.observe(this) {
            if (it != null) {
                products.run {
                    clear()
                    addAll(it)
                }
                productAdapter.notifyDataSetChanged()
                binding.clLoading.visibility = View.GONE
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHANGE && resultCode == RESULT_OK) {
            updateProduct()
        }
    }

    private fun showToast(mess: String) {
        binding.clLoading.visibility = View.GONE
        toast(mess)
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