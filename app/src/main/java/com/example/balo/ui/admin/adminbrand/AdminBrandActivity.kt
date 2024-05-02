package com.example.balo.ui.admin.adminbrand

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.balo.R
import com.example.balo.data.model.BrandEntity
import com.example.balo.databinding.ActivityAdminBrandBinding
import com.example.balo.ui.base.BaseActivity
import com.example.balo.utils.Utils

class AdminBrandActivity : BaseActivity<ActivityAdminBrandBinding>() {

    private var uri: Uri? = null

    private lateinit var dialog: AlertDialog

    private lateinit var viewModel: AdminBrandVM

    companion object {

        const val REQUEST_CODE_IMAGE = 111
    }
    override fun viewBinding(inflate: LayoutInflater): ActivityAdminBrandBinding =
        ActivityAdminBrandBinding.inflate(inflate)

    override fun initView() {
        dialog = Utils.showProgressDialog(this)
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[AdminBrandVM::class.java]
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        btnAdd.setOnClickListener { handleAdd() }
        tvImport.setOnClickListener { handleImport() }
    }

    private fun handleAdd() = binding.run {
        if (uri != null && edtName.text.toString() != "") {
            if (!dialog.isShowing) dialog.show()
            val entity = BrandEntity(
                name = edtName.text.toString().toString(),
                des = edtDes.text.toString().trim(),
                pic = Utils.uriToBase64(this@AdminBrandActivity, uri!!)
            )
            viewModel.createBrand(
                brand = entity,
                handleSuccess = {
                    if (dialog.isShowing) dialog.dismiss()
                    toast(getString(R.string.success_brand))
                    setResult(RESULT_OK)
                    finish()
                },
                handleFail = { error ->
                    if (dialog.isShowing) dialog.dismiss()
                    toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
                }
            )
        } else {
            tvError.visibility = View.VISIBLE
        }
    }

    private fun handleImport() {
        Utils.requireGalleryPermission(this) {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_IMAGE)
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
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImageFromGallery()
        } else {
            Utils.showRequirePermission(this) { finish() }
        }
    }
}