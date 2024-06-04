package com.example.balo.admin.managerproduct

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
        if (!dialog.isShowing) dialog.show()
        viewModel.getAllProducts { error ->
            if (dialog.isShowing) dialog.dismiss()
            toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
        }
    }

    override fun initListener() = binding.run {
        tvTitle.setOnClickListener { finish() }
        imgAdd.setOnClickListener { handleAdd() }
        btnDelete.setOnClickListener { handleDelete() }
    }

    private fun handleAdd() {
        startActivityForResult(
            AdminProductEditActivity.newIntent(this, AdminProductEditActivity.KEY_ADD),
            REQUEST_CODE_CHANGE
        )
    }

    private fun handleDelete() {
        Utils.showOption(this, Option.DELETE) {
            if (!dialog.isShowing) dialog.show()
            viewModel.deleteProducts(chooseDelete, handleSuccess = {
                if (dialog.isShowing) dialog.dismiss()
                toast(getString(R.string.delete_suceess))
                setResult(RESULT_OK)
                updateProduct()
                binding.btnDelete.visibility =
                    if (chooseDelete.size > 0) View.VISIBLE else View.GONE
            }, handleFail = { error ->
                if (dialog.isShowing) dialog.dismiss()
                toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
            })
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
                if (dialog.isShowing) dialog.dismiss()
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
}