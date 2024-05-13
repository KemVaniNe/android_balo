package com.example.balo.shareview.register

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.balo.R
import com.example.balo.data.model.UserEntity
import com.example.balo.databinding.ActivityRegisterBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Utils
import com.example.balo.utils.Utils.convertNumberVerify

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    private lateinit var viewModel: RegisterViewModel

    private var isSendCode = false

    private lateinit var dialog: AlertDialog

    private var phoneVerify: String = ""

    override fun viewBinding(inflate: LayoutInflater): ActivityRegisterBinding =
        ActivityRegisterBinding.inflate(inflate)

    override fun initView() {
        dialog = Utils.showProgressDialog(this)
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        llLogin.setOnClickListener { finish() }
        btnRegister.setOnClickListener { handleButton() }
        tvError.setOnClickListener { sendOTPAgain() }
    }


    private fun handleButton() {
        if (isFillAllInfo()) {
            if (isSendCode) {
                verifyOTP(binding.edtOTP.toString().trim())
            } else {
                registerAccount()
            }
        }
    }

    private fun registerAccount() {
        dialog.show()
        viewModel.registerAccount(
            activity = this, phone = convertNumberVerify(binding.edtEmail.text.toString()),
            handleExistPhone = {
                if (dialog.isShowing) dialog.dismiss()
                toast(getString(R.string.phone_exist))
            },
            handleVerify = { createAccount() },
            handleSend = { waitOTP() },
            handleFail = {
                if (dialog.isShowing) dialog.dismiss()
                toast(getString(R.string.try_again))
            }
        )
    }

    private fun verifyOTP(otp: String) {
        if (otp != "") {
            dialog.show()
            viewModel.verifyOtp(otp = otp, handleRightOTP = { createAccount() },
                handleFail = {
                    if (dialog.isShowing) dialog.dismiss()
                    showError(getString(R.string.otp_wrong))
                }
            )
        } else {
            showError(getString(R.string.enter_otp))
        }
    }

    private fun waitOTP() {
        if (dialog.isShowing) dialog.dismiss()
        isSendCode = true
        binding.edtOTP.visibility = View.VISIBLE
        showError(getString(R.string.send_otp_again))
    }

    private fun createAccount() = binding.run {
        if (phoneVerify == convertNumberVerify(edtEmail.text.toString()) || true) {
            val user = UserEntity(
                username = edtUsername.text.toString().trim(),
                phone = convertNumberVerify(edtEmail.text.toString()),
                password = edtPassword.text.toString().trim(),
                role = false
            )
            viewModel.createUser(
                user = user,
                handleSuccess = { registerSuccess() },
                handleFail = { error ->
                    if (dialog.isShowing) dialog.dismiss()
                    toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
                })
        }
        if (dialog.isShowing) dialog.dismiss()
    }

    private fun isFillAllInfo(): Boolean = binding.run {
        if (edtEmail.text.toString().trim() != "" && edtPassword.text.toString().trim() != "" &&
            edtUsername.text.toString().trim() != "" && edtConfirm.text.toString().trim() != ""
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

//    private fun handleRegister() = binding.run {
//        val user = UserEntity(
//            username = edtUsername.text.toString().trim(),
//            phone = edtEmail.text.toString().trim(),
//            password = edtPassword.text.toString().trim(),
//            role = false
//        )
//        dialog.show()
//        viewModel.createUser(user = user, handleSuccess = {
//            registerSuccess()
//        }, handleFail = { error ->
//            dialog.dismiss()
//            toast("Error: ${error}. Hãy thử lại!!")
//        })
//    }

    private fun handleRegister() = binding.run {
        if (isFillAllInfo()) {
            if (!isSendCode) {
                checkExistPhone(convertNumberVerify(edtEmail.text.toString()))
            } else {
                verifyOTP(edtOTP.text.toString().trim())
            }
        }
    }

    private fun checkExistPhone(phone: String) {
        viewModel.isPhoneRegister(phone = phone, isRegister = { isExist ->
            if (isExist) {
                toast(getString(R.string.phone_exist))
            } else {
                sendOTP(phone)
            }
        }, isError = {
            toast(getString(R.string.try_again))
        })
    }

    private fun sendOTPAgain() = binding.run {
        if (isSendCode) {
            edtOTP.setText("")
            sendOTP(convertNumberVerify(edtEmail.text.toString()))
        }
    }

    private fun sendOTP(phone: String) {
        dialog.show()
        phoneVerify = phone
        viewModel.sendOtp(this, phone, handleCompleted = {
            dialog.dismiss()
            verifySuccess()
        }, handleFail = {
            dialog.dismiss()
            showError(getString(R.string.otp_wrong))
        }, handleSend = {
            dialog.dismiss()
            waitOTP()
        })
    }


    private fun verifySuccess() = binding.run {
        dialog.show()
        val user = UserEntity(
            username = edtUsername.text.toString().trim(),
            phone = edtEmail.text.toString().trim(),
            password = Utils.hashPassword(edtPassword.text.toString().trim()),
            role = false
        )
        viewModel.createUser(user = user, handleSuccess = {
            registerSuccess()
        }, handleFail = { error ->
            dialog.dismiss()
            toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
        })
    }

    private fun showError(error: String) = binding.run {
        tvError.text = error
        tvError.visibility = View.VISIBLE
    }

    private fun registerSuccess() {
        if (dialog.isShowing) dialog.dismiss()
        toast(getString(R.string.register_success))
        finish()
    }

}