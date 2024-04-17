package com.example.balo.ui.login

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.example.balo.databinding.ActivityRegisterBinding
import com.example.balo.ui.base.BaseActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    private lateinit var auth: FirebaseAuth

    private var isSendCode = false

    private var otp = ""

    override fun viewBinding(inflate: LayoutInflater): ActivityRegisterBinding =
        ActivityRegisterBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
        auth = FirebaseAuth.getInstance()
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
                    val credential =
                        PhoneAuthProvider.getCredential(otp, edtOTP.text.toString().trim())
//                    if (edtOTP.text.toString().trim() == otp) {
//                        resultVerify(1)
//                    } else {
//                        resultVerify(2)
//                    }
                    signInWithPhoneAuthCredential(credential)
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
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
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

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            resultVerify(type = 1)
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            resultVerify(type = 2)
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            otp = p0
            resultVerify(type = 3)
            Log.d("VANVAN", "CODE SEND: $p0")
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

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    resultVerify(1)
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    }
                    resultVerify(2)
                }
            }
    }

}