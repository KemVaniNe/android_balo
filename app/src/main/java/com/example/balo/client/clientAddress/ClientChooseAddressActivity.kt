package com.example.balo.client.clientAddress

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.adapter.address.ClientAddressAdapter
import com.example.balo.data.model.AddressEntity
import com.example.balo.databinding.ActivityClientChooseAddressBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Pref

class ClientChooseAddressActivity : BaseActivity<ActivityClientChooseAddressBinding>() {
    private lateinit var viewModel: ClientAddressVM

    private var address: AddressEntity? = null

    private var list = mutableListOf<String>()

    private val adapterAddress by lazy {
        ClientAddressAdapter(list) { Pref.address = list[it] }
    }

    companion object {

        const val KEY_ADDRESS = "client_choose_address"
        fun newIntent(context: Context, response: List<String>): Intent {
            return Intent(context, ClientChooseAddressActivity::class.java).apply {
                putStringArrayListExtra(KEY_ADDRESS, ArrayList(response))
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityClientChooseAddressBinding =
        ActivityClientChooseAddressBinding.inflate(inflate)

    override fun initView() = binding.run {
        rvAddress.layoutManager = LinearLayoutManager(this@ClientChooseAddressActivity)
        rvAddress.adapter = adapterAddress
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[ClientAddressVM::class.java]
        address = viewModel.getAddressFromJson(this)
        val intent = intent
        val addresses = intent.getStringArrayListExtra(KEY_ADDRESS)
        if (intent.hasExtra(KEY_ADDRESS) && addresses != null) {
            list.run {
                clear()
                addAll(addresses)
            }
        } else {
            finish()
        }
    }

    override fun initListener() = binding.run {
        tvTitle.setOnClickListener { finish() }
        btnConfirm.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }
}