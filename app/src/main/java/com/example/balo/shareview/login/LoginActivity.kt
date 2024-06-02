package com.example.balo.shareview.login

import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.example.balo.R
import com.example.balo.data.model.UserEntity
import com.example.balo.databinding.ActivityLoginBinding
import com.example.balo.admin.adminmain.AdminMainActivity
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.shareview.forgotpass.UpdatePassword
import com.example.balo.shareview.register.RegisterActivity
import com.example.balo.client.clientmain.ClientMainActivity
import com.example.balo.utils.Constants
import com.example.balo.utils.Pref
import com.example.balo.utils.Utils

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private lateinit var viewModel: LoginViewModel
    override fun viewBinding(inflate: LayoutInflater): ActivityLoginBinding =
        ActivityLoginBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        Pref.idUser = Constants.ID_GUEST
    }

    override fun initListener() = binding.run {
        llRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
        btnLogin.setOnClickListener { handleLogin() }
        tvForgotPass.setOnClickListener {
            startActivity(Intent(this@LoginActivity, UpdatePassword::class.java))
        }
        tvLater.setOnClickListener {
            Pref.idUser = Constants.ID_GUEST
            startActivity(Intent(this@LoginActivity, ClientMainActivity::class.java))
        }
    }

    private fun handleLogin() {
        if (isFillAllInfo()) {
            login()
        } else {
            toast(getString(R.string.please_fill_login))
        }
    }

    private fun login() = binding.run {
        if (!dialog.isShowing) dialog.show()
        viewModel.login(
            phone = Utils.convertNumberVerify(edtEmail.text.toString().trim()),
            password = edtPassword.text.toString().trim(),
            handleSuccess = { goToNext(it) },
            handleFail = {
                if (dialog.isShowing) dialog.dismiss()
                toast(it)
            }
        )
    }

    private fun goToNext(account: UserEntity) {
        if (dialog.isShowing) dialog.dismiss()
        Pref.idUser = account.id
        if (account.role == Constants.ROLE_AD) {
            startActivity(Intent(this, AdminMainActivity::class.java))
        } else {
            startActivity(Intent(this, ClientMainActivity::class.java))
        }
        finish()
    }

    private fun isFillAllInfo(): Boolean = binding.run {
        return !(edtEmail.text.toString().trim() == "" || edtPassword.text.toString().trim() == "")
    }
}