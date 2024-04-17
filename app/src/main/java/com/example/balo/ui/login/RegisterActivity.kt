package com.example.balo.ui.login

import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.balo.databinding.ActivityRegisterBinding
import com.example.balo.ui.base.BaseActivity

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    private lateinit var viewModel: LoginViewModel

    private var isSendCode = false

    override fun viewBinding(inflate: LayoutInflater): ActivityRegisterBinding =
        ActivityRegisterBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun initListener() = binding.run {
        imgBack.setOnClickListener { finish() }
        llLogin.setOnClickListener { finish() }
        btnRegister.setOnClickListener { handleRegister() }
        tvError.setOnClickListener { sendOTPAgain() }
    }

    private fun handleRegister() = binding.run {
        if (isFillAllInfo()) {
            if (isSendCode) {
                if (edtOTP.text.toString().trim() != "") {
                    viewModel.verifyOtp(edtOTP.text.toString().trim(), handleRightOTP = {
                        resultVerify(1)
                    }, handleFail = {
                        resultVerify(2)
                    })
                } else {
                    showError("Vui lòng nhập mã xác thực")
                }
            } else {
                onclickVerify(edtEmail.text.toString().trim())
            }
        }
    }

    private fun sendOTPAgain() = binding.run {
        if (isSendCode) {
            edtOTP.setText("")
            onclickVerify(edtOTP.text.toString().trim())
        }
    }

    private fun onclickVerify(phone: String) {
        viewModel.sendOtp(this, phone, handleCompleted = {
            resultVerify(type = 1)
        }, handleFail = {
            resultVerify(type = 2)
        }, handleSend = {
            resultVerify(type = 3)
        })
    }

    private fun isFillAllInfo(): Boolean = binding.run {
        if (edtEmail.text.toString().trim() != "" && edtPassword.text.toString().trim() != "" &&
            edtUsername.text.toString().trim() != "" && edtConfirm.text.toString().trim() != ""
        ) {
            if (edtPassword.text.toString() == edtConfirm.text.toString()) {
                return true
            } else {
                showError("Mật khẩu xác nhận không đúng")
                return false
            }
        } else {
            showError("Vui lòng điền đầy đủ thông tin")
            return false
        }
    }

    private fun resultVerify(type: Int) = binding.run {
        when (type) {
            1 -> {
                toast("VERIFIED SUCCESS. HEHEHE")
                tvError.visibility = View.GONE
            }

            2 -> {
                showError("Mã xác thực không đúng!! Vui lòng thử lại")
            }

            3 -> {
                isSendCode = true
                edtOTP.visibility = View.VISIBLE
                showError("Gửi lại mã xác nhận")
            }

            else -> {
                toast("THỬ LẠI!!!!")
            }
        }
    }

    private fun showError(error: String) = binding.run {
        tvError.text = error
        tvError.visibility = View.VISIBLE
    }

}