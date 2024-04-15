package com.example.balo.ui.login

import android.content.Intent
import android.view.LayoutInflater
import com.example.balo.databinding.ActivityLoginBinding
import com.example.balo.ui.base.BaseActivity
import com.example.balo.ui.user.main.MainActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    override fun viewBinding(inflate: LayoutInflater): ActivityLoginBinding =
        ActivityLoginBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initListener() = binding.run {
        llRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
        btnLogin.setOnClickListener { handleLogin() }
        tvForgotPass.setOnClickListener {
            //TODO
        }
    }

    private fun handleLogin() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}