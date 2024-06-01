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
        viewModel.updateAccount { e ->
            binding.clLoading.visibility = View.GONE
            toast("ERROR $e")
        }
    }

    override fun initListener() = binding.run {
        tvInfo.setOnClickListener { handleUpdateInfo() }
        tvUpdatePass.setOnClickListener { handleUpdatePass() }
        tvLogOut.setOnClickListener { handleLogOut() }
        tvOrder.setOnClickListener { handleOrder() }
        tvClient.setOnClickListener { handleClient() }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdminAccountBinding = FragmentAdminAccountBinding.inflate(inflater)

    private fun listenVM() {
        viewModel.account.observe(this) {
            if (it != null) {
                user = it
                binding.run {
                    clLoading.visibility = View.GONE
                    tvUsername.text = it.username
                    tvPhone.text = it.phone
                    Utils.displayUserAvatar(it.pic, imgAvatar)
                    tvLogOut.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun handleLogOut() {
        context?.let { startActivity(Intent(it, LoginActivity::class.java)) }
        (context as AdminMainActivity).finishAct()
    }

    private fun handleUpdatePass() {
        if (isListener()) {
            context?.let { Utils.bottomUpdatePass(it, user!!) { updateInfo() } }
        }
    }

    private fun handleOrder() {
        if (isListener()) {
            context?.let { startActivity(Intent(it, ManagerOrderActivity::class.java)) }
        }
    }

    private fun handleClient() {
        if (isListener()) {
            context?.let { startActivity(Intent(it, ManagerClientActivity::class.java)) }
        }
    }

    private fun handleUpdateInfo() {
        if (isListener()) {
            context?.let { Utils.bottomUpdateInfo(it, user!!) { updateInfo() } }
        }
    }

    private fun updateInfo() {
        binding.clLoading.visibility = View.VISIBLE
        viewModel.updateInfo(user!!,
            handleSuccess = { toast(getString(R.string.update_password_success)) },
            handleError = { error ->
                binding.clLoading.visibility = View.GONE
                toast("ERROR $error")
            })
    }

    private fun isListener(): Boolean {
        return binding.clLoading.visibility == View.GONE
    }
}