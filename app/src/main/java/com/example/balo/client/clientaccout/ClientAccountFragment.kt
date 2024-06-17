package com.example.balo.client.clientaccout

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.balo.R
import com.example.balo.client.chatbot.ChatbotActivity
import com.example.balo.client.clientAddress.ClientAddressActivity
import com.example.balo.client.clientcart.ClientCartActivity
import com.example.balo.client.clientmain.ClientMainActivity
import com.example.balo.client.clientorderstatus.ClientOrderStatusActivity
import com.example.balo.data.model.UserEntity
import com.example.balo.databinding.FragmentAccountBinding
import com.example.balo.shareview.base.BaseFragment
import com.example.balo.shareview.login.LoginActivity
import com.example.balo.utils.Constants
import com.example.balo.utils.Pref
import com.example.balo.utils.Utils

class ClientAccountFragment : BaseFragment<FragmentAccountBinding>() {
    private lateinit var viewModel: ClientAccountVM

    private var user: UserEntity? = null

    companion object {
        const val REQUEST_ADDRESS = 123
        const val REQUEST_LOGIN = 124
    }

    override fun initView() {
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[ClientAccountVM::class.java]
        listenVM()
        if (Pref.idUser != Constants.ID_GUEST) {
            viewModel.updateAccount {
                binding.clLoading.visibility = View.GONE
                toast(it)
            }
        } else {
            binding.clLoading.visibility = View.GONE
        }
    }

    override fun initListener() = binding.run {
        tvAddress.setOnClickListener { handleAddress() }
        tvCart.setOnClickListener { handleCart() }
        tvContact.setOnClickListener {handleContact() }
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
        context?.let {
            startActivityForResult(
                ClientAddressActivity.newIntent(it, ClientAddressActivity.TYPE_ACCOUNT),
                REQUEST_ADDRESS
            )
        }
    }

    private fun handleContact() {
        context?.let { startActivity(Intent(it, ChatbotActivity::class.java)) }
    }

    private fun handleCart() {
        context?.let { startActivity(Intent(it, ClientCartActivity::class.java)) }
    }

    private fun handleOrder() {
        context?.let { startActivity(Intent(it, ClientOrderStatusActivity::class.java)) }
    }

    private fun handleLogout() {
        if (Pref.idUser == Constants.ID_GUEST) {
            context?.let {
                startActivityForResult(
                    LoginActivity.newIntent(it, LoginActivity.KEY_LOGIN),
                    REQUEST_LOGIN
                )
            }
        } else {
            context?.let { startActivity(Intent(it, LoginActivity::class.java)) }
            (context as ClientMainActivity).finishAct()
        }
    }

    private fun listenVM() {
        viewModel.account.observe(this) {
            if (it != null) {
                user = it
                binding.run {
                    clLoading.visibility = View.GONE
                    tvUsername.text = it.username
                    tvPhone.text = it.phone
                    tvLogOut.text = getString(R.string.log_out)
                    ll1.visibility = View.VISIBLE
                    ll2.visibility = View.VISIBLE
                    ll3.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun handleUpdatePass() {
        context?.let { Utils.bottomUpdatePass(it, user!!) { updateInfo() } }
    }

    private fun handleUpdateInfo() {
        context?.let { Utils.bottomUpdateInfo(it, user!!) { updateInfo() } }
    }

    private fun updateInfo() {
        binding.clLoading.visibility = View.VISIBLE
        viewModel.updateInfo(user!!,
            handleSuccess = { showToast(getString(R.string.update_success)) },
            handleError = { showToast(it) })
    }

    private fun showToast(mess: String) {
        binding.clLoading.visibility = View.GONE
        toast(mess)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGIN && resultCode == Activity.RESULT_OK) {
            context?.let {
                startActivity(Intent(it, ClientMainActivity::class.java))
                (context as ClientMainActivity).finishAct()
            }
        }
    }
}