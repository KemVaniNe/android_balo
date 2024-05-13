package com.example.balo.ui.admin.managerproduct

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.R
import com.example.balo.adapter.brand.BrandAdapter
import com.example.balo.adapter.product.AdminProductAdapter
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.BrandEntity
import com.example.balo.databinding.ActivityAllProductBinding
import com.example.balo.ui.admin.adminproduct.AdminProductFragment
import com.example.balo.ui.admin.adminproduct.AdminProductVM
import com.example.balo.ui.admin.managerbrand.AdminBrandActivity
import com.example.balo.ui.admin.managerproduct.choosebrand.ChooseBrandVM
import com.example.balo.ui.admin.managerproduct.detail.AdminDetailProductActivity
import com.example.balo.ui.base.BaseActivity
import com.example.balo.utils.Utils
import com.google.gson.Gson

class ManagerProductActivity : BaseActivity<ActivityAllProductBinding>() {

    private val products = mutableListOf<BaloEntity>()

    private lateinit var dialog: AlertDialog

    private lateinit var viewModel: ManagerProductVM

    private val productAdapter by lazy {
        AdminProductAdapter(products) { pos ->
            startActivityForResult(
                AdminDetailProductActivity.newIntent(this, products[pos].id),
                REQUEST_CODE_CHANGE
            )
        }
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
        dialog = Utils.showProgressDialog(this)
        viewModel = ViewModelProvider(this)[ManagerProductVM::class.java]
        listenVM()
        updateProduct()
    }

    private fun updateProduct() {
        if (!dialog.isShowing) dialog.show()
        viewModel.getAllProducts { error ->
            if (dialog.isShowing) dialog.dismiss()
            toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
        }
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        imgAdd.setOnClickListener { handleAdd() }
    }

    private fun handleAdd() {
        startActivityForResult(
            AdminDetailProductActivity.newIntent(this, AdminDetailProductActivity.KEY_ADD),
            REQUEST_CODE_CHANGE
        )
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
            setResult(RESULT_OK)
            updateProduct()
        }
    }
}