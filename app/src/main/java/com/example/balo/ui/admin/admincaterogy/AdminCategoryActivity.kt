package com.example.balo.ui.admin.admincaterogy

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.balo.R
import com.example.balo.data.model.CategoryEntity
import com.example.balo.databinding.ActivityAdminCategoryBinding
import com.example.balo.ui.base.BaseActivity
import com.example.balo.utils.Utils

class AdminCategoryActivity : BaseActivity<ActivityAdminCategoryBinding>() {

    private var uri: Uri? = null

    private lateinit var dialog: AlertDialog

    private lateinit var viewModel: AdminCategoryVM

    companion object {

        const val REQUEST_CODE_IMAGE = 111
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityAdminCategoryBinding =
        ActivityAdminCategoryBinding.inflate(inflate)

    override fun initView() {
        dialog = Utils.showProgressDialog(this)
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[AdminCategoryVM::class.java]
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        btnAdd.setOnClickListener { handleAdd() }
        tvImport.setOnClickListener { handleImport() }
    }

    private fun handleAdd() = binding.run {
        if (uri != null && edtCategory.text.toString() != "") {
            if (!dialog.isShowing) dialog.show()
            val entity = CategoryEntity(
                name = edtCategory.text.toString(),
                pic = Utils.uriToBase64(this@AdminCategoryActivity, uri!!)
            )
            viewModel.createCategory(
                category = entity,
                handleSuccess = {
                    if (dialog.isShowing) dialog.dismiss()
                    (getString(R.string.success_category))
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