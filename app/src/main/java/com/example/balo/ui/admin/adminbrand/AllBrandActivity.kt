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
import com.google.gson.Gson

class AllBrandActivity : BaseActivity<ActivityAllBrandBinding>() {

    private lateinit var viewModel: AdminBrandVM

    private lateinit var dialog: AlertDialog

    private val brands = mutableListOf<BrandEntity>()

    private val brandAdapter by lazy {
        BrandAdapter(brands) { pos ->
            startActivity(
                AdminBrandActivity.newIntent(this@AllBrandActivity, Gson().toJson(brands[pos]))
            )
        }
    }

    companion object {

        const val KEY_ALL_BRAND = "admin_all_brand"

        const val REQUEST_CODE_ADD = 111
        fun newIntent(context: Context, response: List<String>): Intent {
            return Intent(context, AllBrandActivity::class.java).apply {
                putStringArrayListExtra(KEY_ALL_BRAND, ArrayList(response))
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityAllBrandBinding =
        ActivityAllBrandBinding.inflate(inflate)

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() = binding.run {
        rvBrand.layoutManager = LinearLayoutManager(this@AllBrandActivity)
        rvBrand.adapter = brandAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        viewModel = ViewModelProvider(this)[AdminBrandVM::class.java]
        listenData()
        val intent = intent
        if (intent.hasExtra(KEY_ALL_BRAND)) {
            val brandList = intent.getStringArrayListExtra(KEY_ALL_BRAND)
            if (brandList != null) {
                val list = Utils.convertJsonListToObjectList<BrandEntity>(brandList)
                brands.run {
                    clear()
                    addAll(list)
                }
                brandAdapter.notifyDataSetChanged()
            }
        } else {
            finish()
        }
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