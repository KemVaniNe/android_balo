package com.example.balo.ui.admin.adminbrand

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.R
import com.example.balo.data.model.BrandEntity
import com.example.balo.databinding.ActivityAllBrandBinding
import com.example.balo.ui.base.BaseActivity
import com.example.balo.ui.share.BrandAdapter
import com.example.balo.utils.Utils

class AllBrandActivity : BaseActivity<ActivityAllBrandBinding>() {

    private lateinit var viewModel: AdminBrandVM

    private lateinit var dialog: AlertDialog

    private val brands = mutableListOf<BrandEntity>()

    private val brandAdapter by lazy {
        BrandAdapter(brands) { pos ->
            startActivityForResult(
                AdminBrandActivity.newIntent(this@AllBrandActivity, brands[pos].id),
                REQUEST_CODE_ADD
            )
        }
    }

    companion object {

        const val REQUEST_CODE_ADD = 160
        fun newIntent(context: Context): Intent {
            return Intent(context, AllBrandActivity::class.java)
        }
    }


    override fun viewBinding(inflate: LayoutInflater): ActivityAllBrandBinding =
        ActivityAllBrandBinding.inflate(inflate)

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() = binding.run {
        updateList()
        rvBrand.layoutManager = LinearLayoutManager(this@AllBrandActivity)
        rvBrand.adapter = brandAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        dialog = Utils.showProgressDialog(this)
        viewModel = ViewModelProvider(this)[AdminBrandVM::class.java]
        listenData()
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        imgAdd.setOnClickListener { handleAdd() }
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
    }

    private fun updateList() {
        if (!dialog.isShowing) dialog.show()
        viewModel.getAllBrands(
            handleFail = { error ->
                if (dialog.isShowing) dialog.dismiss()
                toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
            })
    }

    private fun handleAdd() {
        startActivityForResult(
            AdminBrandActivity.newIntent(this, AdminBrandActivity.KEY_ADD),
            REQUEST_CODE_ADD
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {
            setResult(RESULT_OK)
            updateList()
        }
    }

}