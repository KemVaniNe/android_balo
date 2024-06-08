package com.example.balo.admin.managerproduct.detailproduct

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.balo.R
import com.example.balo.admin.managerbrand.detail.AdminBrandEditActivity
import com.example.balo.admin.managerproduct.ManagerProductVM
import com.example.balo.admin.managerproduct.choosebrand.AdminChooseBrandActivity
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.BrandEntity
import com.example.balo.databinding.ActivityAdminProductEditBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Option
import com.example.balo.utils.Utils
import com.google.gson.Gson

class AdminProductEditActivity : BaseActivity<ActivityAdminProductEditBinding>() {

    private var uri: Uri? = null

    private lateinit var viewModel: ManagerProductVM

    private var productCurrent: BaloEntity? = null

    private var currentBrand: BrandEntity? = null

    companion object {

        const val REQUEST_CODE_IMAGE = 111
        const val REQUEST_CHOOSE_BRAND = 123
        const val KEY_PRODUCT = "admin_product"
        const val KEY_ADD = ""
        fun newIntent(context: Context, response: String): Intent {
            return Intent(context, AdminProductEditActivity::class.java).apply {
                putExtra(KEY_PRODUCT, response)
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityAdminProductEditBinding =
        ActivityAdminProductEditBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[ManagerProductVM::class.java]
        listenVM()
        val intent = intent
        if (intent.hasExtra(KEY_PRODUCT) && intent.getStringExtra(KEY_PRODUCT) != null) {
            if (intent.getStringExtra(KEY_PRODUCT) != KEY_ADD) {
                viewModel.getProducts(intent.getStringExtra(KEY_PRODUCT)!!) {
                    showToast(it)
                    finishAct(false)
                }
            } else {
                binding.clLoading.visibility = View.GONE
            }
        } else {
            finishAct(false)
        }
    }

    override fun initListener() = binding.run {
        tvTitle.setOnClickListener { finish() }
        btnAdd.setOnClickListener {
            if (clLoading.visibility == View.GONE) {
                handleAdd()
            }
        }
        tvImport.setOnClickListener {
            if (clLoading.visibility == View.GONE) {
                handleImport()
            }
        }
        btnDelete.setOnClickListener {
            if (clLoading.visibility == View.GONE) {
                handleDelete()
            }
        }
        tvBrand.setOnClickListener {
            if (clLoading.visibility == View.GONE) {
                handleBrand()
            }
        }
    }

    private fun handleBrand() {
        val data = if (currentBrand == null) "" else Gson().toJson(currentBrand)
        startActivityForResult(
            AdminChooseBrandActivity.newIntent(this, data), REQUEST_CHOOSE_BRAND
        )
    }

    private fun handleDelete() {
        Utils.showOption(this, Option.DELETE) {
            binding.clLoading.visibility = View.VISIBLE
            deleteBrand()
        }
    }

    private fun deleteBrand() {
        viewModel.deleteProduct(
            productCurrent!!.id,
            handleSuccess = {
                showToast(getString(R.string.delete_suceess))
                finishAct(true)
            },
            handleFail = { showToast(it) })
    }

    private fun handleAdd() = binding.run {
        when (productCurrent) {
            null -> {
                if (uri != null && isFillAllInfo()) {
                    binding.clLoading.visibility = View.VISIBLE
                    handleCreate()
                }
            }

            else -> {
                if (isFillAllInfo()) {
                    binding.clLoading.visibility = View.VISIBLE
                    handleUpdate()
                }
            }
        }
    }

    private fun isFillAllInfo(): Boolean {
        binding.run {
            if (edtName.text.toString() != "" && currentBrand != null
                && edtAddNum.text.toString() != "" && edtPriceSell.text.toString() != ""
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
        val priceImport = Utils.stringToDouble(edtPriceImport.text.toString())
        val quantity = Utils.stringToDouble(edtAddNum.text.toString())
        val totalPrice = priceImport * quantity
        val entity = BaloEntity(
            name = edtName.text.toString(),
            idBrand = currentBrand!!.id,
            pic = Utils.uriToBase64(this@AdminProductEditActivity, uri!!),
            priceSell = Utils.stringToDouble(edtPriceSell.text.toString()),
            priceImport = priceImport,
            des = edtDes.text.toString().trim(),
            quantitiy = quantity,
            totalImport = totalPrice
        )
        viewModel.createProduct(
            product = entity,
            handleSuccess = {
                showToast(getString(R.string.success_brand))
                finishAct(true)
            },
            handleFail = { showToast(it) }
        )
    }

    private fun handleUpdate() {
        val pic = if (uri != null) Utils.uriToBase64(this, uri!!) else productCurrent!!.pic
        binding.run {
            val entity = BaloEntity(
                id = productCurrent!!.id,
                name = edtName.text.toString().trim(),
                idBrand = currentBrand!!.id,
                pic = pic,
                priceSell = Utils.stringToDouble(edtPriceSell.text.toString()),
                priceImport = Utils.stringToDouble(edtPriceImport.text.toString()),
                des = edtDes.text.toString().trim(),
                quantitiy = Utils.stringToDouble(edtQuantity.text.toString()),
                sell = productCurrent!!.sell,
                rate = productCurrent!!.rate,
                comment = productCurrent!!.comment,
                totalImport = productCurrent!!.totalImport,
                totalSell = productCurrent!!.totalSell,
                isSell = productCurrent!!.isSell
            )
            viewModel.updateProduct(
                numProductAdd = Utils.stringToInt(edtAddNum.text.toString()),
                product = entity,
                handleSuccess = {
                    showToast(getString(R.string.update_product))
                    finishAct(true)
                },
                handleFail = { showToast(it) }
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
        startActivityForResult(intent, AdminBrandEditActivity.REQUEST_CODE_IMAGE)
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
                binding.clLoading.visibility = View.VISIBLE
            } else {
                if (viewModel.currentProduct != null && viewModel.brandCurrent != null) {
                    currentBrand = viewModel.brandCurrent
                    productCurrent = viewModel.currentProduct
                    binding.clLoading.visibility = View.GONE
                    binding.run {
                        edtName.setText(productCurrent!!.name)
                        tvBrand.text = currentBrand!!.name
                        edtQuantity.setText(productCurrent!!.quantitiy.toString())
                        edtSold.setText(productCurrent!!.sell.toString())
                        edtPriceSell.setText(productCurrent!!.priceSell.toString())
                        edtPriceImport.setText(productCurrent!!.priceImport.toString())
                        Utils.displayBase64Image(productCurrent!!.pic, imgPic)
                        tvDes.visibility = View.GONE
                        btnAdd.text = getString(R.string.update)
                        btnDelete.visibility = View.VISIBLE
                        edtDes.setText(productCurrent!!.des)
                        llEdit.visibility = View.VISIBLE
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

    private fun showToast(mess: String) {
        binding.clLoading.visibility = View.GONE
        toast(mess)
    }
}