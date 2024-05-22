package com.example.balo.client.clientAddress

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.adapter.address.ClientAddressAdapter
import com.example.balo.data.model.UserEntity
import com.example.balo.databinding.ActivityClientAddressBinding
import com.example.balo.shareview.base.BaseActivity
import com.google.gson.Gson

class ClientAddressActivity : BaseActivity<ActivityClientAddressBinding>() {

    private var isFromAccount = false

    private lateinit var userEntity: UserEntity

    private val address = mutableListOf<String>()

    private val addressAdapter by lazy {
        ClientAddressAdapter(address) {
        }
    }

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
    override fun initView() = binding.run {
        rvAddress.layoutManager = LinearLayoutManager(this@ClientAddressActivity)
        rvAddress.adapter = addressAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        val intent = intent
        if (intent.hasExtra(KEY_ADDRESS) && intent.getStringExtra(KEY_ADDRESS) != null
            && intent.hasExtra(KEY_TYPE) && intent.getStringExtra(KEY_TYPE) != null
        ) {
            isFromAccount = intent.getStringExtra(KEY_TYPE) == TYPE_ACCOUNT
            userEntity = Gson().fromJson(intent.getStringExtra(KEY_ADDRESS), UserEntity::class.java)
            address.run {
                clear()
                addAll(userEntity.address)
            }
            addressAdapter.notifyDataSetChanged()
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
        tvTitle.setOnClickListener { finish() }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == CODE_NEW) {
            val newUser = data?.getStringExtra(ClientNewAddressActivity.RESULT_ADDRESS)
            if (newUser != null) {
                userEntity = Gson().fromJson(newUser, UserEntity::class.java)
                address.run {
                    clear()
                    addAll(userEntity.address)
                }
                addressAdapter.notifyDataSetChanged()
            }
        }
    }
}