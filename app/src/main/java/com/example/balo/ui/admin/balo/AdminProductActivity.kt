package com.example.balo.ui.admin.balo

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.balo.R
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.BrandEntity
import com.example.balo.databinding.ActivityAdminProductBinding
import com.example.balo.ui.admin.adminbrand.AdminBrandActivity
import com.example.balo.ui.admin.adminbrand.AdminBrandVM
import com.example.balo.ui.base.BaseActivity
import com.example.balo.utils.Option
import com.example.balo.utils.Utils

class AdminProductActivity : BaseActivity<ActivityAdminProductBinding>() {

    private var uri: Uri? = null

    private lateinit var dialog: AlertDialog

    private lateinit var viewModel: AdminProductVM

    private var productCurrent: BaloEntity? = null

    companion object {

        const val REQUEST_CODE_IMAGE = 111
        const val KEY_PRODUCT = "admin_product"
        const val KEY_ADD = ""
        fun newIntent(context: Context, response: String): Intent {
            return Intent(context, AdminProductActivity::class.java).apply {
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
        viewModel = ViewModelProvider(this)[AdminProductVM::class.java]
        listenVM()
        val intent = intent
        if (intent.hasExtra(KEY_PRODUCT) && intent.getStringExtra(KEY_PRODUCT) != null) {
            if (intent.getStringExtra(KEY_PRODUCT) != KEY_ADD) {
                if (!dialog.isShowing) dialog.show()
                viewModel.getBaloById(intent.getStringExtra(AdminBrandActivity.KEY_BRAND)!!) {
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
            if (edtName.text.toString() != "" && edtBrand.text.toString() != ""
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
            idBrand = edtBrand.text.toString().trim(),
            pic = Utils.uriToBase64(this@AdminProductActivity, uri!!),
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
                idBrand = edtBrand.text.toString().trim(),
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
        viewModel.currentProduct.observe(this) {
            if (it != null) {
                if (dialog.isShowing) dialog.dismiss()
                productCurrent = it
                binding.run {
                    edtName.setText(productCurrent!!.name)
                    edtBrand.setText(productCurrent!!.idBrand)
                    edtQuantity.setText(productCurrent!!.quantitiy)
                    edtPriceSell.setText(productCurrent!!.priceSell)
                    edtPriceImport.setText(productCurrent!!.priceImport)
                    Utils.displayBase64Image(productCurrent!!.pic, imgPic)
                    tvDes.visibility = View.GONE
                    btnAdd.text = getString(R.string.update)
                    btnDelete.visibility = View.VISIBLE
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