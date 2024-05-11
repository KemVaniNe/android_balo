package com.example.balo.ui.admin.balo.choosebrand

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.R
import com.example.balo.adapter.BrandBottomAdapter
import com.example.balo.data.model.BrandEntity
import com.example.balo.databinding.ActivityAdminChooseBrandBinding
import com.example.balo.ui.base.BaseActivity
import com.example.balo.utils.Utils
import com.google.gson.Gson

class AdminChooseBrandActivity : BaseActivity<ActivityAdminChooseBrandBinding>() {

    private val brands = mutableListOf<BrandEntity>()

    private var viewModel = ChooseBrandVM()

    private lateinit var dialog: AlertDialog

    private var brand: BrandEntity? = null

    private val brandAdapter by lazy {
        BrandBottomAdapter(brands) { pos ->
            brand = brands[pos]
            brands.map { it.isSelected = it == brands[pos] }
        }
    }

    companion object {

        const val KEY_BRAND = "key_brand"
        fun newIntent(context: Context, response: String): Intent {
            return Intent(context, AdminChooseBrandActivity::class.java).apply {
                putExtra(KEY_BRAND, response)
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityAdminChooseBrandBinding =
        ActivityAdminChooseBrandBinding.inflate(inflate)

    override fun initView() = binding.rvBrand.run {
        layoutManager = LinearLayoutManager(this@AdminChooseBrandActivity)
        adapter = brandAdapter
    }

    override fun initData() {
        dialog = Utils.showProgressDialog(this)
        viewModel = ViewModelProvider(this)[ChooseBrandVM::class.java]
        listenVM()
        val intent = intent
        if (intent.hasExtra(KEY_BRAND) && intent.getStringExtra(KEY_BRAND) != null) {
            if (!dialog.isShowing) dialog.show()
            viewModel.getAllBrands(intent.getStringExtra(KEY_BRAND)!!) {
                if (dialog.isShowing) dialog.dismiss()
                toast(it)
            }
        } else {
            finish()
        }
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        btnChoose.setOnClickListener { handleChoose() }
    }

    private fun handleChoose() {
        if (brand != null) {
            val intent = Intent()
            intent.putExtra(KEY_BRAND, Gson().toJson(brand))
            setResult(RESULT_OK, intent)
            finish()

        } else {
            toast(getString(R.string.you_need_choose_brand))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listenVM() {
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
}