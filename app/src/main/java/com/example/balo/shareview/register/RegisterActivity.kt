package com.example.balo.shareview.register

import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.balo.R
import com.example.balo.data.model.UserEntity
import com.example.balo.databinding.ActivityRegisterBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Utils.convertNumberVerify

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    private lateinit var viewModel: RegisterViewModel

    override fun viewBinding(inflate: LayoutInflater): ActivityRegisterBinding =
        ActivityRegisterBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
    }

    override fun initListener() = binding.run {
        tvTitle.setOnClickListener { finish() }
        llLogin.setOnClickListener { finish() }
        btnRegister.setOnClickListener { handleRegister() }
    }

    private fun handleRegister() {
        if (isFillAllInfo()) {
            if (!dialog.isShowing) dialog.show()
            register()
        }
    }

    private fun register() = binding.run {
        val account = UserEntity(
            username = edtUsername.text.toString().trim(),
            phone = convertNumberVerify(edtPhone.text.toString()),
            password = edtPassword.text.toString().trim(),
            role = false,
            address = emptyList(),
            authcode = edtAuth.text.toString(),
            email = edtEmail.text.toString(),
        )
        viewModel.register(
            account = account,
            handleSuccess = {
                toastShow(getString(R.string.register_success))
                finish()
            },
            handleFail = { toastShow(it) }
        )
    }

    private fun toastShow(value: String) {
        if (dialog.isShowing) dialog.dismiss()
        toast(value)
    }

    private fun isFillAllInfo(): Boolean = binding.run {
        if (edtEmail.text.toString().trim() != "" && edtPassword.text.toString().trim() != "" &&
            edtUsername.text.toString().trim() != "" && edtConfirm.text.toString().trim() != "" &&
            edtAuth.text.toString().trim() != "" && edtPhone.text.toString().trim() != ""
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