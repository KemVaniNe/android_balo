package com.example.balo.shareview.base

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseActivity<B : ViewBinding> : FragmentActivity() {
    protected lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = viewBinding(LayoutInflater.from(this))
        setContentView(binding.root)
        initData()
        initView()
        initListener()
    }

    protected val viewException: Array<View>?
        protected get() = null

    protected abstract fun viewBinding(inflate: LayoutInflater): B
    protected abstract fun initView()
    protected abstract fun initData()
    protected abstract fun initListener()

    fun toast(content: String?) {
        lifecycleScope.launch(Dispatchers.Main) {
            if (!TextUtils.isEmpty(content)) Toast.makeText(
                this@BaseActivity,
                content,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}