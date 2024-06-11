package com.example.balo.shareview.login

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
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

    companion object {

        const val KEY_LOGIN = "login"
        fun newIntent(context: Context, response: String): Intent {
            return Intent(context, LoginActivity::class.java).apply {
                putExtra(KEY_LOGIN, response)
            }
        }
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        Pref.idUser = Constants.ID_GUEST
        val intent = intent
        if (intent.hasExtra(KEY_LOGIN) && intent.getStringExtra(KEY_LOGIN) != null) {
            binding.tvBack.visibility = View.VISIBLE
        } else {
            binding.tvBack.visibility = View.GONE
        }
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
            if (tvBack.visibility == View.GONE) {
                startActivity(Intent(this@LoginActivity, ClientMainActivity::class.java))
                finish()
            } else {
                finish()
            }
        }
        tvBack.setOnClickListener { finish() }
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
        if (binding.tvBack.visibility == View.GONE) {
            if (account.role == Constants.ROLE_AD) {
                startActivity(Intent(this, AdminMainActivity::class.java))
            } else {
                startActivity(Intent(this, ClientMainActivity::class.java))
            }
        }
        setResult(RESULT_OK)
        finish()
    }

    private fun isFillAllInfo(): Boolean = binding.run {
        return !(edtEmail.text.toString().trim() == "" || edtPassword.text.toString().trim() == "")
    }
}