package com.example.balo.ui.forgotpass

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.balo.R
import com.example.balo.databinding.ActivityUpdatePasswordBinding
import com.example.balo.ui.base.BaseActivity
import com.example.balo.utils.Utils

class UpdatePassword : BaseActivity<ActivityUpdatePasswordBinding>() {

    private lateinit var dialog: AlertDialog

    private lateinit var viewModel: UpdatePassViewModel

    private var isSendCode = true
    override fun viewBinding(inflate: LayoutInflater): ActivityUpdatePasswordBinding =
        ActivityUpdatePasswordBinding.inflate(inflate)

    override fun initView() {
        dialog = Utils.showProgressDialog(this)
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[UpdatePassViewModel::class.java]
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        btnUpdate.setOnClickListener { handleRegister() }
    }

    private fun handleRegister() = binding.run {
        if (!isSendCode) {
            handleSendOTP()
        } else {
            updatePassword()
        }
    }

    private fun handleSendOTP() = binding.run {
        if (edtEmail.text.toString().trim() == "") {
            toast(getString(R.string.please_enter_phone))
        } else {
            //TODO
        }
    }

    private fun updatePassword() = binding.run {
        if (isFillAllInfo()) {
            if (!dialog.isShowing) dialog.show()
            viewModel.updatePassword(
                phone = Utils.convertNumberVerify(edtEmail.text.toString().trim()),
                newPassword = edtPassword.text.toString().trim(),
                handleSuccess = {
                    if (dialog.isShowing) dialog.dismiss()
                    toast(getString(R.string.update_password_success))
                    finish()
                },
                handleNotRegister = {
                    if (dialog.isShowing) dialog.dismiss()
                    toast(getString(R.string.account_not_register))
                },
                handleError = { error ->
                    if (dialog.isShowing) dialog.dismiss()
                    toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
                })
        }
    }

    private fun isFillAllInfo(): Boolean = binding.run {
        if (edtEmail.text.toString().trim() != "" && edtPassword.text.toString()
                .trim() != "" && edtConfirm.text.toString().trim() != ""
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