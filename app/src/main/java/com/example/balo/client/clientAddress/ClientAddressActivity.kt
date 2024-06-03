package com.example.balo.client.clientAddress

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.R
import com.example.balo.adapter.address.ClientAddressAdapter
import com.example.balo.adapter.address.ClientAddressMutiAdapter
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

    private var userEntity: UserEntity? = null

    private val address = mutableListOf<String>()

    private val listChoose = mutableListOf<String>()

    private val addressSelect = mutableListOf<AddressSelect>()

    private val addressAdapter by lazy {
        ClientAddressAdapter(address) { Pref.address = address[it] }
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
        const val KEY_TYPE = "client_type"
        const val TYPE_ACCOUNT = "account"
        const val TYPE_ORDER = "order"
        fun newIntent(context: Context, type: String): Intent {
            return Intent(context, ClientAddressActivity::class.java).apply {
                putExtra(KEY_TYPE, type)
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityClientAddressBinding =
        ActivityClientAddressBinding.inflate(inflate)

    override fun initView() = binding.run {
        rvAddress.layoutManager = LinearLayoutManager(this@ClientAddressActivity)
        rvAddress.adapter = if (isFromAccount) addressSelectAdapter else addressAdapter
        tvConfirm.text = getString(if (isFromAccount) R.string.delete else R.string.confirm)
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[ClientAddressVM::class.java]
        val intent = intent
        val receive = intent.getStringExtra(KEY_TYPE)
        if (intent.hasExtra(KEY_TYPE) && receive != null) {
            isFromAccount = intent.getStringExtra(KEY_TYPE) == TYPE_ACCOUNT
            updateInfo()
            listenVM()
        } else {
            finish()
        }
    }

    override fun initListener() = binding.run {
        imgAdd.setOnClickListener { goToNewAddress() }
        tvTitle.setOnClickListener { finish() }
        tvConfirm.setOnClickListener { handleConfirm() }
    }

    private fun goToNewAddress() {
        startActivityForResult(
            ClientNewAddressActivity.newIntent(this, Gson().toJson(userEntity)), CODE_NEW
        )
    }

    private fun updateInfo() {
        binding.clLoading.visibility = View.VISIBLE
        viewModel.getUser { showToast(it) }
    }

    private fun listenVM() {
        viewModel.account.observe(this) { user ->
            if (user != null) {
                binding.clLoading.visibility = View.GONE
                userEntity = user
                updateAdapter()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateAdapter() {
        address.run {
            clear()
            addAll(userEntity!!.address)
        }
        addressSelect.run {
            clear()
            userEntity!!.address.forEach { add(AddressSelect(address = it)) }
        }
        addressAdapter.notifyDataSetChanged()
        addressSelectAdapter.notifyDataSetChanged()
    }

    private fun showToast(mess: String) {
        binding.clLoading.visibility = View.GONE
        toast(mess)
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
        binding.clLoading.visibility = View.VISIBLE
        val newAddress = mutableListOf<String>()
        newAddress.addAll(userEntity!!.address)
        newAddress.removeAll(listChoose)
        userEntity!!.address = newAddress

        viewModel.updateInfo(
            userEntity!!,
            handleSuccess = {
                updateAdapter()
                showToast(getString(R.string.delete_suceess))
            },
            handleError = { showToast(it) })
    }

    @SuppressLint("NotifyDataSetChanged")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == CODE_NEW) {
            val newUser = data?.getStringExtra(ClientNewAddressActivity.RESULT_ADDRESS)
            if (newUser != null) {
                userEntity = Gson().fromJson(newUser, UserEntity::class.java)
                updateAdapter()
            }
        }
    }

}