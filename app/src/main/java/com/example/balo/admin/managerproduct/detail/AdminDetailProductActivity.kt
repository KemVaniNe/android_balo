package com.example.balo.admin.managerproduct.detail

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.balo.R
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.BrandEntity
import com.example.balo.databinding.ActivityAdminProductBinding
import com.example.balo.admin.managerbrand.AdminBrandActivity
import com.example.balo.admin.managerproduct.choosebrand.AdminChooseBrandActivity
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Option
import com.example.balo.utils.Utils
import com.google.gson.Gson

class AdminDetailProductActivity : BaseActivity<ActivityAdminProductBinding>() {

    private var uri: Uri? = null

    private lateinit var dialog: AlertDialog

    private lateinit var viewModel: AdminDetailProductVM

    private var productCurrent: BaloEntity? = null

    private var currentBrand: BrandEntity? = null

    companion object {

        const val REQUEST_CODE_IMAGE = 111
        const val REQUEST_CHOOSE_BRAND = 123
        const val KEY_PRODUCT = "admin_product"
        const val KEY_ADD = ""
        fun newIntent(context: Context, response: String): Intent {
            return Intent(context, AdminDetailProductActivity::class.java).apply {
                putExtra(KEY_PRODUCT, response)
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityAdminProductBinding =
        ActivityAdminProductBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
        dialog = Utils.showProgressDialog(this)
        viewModel = ViewModelProvider(this)[AdminDetailProductVM::class.java]
        listenVM()
        val intent = intent
        if (intent.hasExtra(KEY_PRODUCT) && intent.getStringExtra(KEY_PRODUCT) != null) {
            if (intent.getStringExtra(KEY_PRODUCT) != KEY_ADD) {
                viewModel.getBaloById(intent.getStringExtra(KEY_PRODUCT)!!) {
                    if (dialog.isShowing) dialog.dismiss()
                    toast(it)
                    finishAct(false)
                }
            }
        } else {
            finishAct(false)
        }
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        btnAdd.setOnClickListener { handleAdd() }
        tvImport.setOnClickListener { handleImport() }
        btnDelete.setOnClickListener { handleDelete() }
        tvBrand.setOnClickListener { handleBrand() }
    }

    private fun handleBrand() {
        val data = if (currentBrand == null) "" else Gson().toJson(currentBrand)
        startActivityForResult(
            AdminChooseBrandActivity.newIntent(this, data), REQUEST_CHOOSE_BRAND
        )
    }

    private fun handleDelete() {
        Utils.showOption(this, Option.DELETE) {
            if (!dialog.isShowing) dialog.show()
            deleteBrand()
        }
    }

    private fun deleteBrand() {
        viewModel.deleteProduct(productCurrent!!.id, handleSuccess = {
            if (dialog.isShowing) dialog.dismiss()
            toast(getString(R.string.delete_suceess))
            finishAct(true)
        }, handleFail = { error ->
            if (dialog.isShowing) dialog.dismiss()
            toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
        })
    }

    private fun handleAdd() = binding.run {
        when (productCurrent) {
            null -> {
                if (uri != null && isFillAllInfo()) {
                    if (!dialog.isShowing) dialog.show()
                    handleCreate()
                }
            }

            else -> {
                if (isFillAllInfo()) {
                    if (!dialog.isShowing) dialog.show()
                    handleUpdate()
                }
            }
        }
    }

    private fun isFillAllInfo(): Boolean {
        binding.run {
            if (edtName.text.toString() != "" && currentBrand != null
                && edtQuantity.text.toString() != "" && edtPriceSell.text.toString() != ""
                && edtPriceImport.text.toString() != ""
            ) {
                return true
            } else {
                tvError.visibility = View.VISIBLE
                return false
            }
        }
    }

    private fun handleCreate() = binding.run {
        val entity = BaloEntity(
            name = edtName.text.toString().trim(),
            idBrand = currentBrand!!.id,
            pic = Utils.uriToBase64(this@AdminDetailProductActivity, uri!!),
            priceSell = edtPriceSell.text.toString().trim(),
            priceImport = edtPriceImport.text.toString().trim(),
            des = edtDes.text.toString().trim(),
            quantitiy = edtQuantity.text.toString().trim(),
        )
        viewModel.createProduct(
            product = entity,
            handleSuccess = {
                if (dialog.isShowing) dialog.dismiss()
                toast(getString(R.string.success_brand))
                finishAct(true)
            },
            handleFail = { error ->
                if (dialog.isShowing) dialog.dismiss()
                toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
            }
        )
    }

    private fun handleUpdate() {
        val pic = if (uri != null) Utils.uriToBase64(this, uri!!) else productCurrent!!.pic
        binding.run {
            val entity = BaloEntity(
                name = edtName.text.toString().trim(),
                idBrand = currentBrand!!.id,
                pic = pic,
                priceSell = edtPriceSell.text.toString().trim(),
                priceImport = edtPriceImport.text.toString().trim(),
                des = edtDes.text.toString().trim(),
                quantitiy = edtQuantity.text.toString().trim(),
            )
            viewModel.updateProduct(
                product = entity,
                idDocument = productCurrent!!.id,
                handleSuccess = {
                    if (dialog.isShowing) dialog.dismiss()
                    toast(getString(R.string.update_product))
                    finishAct(true)
                },
                handleFail = { error ->
                    if (dialog.isShowing) dialog.dismiss()
                    toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
                }
            )
        }
    }


    private fun handleImport() {
        Utils.requireGalleryPermission(this) {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, AdminBrandActivity.REQUEST_CODE_IMAGE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                uri = data.data
                binding.imgPic.setImageURI(data.data)
                Utils.uriToBase64(this, uri!!)
            }
        } else if (requestCode == REQUEST_CHOOSE_BRAND && resultCode == RESULT_OK) {
            val brandJson = data?.getStringExtra(AdminChooseBrandActivity.KEY_BRAND)
            if (brandJson != null) {
                currentBrand = Gson().fromJson(brandJson, BrandEntity::class.java)
                binding.tvBrand.text = currentBrand!!.name
            }
        }
        //WHEN BACK FROM SETTING
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImageFromGallery()
        } else {
            Utils.showRequirePermission(this) { finishAct(false) }
        }
    }

    private fun listenVM() {
        viewModel.isLoading.observe(this) {
            if (it) {
                if (!dialog.isShowing) dialog.show()
            } else {
                if (viewModel.currentProduct != null && viewModel.brandCurrent != null) {
                    currentBrand = viewModel.brandCurrent
                    productCurrent = viewModel.currentProduct
                    if (dialog.isShowing) dialog.dismiss()
                    binding.run {
                        edtName.setText(productCurrent!!.name)
                        tvBrand.text = currentBrand!!.name
                        edtQuantity.setText(productCurrent!!.quantitiy)
                        edtPriceSell.setText(productCurrent!!.priceSell)
                        edtPriceImport.setText(productCurrent!!.priceImport)
                        Utils.displayBase64Image(productCurrent!!.pic, imgPic)
                        tvDes.visibility = View.GONE
                        btnAdd.text = getString(R.string.update)
                        btnDelete.visibility = View.VISIBLE
                        edtDes.setText(productCurrent!!.des)
                    }
                }
            }
        }
    }

    private fun finishAct(isOK: Boolean) {
        viewModel.resetCurrentProduct()
        if (isOK) setResult(RESULT_OK)
        finish()
    }

}