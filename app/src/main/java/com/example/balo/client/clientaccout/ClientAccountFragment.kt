package com.example.balo.client.clientaccout

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.balo.R
import com.example.balo.client.clientcart.ClientCartActivity
import com.example.balo.client.clientmain.ClientMainActivity
import com.example.balo.data.model.UserEntity
import com.example.balo.databinding.FragmentAccountBinding
import com.example.balo.shareview.base.BaseFragment
import com.example.balo.shareview.login.LoginActivity
import com.example.balo.utils.Utils

class ClientAccountFragment : BaseFragment<FragmentAccountBinding>() {

    private lateinit var dialog: AlertDialog

    private lateinit var viewModel: ClientAccountVM

    private var user: UserEntity? = null

    override fun initView() {
    }

    override fun initData() {
        context?.let {
            dialog = Utils.showProgressDialog(it)
        }
        viewModel = ViewModelProvider(this)[ClientAccountVM::class.java]
        listenVM()
        viewModel.updateAccount { e -> toast("ERROR $e") }
    }

    override fun initListener() = binding.run {
        tvAddress.setOnClickListener {
            //TODO
        }
        tvCart.setOnClickListener {
            context?.let { startActivity(Intent(it, ClientCartActivity::class.java)) }
        }
        tvContact.setOnClickListener {
            //TODO
        }
        tvOrder.setOnClickListener {
            //TODO
        }
        tvInfo.setOnClickListener {
            //TODO
        }
        tvUpdatePass.setOnClickListener {
            context?.let {
                Utils.bottomUpdatePass(it, user!!) {
                    if (!dialog.isShowing) dialog.show()
                    viewModel.updatePassword(user!!,
                        handleSuccess = { toast(getString(R.string.update_password_success)) },
                        handleError = { error ->
                            if (dialog.isShowing) dialog.dismiss()
                            toast("ERROR $error")
                        })
                }
            }
        }
        tvLogOut.setOnClickListener {
            context?.let { startActivity(Intent(it, LoginActivity::class.java)) }
            (context as ClientMainActivity).finishAct()
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAccountBinding = FragmentAccountBinding.inflate(inflater)

    private fun listenVM() {
        viewModel.account.observe(this) {
            if (it != null) {
                if (dialog.isShowing) dialog.dismiss()
                user = it
                binding.run {
                    tvUsername.text = it.username
                    tvPhone.text = it.phone
                    tvLogOut.visibility = View.VISIBLE
                    llInfo.visibility = View.VISIBLE
                }
            }
        }
    }
}