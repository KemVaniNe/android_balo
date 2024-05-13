package com.example.balo.shareview.login

import android.app.AlertDialog
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
import com.example.balo.client.clientmain.ClientMainActivity.Companion.EMPTY_ACCOUNT
import com.example.balo.utils.Utils
import com.google.gson.Gson

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private lateinit var dialog: AlertDialog

    private lateinit var viewModel: LoginViewModel
    override fun viewBinding(inflate: LayoutInflater): ActivityLoginBinding =
        ActivityLoginBinding.inflate(inflate)

    override fun initView() {
        dialog = Utils.showProgressDialog(this)
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
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
            startActivity(ClientMainActivity.newIntent(this@LoginActivity, EMPTY_ACCOUNT))
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
        viewModel.login(phoneNumber = Utils.convertNumberVerify(edtEmail.text.toString().trim()),
            password = edtPassword.text.toString().trim(),
            handleSuccess = { account ->
                if (dialog.isShowing) dialog.dismiss()
                goToNext(account)
            },
            handleFail = {
                if (dialog.isShowing) dialog.dismiss()
                toast(getString(R.string.account_not_true))
            },
            handleError = { error ->
                if (dialog.isShowing) dialog.dismiss()
                toast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
            })

    }

    private fun goToNext(account: UserEntity) {
        if (account.role) {
            startActivity(AdminMainActivity.newIntent(this, Gson().toJson(account)))
        } else {
            startActivity(ClientMainActivity.newIntent(this, Gson().toJson(account)))
        }
        finish()
    }

    private fun isFillAllInfo(): Boolean = binding.run {
        return !(edtEmail.text.toString().trim() == "" || edtPassword.text.toString().trim() == "")
    }

}