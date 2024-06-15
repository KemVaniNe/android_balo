package com.example.balo.admin.adminacount

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.balo.R
import com.example.balo.admin.adminmain.AdminMainActivity
import com.example.balo.admin.managerbill.ManagerBillActivity
import com.example.balo.admin.managerbrand.ManagerBrandActivity
import com.example.balo.admin.managerclient.ManagerClientActivity
import com.example.balo.admin.managerorder.ManagerOrderActivity
import com.example.balo.admin.managerproduct.ManagerProductActivity
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
        viewModel.getAccountBaseId {
            binding.clLoading.visibility = View.GONE
            toast(it)
        }
    }

    override fun initListener() = binding.run {
        tvInfo.setOnClickListener { handleUpdateInfo() }
        tvUpdatePass.setOnClickListener { handleUpdatePass() }
        tvLogOut.setOnClickListener { handleLogOut() }
        tvOrder.setOnClickListener { handleOrder() }
        tvClient.setOnClickListener { handleClient() }
        tvBill.setOnClickListener { handleBill() }
        tvProduct.setOnClickListener { handleProduct() }
        tvBrand.setOnClickListener { handleBrand() }
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
                    tvLogOut.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun handleBill() {
        if (isListener()) {
            context?.let { startActivity(Intent(it, ManagerBillActivity::class.java)) }
        }
    }

    private fun handleBrand() {
        if (isListener()) {
            context?.let { startActivity(ManagerBrandActivity.newIntent(it)) }
        }
    }

    private fun handleProduct() {
        if (isListener()) {
            context?.let { startActivity(ManagerProductActivity.newIntent(it)) }
        }
    }

    private fun handleLogOut() {
        if (isListener()) {
            context?.let { startActivity(Intent(it, LoginActivity::class.java)) }
            (context as AdminMainActivity).finishAct()
        }
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
            handleSuccess = { toast(getString(R.string.update_success)) },
            handleError = { error ->
                binding.clLoading.visibility = View.GONE
                toast("ERROR $error")
            })
    }

    private fun isListener(): Boolean {
        return binding.clLoading.visibility == View.GONE
    }
}