package com.example.balo.admin.managerbrand

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
import com.example.balo.adapter.brand.EditBrandAdapter
import com.example.balo.admin.managerbrand.detail.AdminBrandDetailActivity
import com.example.balo.admin.managerbrand.detail.AdminBrandEditActivity
import com.example.balo.data.model.BrandEntity
import com.example.balo.databinding.ActivityAllBrandBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Option
import com.example.balo.utils.Utils

class ManagerBrandActivity : BaseActivity<ActivityAllBrandBinding>() {

    private lateinit var viewModel: ManagerBrandVM

    private val brands = mutableListOf<BrandEntity>()

    private val chooseDelete = mutableListOf<String>()

    private val brandAdapter by lazy {
        EditBrandAdapter(brands, listener = { pos ->
            startActivityForResult(
                AdminBrandDetailActivity.newIntent(this@ManagerBrandActivity, brands[pos].id),
                REQUEST_CODE_ADD
            )
        }, onCheckBox = {
            if (it.first) chooseDelete.add(it.second)
            else chooseDelete.remove(it.second)
            binding.btnDelete.visibility = if (chooseDelete.size > 0) View.VISIBLE else View.GONE
        })
    }

    companion object {

        const val REQUEST_CODE_ADD = 160
        fun newIntent(context: Context): Intent {
            return Intent(context, ManagerBrandActivity::class.java)
        }
    }


    override fun viewBinding(inflate: LayoutInflater): ActivityAllBrandBinding =
        ActivityAllBrandBinding.inflate(inflate)

    @SuppressLint("NotifyDataSetChanged")
    override fun initView() = binding.run {
        updateList()
        rvBrand.layoutManager = LinearLayoutManager(this@ManagerBrandActivity)
        rvBrand.adapter = brandAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        viewModel = ViewModelProvider(this)[ManagerBrandVM::class.java]
        listenData()
    }

    override fun initListener() = binding.run {
        listenerEditText()
        tvTitle.setOnClickListener { finish() }
        imgAdd.setOnClickListener { handleAdd() }
        btnDelete.setOnClickListener { handleDelete() }
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
                binding.clLoading.visibility = View.GONE
            }
        }
    }

    private fun handleDelete() {
        if(binding.clLoading.visibility == View.GONE) {
            Utils.showOption(this, Option.DELETE) {
                binding.clLoading.visibility = View.VISIBLE
                viewModel.deleteBrands(
                    chooseDelete,
                    handleSuccess = {
                        showToast(getString(R.string.delete_suceess))
                        setResult(RESULT_OK)
                        updateList()
                        binding.btnDelete.visibility =
                            if (chooseDelete.size > 0) View.VISIBLE else View.GONE
                    },
                    handleFail = { showToast(it) }
                )
            }
        }
    }

    private fun updateList() {
        binding.clLoading.visibility = View.VISIBLE
        viewModel.getAllBrands(handleFail = { showToast(it) })
    }

    private fun handleAdd() {
        if(binding.clLoading.visibility == View.GONE) {
            startActivityForResult(
                AdminBrandEditActivity.newIntent(this, AdminBrandEditActivity.KEY_ADD),
                REQUEST_CODE_ADD
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ADD && resultCode == RESULT_OK) {
            setResult(RESULT_OK)
            updateList()
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