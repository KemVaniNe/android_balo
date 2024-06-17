package com.example.balo.client.clientAddress.newAddress

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.balo.R
import com.example.balo.client.clientAddress.ClientAddressVM
import com.example.balo.data.model.AddressEntity
import com.example.balo.data.model.UserEntity
import com.example.balo.databinding.ActivityClientNewAddressBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Pref
import com.google.gson.Gson

class ClientNewAddressActivity : BaseActivity<ActivityClientNewAddressBinding>() {
    private lateinit var viewModel: ClientAddressVM
    private lateinit var userEntity: UserEntity

    private var address: AddressEntity? = null
    private var cityChoose: String = ""
    private var qhChoose: String = ""
    private var txChoose: String = ""

    companion object {
        const val KEY_ADDRESS = "client_new_address"
        const val RESULT_ADDRESS = "result_address"
        const val REQUEST_CODE_CITY = 123
        const val REQUEST_CODE_QH = 124
        const val REQUEST_CODE_TX = 125
        fun newIntent(context: Context, response: String): Intent {
            return Intent(context, ClientNewAddressActivity::class.java).apply {
                putExtra(KEY_ADDRESS, response)
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityClientNewAddressBinding =
        ActivityClientNewAddressBinding.inflate(inflate)

    override fun initView() {}

    override fun initData() {
        viewModel = ViewModelProvider(this)[ClientAddressVM::class.java]
        val intent = intent
        if (intent.hasExtra(KEY_ADDRESS) && intent.getStringExtra(KEY_ADDRESS) != null) {
            userEntity = Gson().fromJson(intent.getStringExtra(KEY_ADDRESS), UserEntity::class.java)
            address = viewModel.getAddressFromJson(this)
        } else {
            finish()
        }
    }

    override fun initListener() = binding.run {
        tvTitle.setOnClickListener { finish() }
        tvConfirm.setOnClickListener { handleConfirm() }
        tvCity.setOnClickListener { handleCity() }
        tvQuanHuyen.setOnClickListener { handleQH() }
        tvPhuongXa.setOnClickListener { handleTX() }
    }

    private fun handleConfirm() {
        if (isAllFill()) {
            binding.clLoading.visibility = View.VISIBLE
            val newAddress = mutableListOf<String>()
            newAddress.addAll(userEntity.address)
            binding.run {
                newAddress.add("${edtName.text}, ${edtPhone.text}, ${edtAddress.text}, ${txChoose}, ${qhChoose}, ${cityChoose}")
            }
            userEntity.address = newAddress
            viewModel.updateInfo(userEntity, handleSuccess = {
                showToast(getString(R.string.address_success))
                val resultIntent = Intent().apply {
                    putExtra(RESULT_ADDRESS, Gson().toJson(userEntity))
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            }, handleError = { showToast(it) })
        }
    }

    private fun showToast(mess: String) {
        binding.clLoading.visibility = View.GONE
        toast(mess)
    }

    private fun handleCity() {
        Pref.address = cityChoose
        binding.tvErrorCity.visibility = View.INVISIBLE
        goToChoose(listCity(), REQUEST_CODE_CITY)
    }

    private fun handleQH() = binding.run {
        Pref.address = qhChoose
        tvErrorQH.visibility = View.INVISIBLE
        if (tvCity.text == getString(R.string.click_to_choose)) {
            tvErrorCity.visibility = View.VISIBLE
        } else {
            tvErrorCity.visibility = View.INVISIBLE
            goToChoose(listQH(), REQUEST_CODE_QH)
        }
    }

    private fun handleTX() = binding.run {
        Pref.address = txChoose
        if (tvQuanHuyen.text == getString(R.string.click_to_choose)) {
            tvErrorQH.visibility = View.VISIBLE
        } else {
            tvErrorQH.visibility = View.INVISIBLE
            goToChoose(listTX(), REQUEST_CODE_TX)
        }
    }

    private fun goToChoose(list: List<String>, requestCode: Int) {
        startActivityForResult(
            ClientChooseAddressActivity.newIntent(this, list), requestCode
        )
    }

    private fun isAllFill(): Boolean {
        binding.run {
            if (tvCity.text == getString(R.string.click_to_choose) ||
                tvQuanHuyen.text == getString(R.string.click_to_choose) ||
                tvPhuongXa.text == getString(R.string.click_to_choose) ||
                edtAddress.text.toString().trim() == "" ||
                edtName.text.toString().trim() == "" ||
                edtPhone.text.toString().trim() == ""
            ) {
                tvError.visibility = View.VISIBLE
                return false
            } else {
                tvError.visibility = View.GONE
                return true
            }
        }
    }

    private fun listCity(): List<String> {
        val list = mutableListOf<String>()
        address?.data?.forEach {
            list.add(it.tinhThanhPho)
        }
        return list
    }

    private fun listQH(): List<String> {
        val list = mutableListOf<String>()
        address?.data?.forEach {
            if (it.tinhThanhPho == cityChoose) {
                it.quanHuyenDS.forEach { qh ->
                    list.add(qh.quanHuyen)
                }
                return list
            }
        }
        return list
    }

    private fun listTX(): List<String> {
        val list = mutableListOf<String>()
        address?.data?.forEach {
            if (it.tinhThanhPho == cityChoose) {
                it.quanHuyenDS.forEach { qh ->
                    if (qh.quanHuyen == qhChoose) {
                        list.addAll(qh.phuongXaDS)
                        return list
                    }
                }
            }
        }
        return list
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            binding.run {
                if (requestCode == REQUEST_CODE_CITY) {
                    tvCity.text = Pref.address
                    if (Pref.address != cityChoose) {
                        tvQuanHuyen.text = getString(R.string.click_to_choose)
                        tvPhuongXa.text = getString(R.string.click_to_choose)
                        qhChoose = ""
                        txChoose = ""
                        cityChoose = Pref.address
                    }
                } else if (requestCode == REQUEST_CODE_QH) {
                    tvQuanHuyen.text = Pref.address
                    if (Pref.address != qhChoose) {
                        tvPhuongXa.text = getString(R.string.click_to_choose)
                        qhChoose = Pref.address
                        txChoose = ""
                    }
                } else if (requestCode == REQUEST_CODE_TX) {
                    tvPhuongXa.text = Pref.address
                    txChoose = Pref.address
                }
            }
        }
    }
}