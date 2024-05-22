package com.example.balo.client.clientAddress

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import com.example.balo.R
import com.example.balo.data.model.UserEntity
import com.example.balo.databinding.ActivityClientNewAddressBinding
import com.example.balo.shareview.base.BaseActivity
import com.google.gson.Gson

class ClientNewAddressActivity : BaseActivity<ActivityClientNewAddressBinding>() {

    private lateinit var userEntity: UserEntity

    companion object {

        const val KEY_ADDRESS = "client_new_address"
        fun newIntent(context: Context, response: String): Intent {
            return Intent(context, ClientNewAddressActivity::class.java).apply {
                putExtra(KEY_ADDRESS, response)
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityClientNewAddressBinding =
        ActivityClientNewAddressBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
        val intent = intent
        if (intent.hasExtra(KEY_ADDRESS) && intent.getStringExtra(KEY_ADDRESS) != null) {
            userEntity = Gson().fromJson(intent.getStringExtra(KEY_ADDRESS), UserEntity::class.java)
        } else {
            finish()
        }
    }

    override fun initListener() = binding.run {
        tvTitle.setOnClickListener { finish() }
        tvConfirm.setOnClickListener { handleConfirm() }
    }

    private fun handleConfirm() {
        if (isAllFill()) {

        } else {

        }
    }

    private fun isAllFill(): Boolean {
        binding.run {
            if (tvCity.text == getString(R.string.click_to_choose) ||
                tvQuanHuyen.text == getString(R.string.click_to_choose) ||
                tvPhuongXa.text == getString(R.string.click_to_choose) ||
                edtAddress.text.toString().trim() == ""
            ) {
                tvError.visibility = View.VISIBLE
                return false
            } else {
                tvError.visibility = View.GONE
                return true
            }
        }
    }

}