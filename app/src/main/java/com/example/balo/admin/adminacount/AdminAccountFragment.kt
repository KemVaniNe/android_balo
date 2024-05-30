package com.example.balo.admin.adminacount

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.balo.R
import com.example.balo.admin.adminmain.AdminMainActivity
import com.example.balo.admin.managerclient.ManagerClientActivity
import com.example.balo.admin.managerorder.ManagerOrderActivity
import com.example.balo.data.model.UserEntity
import com.example.balo.databinding.FragmentAdminAccountBinding
import com.example.balo.shareview.base.BaseFragment
import com.example.balo.shareview.login.LoginActivity
import com.example.balo.utils.Utils

class AdminAccountFragment : BaseFragment<FragmentAdminAccountBinding>() {
    private lateinit var viewModel: AdminAccountVM

    private var user: UserEntity? = null
    override fun initView() {
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[AdminAccountVM::class.java]
        listenVM()
        viewModel.updateAccount { e -> toast("ERROR $e") }
    }

    override fun initListener() = binding.run {
        tvInfo.setOnClickListener { handleUpdateInfo() }
        tvUpdatePass.setOnClickListener { handleUpdatePass() }
        tvLogOut.setOnClickListener {
            context?.let { startActivity(Intent(it, LoginActivity::class.java)) }
            (context as AdminMainActivity).finishAct()
        }
        tvOrder.setOnClickListener {
            context?.let { startActivity(Intent(it, ManagerOrderActivity::class.java)) }
        }
        tvClient.setOnClickListener {
            context?.let { startActivity(Intent(it, ManagerClientActivity::class.java)) }
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdminAccountBinding = FragmentAdminAccountBinding.inflate(inflater)

    private fun listenVM() {
        viewModel.account.observe(this) {
            if (it != null) {
                if (dialog.isShowing) dialog.dismiss()
                user = it
                binding.run {
                    tvUsername.text = it.username
                    tvPhone.text = it.phone
                    Utils.displayUserAvatar(it.pic, imgAvatar)
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
        if (!dialog.isShowing) dialog.show()
        viewModel.updateInfo(user!!,
            handleSuccess = { toast(getString(R.string.update_password_success)) },
            handleError = { error ->
                if (dialog.isShowing) dialog.dismiss()
                toast("ERROR $error")
            })
    }

}