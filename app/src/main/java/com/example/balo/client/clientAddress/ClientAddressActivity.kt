package com.example.balo.client.clientAddress

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import com.example.balo.admin.managerproduct.detail.AdminDetailProductActivity
import com.example.balo.data.model.UserEntity
import com.example.balo.databinding.ActivityClientAddressBinding
import com.example.balo.shareview.base.BaseActivity
import com.google.gson.Gson

class ClientAddressActivity : BaseActivity<ActivityClientAddressBinding>() {

    private var isFromAccount = false

    private lateinit var userEntity: UserEntity

    companion object {

        const val CODE_NEW = 111
        const val KEY_ADDRESS = "client_address"
        const val KEY_TYPE = "client_type"
        const val TYPE_ACCOUNT = "account"
        fun newIntent(context: Context, response: String, type: String): Intent {
            return Intent(context, ClientAddressActivity::class.java).apply {
                putExtra(KEY_ADDRESS, response)
                putExtra(KEY_TYPE, type)
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityClientAddressBinding =
        ActivityClientAddressBinding.inflate(inflate)

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() = binding.run {}

    override fun initData() {
        val intent = intent
        if (intent.hasExtra(KEY_ADDRESS) && intent.getStringExtra(KEY_ADDRESS) != null
            && intent.hasExtra(KEY_TYPE) && intent.getStringExtra(KEY_TYPE) != null
        ) {
            isFromAccount = intent.getStringExtra(KEY_TYPE) == TYPE_ACCOUNT
            userEntity = Gson().fromJson(intent.getStringExtra(KEY_ADDRESS), UserEntity::class.java)
        } else {
            finish()
        }
    }

    override fun initListener() = binding.run {
        imgAdd.setOnClickListener {
            startActivity(
                ClientNewAddressActivity.newIntent(
                    this@ClientAddressActivity, Gson().toJson(userEntity)
                )
            )
        }
    }
}