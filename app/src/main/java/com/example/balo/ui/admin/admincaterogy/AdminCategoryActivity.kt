package com.example.balo.ui.admin.admincaterogy

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.view.LayoutInflater
import com.example.balo.databinding.ActivityAdminCategoryBinding
import com.example.balo.ui.admin.mainadmin.AdminActivity
import com.example.balo.ui.base.BaseActivity
import com.example.balo.utils.Utils

class AdminCategoryActivity : BaseActivity<ActivityAdminCategoryBinding>() {

    companion object {

        const val REQUEST_CODE_IMAGE = 111
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityAdminCategoryBinding =
        ActivityAdminCategoryBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        btnAdd.setOnClickListener { handleAdd() }
        tvImport.setOnClickListener { handleImport() }
    }

    private fun handleAdd() {

    }

    private fun handleImport() {
//        Utils.requireGalleryPermission(this) {
//            pickImageFromGallery()
//        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK) {
            binding.imgPic.setImageURI(data?.data)
        }
    }
}