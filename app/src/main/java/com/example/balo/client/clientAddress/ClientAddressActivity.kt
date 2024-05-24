package com.example.balo.client.clientAddress

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.R
import com.example.balo.adapter.address.ClientAddressAdapter
import com.example.balo.adapter.address.ClientAddressMutiAdapter
import com.example.balo.client.clientAddress.newAddress.ClientAddressVM
import com.example.balo.client.clientAddress.newAddress.ClientNewAddressActivity
import com.example.balo.data.model.AddressSelect
import com.example.balo.data.model.UserEntity
import com.example.balo.databinding.ActivityClientAddressBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Pref
import com.google.gson.Gson

class ClientAddressActivity : BaseActivity<ActivityClientAddressBinding>() {

    private var isFromAccount = false

    private lateinit var viewModel: ClientAddressVM

    private lateinit var userEntity: UserEntity

    private val address = mutableListOf<String>()

    private val listChoose = mutableListOf<String>()

    private val addressSelect = mutableListOf<AddressSelect>()

    private val addressAdapter by lazy {
        ClientAddressAdapter(address) {
            Pref.address = address[it]
        }
    }

    private val addressSelectAdapter by lazy {
        ClientAddressMutiAdapter(addressSelect) { pos ->
            val addressCurrent = addressSelect[pos]
            if (addressCurrent.isSelected) {
                listChoose.remove(addressCurrent.address)
            } else {
                listChoose.add(addressCurrent.address)
            }
            addressSelect[pos].isSelected = !addressCurrent.isSelected
        }
    }

    companion object {

        const val CODE_NEW = 111
        const val KEY_ADDRESS = "client_address"
        const val KEY_TYPE = "client_type"
        const val TYPE_ACCOUNT = "account"
        const val TYPE_ORDER = "order"
        const val RESULT_ADDRESS = "result_address"
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
        rvAddress.adapter = if (isFromAccount) addressSelectAdapter else addressAdapter
        tvConfirm.text = getString(if (isFromAccount) R.string.delete else R.string.confirm)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        viewModel = ViewModelProvider(this)[ClientAddressVM::class.java]
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
            addressSelect.run {
                clear()
                userEntity.address.forEach {
                    add(AddressSelect(address = it))
                }
            }
            addressAdapter.notifyDataSetChanged()
            addressSelectAdapter.notifyDataSetChanged()
        } else {
            finish()
        }
    }

    override fun initListener() = binding.run {
        imgAdd.setOnClickListener {
            startActivityForResult(
                ClientNewAddressActivity.newIntent(
                    this@ClientAddressActivity, Gson().toJson(userEntity)
                ), CODE_NEW
            )
        }
        tvTitle.setOnClickListener { finishAct() }
        tvConfirm.setOnClickListener { handleConfirm() }
    }

    private fun handleConfirm() {
        if (isFromAccount) {
            delete()
        } else {
            setResult(RESULT_OK)
            finish()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun delete() {
        if (!dialog.isShowing) dialog.show()
        val newAddress = mutableListOf<String>()
        newAddress.addAll(userEntity.address)
        newAddress.removeAll(listChoose)
        userEntity.address = newAddress
        viewModel.updateInfo(userEntity, handleSuccess = {
            addressSelect.run {
                clear()
                userEntity.address.forEach {
                    add(AddressSelect(address = it))
                }
            }
            addressSelectAdapter.notifyDataSetChanged()
            if (dialog.isShowing) dialog.dismiss()
            toast(getString(R.string.delete_suceess))
        }, handleError = { error ->
            if (dialog.isShowing) dialog.dismiss()
            toast("ERROR : $error")
        })
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        finishAct()
        super.onBackPressed()
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
                addressSelect.run {
                    clear()
                    userEntity.address.forEach {
                        add(AddressSelect(address = it))
                    }
                }
                addressAdapter.notifyDataSetChanged()
                addressSelectAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun finishAct() {
        val resultIntent = Intent().apply {
            putExtra(RESULT_ADDRESS, Gson().toJson(userEntity))
        }
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}