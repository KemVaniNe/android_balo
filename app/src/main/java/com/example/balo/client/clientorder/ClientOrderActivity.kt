package com.example.balo.client.clientorder

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.example.balo.client.clientAddress.ClientAddressActivity
import com.example.balo.client.clientdetail.ClientDetailActivity
import com.example.balo.data.model.UserEntity
import com.example.balo.databinding.ActivityClientOrderBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Pref
import com.google.gson.Gson

class ClientOrderActivity : BaseActivity<ActivityClientOrderBinding>() {

    private var user: UserEntity? = null

    private lateinit var viewModel: ClientOrderVM

    companion object {

        const val CODE_ADDRESS = 111
        const val KEY_ORDER = "client_address"
        fun newIntent(context: Context, response: String): Intent {
            return Intent(context, ClientOrderActivity::class.java).apply {
                putExtra(KEY_ORDER, response)
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityClientOrderBinding =
        ActivityClientOrderBinding.inflate(inflate)

    override fun initView() {
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[ClientOrderVM::class.java]
        listenVM()
        val intent = intent
        if (intent.hasExtra(KEY_ORDER) && intent.getStringExtra(KEY_ORDER) != null) {
            updateInfo()
        } else {
            finish()
        }
    }

    private fun updateInfo() {
        if (!dialog.isShowing) dialog.show()
        viewModel.loadUser { error ->
            if (dialog.isShowing) dialog.dismiss()
            toast("error: $error")
            finish()
        }
    }

    override fun initListener() = binding.run {
        tvTitle.setOnClickListener { finish() }
        tvBuy.setOnClickListener { handleBuy() }
        tvAddress.setOnClickListener { handleAddress() }
    }

    private fun handleAddress() {
        startActivityForResult(
            ClientAddressActivity.newIntent(
                this, Gson().toJson(user), ClientAddressActivity.TYPE_ORDER
            ), CODE_ADDRESS
        )
    }

    private fun handleBuy() {

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == CODE_ADDRESS) {
            binding.tvAddress.text = Pref.address
        }
    }

    private fun listenVM() {
        viewModel.isLoading.observe(this) {
            if (dialog.isShowing) dialog.dismiss()
            user = viewModel.account
        }
    }
}