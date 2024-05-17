package com.example.balo.admin.managerbrand

import android.content.Context
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
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Option
import com.example.balo.utils.Utils

class AdminBrandActivity : BaseActivity<ActivityAdminBrandBinding>() {

    private var uri: Uri? = null

    private lateinit var viewModel: AdminBrandVM

    private var brandCurrent: BrandEntity? = null

    companion object {

        const val REQUEST_CODE_IMAGE = 111
        const val KEY_BRAND = "admin_brand"
        const val KEY_ADD = ""
        fun newIntent(context: Context, response: String): Intent {
            return Intent(context, AdminBrandActivity::class.java).apply {
                putExtra(KEY_BRAND, response)
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityAdminBrandBinding =
        ActivityAdminBrandBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[AdminBrandVM::class.java]
        listenVM()
        val intent = intent
        if (intent.hasExtra(KEY_BRAND) && intent.getStringExtra(KEY_BRAND) != null) {
            if (intent.getStringExtra(KEY_BRAND) != "") {
                if (!dialog.isShowing) dialog.show()
                viewModel.getBrandById(intent.getStringExtra(KEY_BRAND)!!) {
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
        imgBack.setOnClickListener { finishAct(false) }
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
        viewModel.deleteBrand(brandCurrent!!.id, handleSuccess = {
            if (dialog.isShowing) dialog.dismiss()
            toast(getString(R.string.delete_suceess))
            finishAct(true)
        }, handleFail = { error ->
            if (dialog.isShowing) dialog.dismiss()
            toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
        })
    }

    private fun handleAdd() = binding.run {
        when (brandCurrent) {
            null -> {
                if (uri != null && edtName.text.toString() != "") {
                    if (!dialog.isShowing) dialog.show()
                    handleCreate()
                } else {
                    tvError.visibility = View.VISIBLE
                }
            }

            else -> {
                if (edtName.text.toString() != "") {
                    if (!dialog.isShowing) dialog.show()
                    handleUpdate()
                } else {
                    tvError.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun handleCreate() = binding.run {
        val entity = BrandEntity(
            name = edtName.text.toString(),
            des = edtDes.text.toString().trim(),
            pic = Utils.uriToBase64(this@AdminBrandActivity, uri!!)
        )
        viewModel.createBrand(
            brand = entity,
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
        val pic = if (uri != null) Utils.uriToBase64(this, uri!!) else brandCurrent!!.pic
        binding.run {
            val entity = BrandEntity(
                name = edtName.text.toString(),
                des = edtDes.text.toString().trim(),
                pic = pic
            )
            viewModel.updateBrand(
                brand = entity,
                idDocument = brandCurrent!!.id,
                handleSuccess = {
                    if (dialog.isShowing) dialog.dismiss()
                    toast(getString(R.string.update_brand))
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
            Utils.showRequirePermission(this) { finishAct(false) }
        }
    }

    private fun listenVM() {
        viewModel.currentBrand.observe(this) {
            if (it != null) {
                if (dialog.isShowing) dialog.dismiss()
                brandCurrent = it
                binding.run {
                    edtName.setText(brandCurrent!!.name)
                    edtDes.setText(brandCurrent!!.des)
                    Utils.displayBase64Image(brandCurrent!!.pic, imgPic)
                    tvDes.visibility = View.GONE
                    btnAdd.text = getString(R.string.update)
                    btnDelete.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun finishAct(isOK: Boolean) {
        viewModel.resetCurrentBrand()
        if (isOK) setResult(RESULT_OK)
        finish()
    }
}