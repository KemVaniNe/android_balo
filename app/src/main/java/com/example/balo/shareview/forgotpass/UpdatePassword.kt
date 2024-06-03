package com.example.balo.shareview.forgotpass

import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.balo.R
import com.example.balo.databinding.ActivityUpdatePasswordBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Utils

class UpdatePassword : BaseActivity<ActivityUpdatePasswordBinding>() {

    private lateinit var viewModel: UpdatePassViewModel

    override fun viewBinding(inflate: LayoutInflater): ActivityUpdatePasswordBinding =
        ActivityUpdatePasswordBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[UpdatePassViewModel::class.java]
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        btnUpdate.setOnClickListener { handleUpdate() }
    }

    private fun handleUpdate() {
        if (isFillAllInfo()) {
            update()
        }
    }

    private fun update() = binding.run {
        if (!dialog.isShowing) dialog.show()
        viewModel.updatePassword(
            phone = Utils.convertNumberVerify(edtEmail.text.toString().trim()),
            newPassword = edtPassword.text.toString().trim(),
            handleSuccess = {
                toastError(getString(R.string.update_password_success))
                finish()
            },
            handleError = { toastError(it) }
        )
    }

    private fun toastError(mess: String) {
        if (dialog.isShowing) dialog.dismiss()
        toast(mess)
    }

    private fun isFillAllInfo(): Boolean = binding.run {
        if (edtEmail.text.toString().trim() != ""
            && edtPassword.text.toString().trim() != ""
            && edtConfirm.text.toString().trim() != ""
        ) {
            if (edtPassword.text.toString() == edtConfirm.text.toString()) {
                return true
            } else {
                showError(getString(R.string.password_confirm_wrong))
                return false
            }
        } else {
            showError(getString(R.string.please_fill_all))
            return false
        }
    }

    private fun showError(error: String) = binding.run {
        tvError.text = error
        tvError.visibility = View.VISIBLE
    }
}