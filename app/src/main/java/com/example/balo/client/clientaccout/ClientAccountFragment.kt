package com.example.balo.client.clientaccout

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.balo.R
import com.example.balo.client.clientAddress.ClientAddressActivity
import com.example.balo.client.clientcart.ClientCartActivity
import com.example.balo.client.clientmain.ClientMainActivity
import com.example.balo.client.clientorderstatus.ClientOrderStatusActivity
import com.example.balo.data.model.UserEntity
import com.example.balo.databinding.FragmentAccountBinding
import com.example.balo.shareview.base.BaseFragment
import com.example.balo.shareview.login.LoginActivity
import com.example.balo.utils.Utils

class ClientAccountFragment : BaseFragment<FragmentAccountBinding>() {
    private lateinit var viewModel: ClientAccountVM

    private var user: UserEntity? = null

    val REQUEST_ADDRESS = 123
    override fun initView() {
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[ClientAccountVM::class.java]
        listenVM()
        viewModel.updateAccount { e -> toast("ERROR $e") }
    }

    override fun initListener() = binding.run {
        tvAddress.setOnClickListener { handleAddress() }
        tvCart.setOnClickListener { handleCart() }
        tvContact.setOnClickListener {
            //TODO
        }
        tvOrder.setOnClickListener { handleOrder() }
        tvInfo.setOnClickListener { handleUpdateInfo() }
        tvUpdatePass.setOnClickListener { handleUpdatePass() }
        tvLogOut.setOnClickListener { handleLogout() }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAccountBinding = FragmentAccountBinding.inflate(inflater)

    private fun handleAddress() {
        if (!isLoading()) {
            context?.let {
                startActivityForResult(
                    ClientAddressActivity.newIntent(it, ClientAddressActivity.TYPE_ACCOUNT),
                    REQUEST_ADDRESS
                )
            }
        }
    }

    private fun handleCart() {
        if (!isLoading()) {
            context?.let { startActivity(Intent(it, ClientCartActivity::class.java)) }

        }
    }

    private fun handleOrder() {
        if (!isLoading()) {
            context?.let { startActivity(Intent(it, ClientOrderStatusActivity::class.java)) }
        }
    }

    private fun handleLogout() {
        context?.let { startActivity(Intent(it, LoginActivity::class.java)) }
        (context as ClientMainActivity).finishAct()
    }

    private fun listenVM() {
        viewModel.account.observe(this) {
            if (it != null) {
                user = it
                binding.run {
                    clLoading.visibility = View.GONE
                    tvUsername.text = it.username
                    tvPhone.text = it.phone
                    tvLogOut.visibility = View.VISIBLE
                    llInfo.visibility = View.VISIBLE
                    imgAvatar.visibility = View.VISIBLE
                    Utils.displayUserAvatar(it.pic, imgAvatar)
                }
            }
        }
    }

    private fun handleUpdatePass() {
        if (!isLoading()) {
            context?.let { Utils.bottomUpdatePass(it, user!!) { updateInfo() } }
        }
    }

    private fun handleUpdateInfo() {
        if (!isLoading()) {
            context?.let { Utils.bottomUpdateInfo(it, user!!) { updateInfo() } }
        }
    }

    private fun updateInfo() {
        binding.clLoading.visibility = View.VISIBLE
        viewModel.updateInfo(user!!,
            handleSuccess = { showToast(getString(R.string.update_password_success)) },
            handleError = { showToast(it) })
    }

    private fun showToast(mess: String) {
        binding.clLoading.visibility = View.GONE
        toast(mess)
    }

    private fun isLoading(): Boolean {
        return binding.clLoading.visibility == View.VISIBLE
    }
}