package com.example.balo.admin.managerorder

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.R
import com.example.balo.adapter.order.ClientOrderAdapter
import com.example.balo.admin.managerorder.detail.AdminDetailOrderActivity
import com.example.balo.data.model.OrderEntity
import com.example.balo.databinding.ActivityManagerOrderBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Constants

class ManagerOrderActivity : BaseActivity<ActivityManagerOrderBinding>() {
    private lateinit var viewModel: ManagerOrderVM
    private val listConfirm = mutableListOf<OrderEntity>()
    private val listPackage = mutableListOf<OrderEntity>()
    private val listShip = mutableListOf<OrderEntity>()
    private val listSuccess = mutableListOf<OrderEntity>()
    private val listCancel = mutableListOf<OrderEntity>()

    private val confirmAdapter by lazy {
        ClientOrderAdapter(listConfirm) { goToDetail(listConfirm[it].id) }
    }

    private val packageAdapter by lazy {
        ClientOrderAdapter(listPackage) { goToDetail(listPackage[it].id) }
    }

    private val shipAdapter by lazy {
        ClientOrderAdapter(listShip) { goToDetail(listShip[it].id) }
    }

    private val successAdapter by lazy {
        ClientOrderAdapter(listSuccess) { goToDetail(listSuccess[it].id) }
    }

    private val cancelAdapter by lazy {
        ClientOrderAdapter(listCancel) { goToDetail(listCancel[it].id) }
    }

    companion object {
        const val TYPE_CONFIRM = 1
        const val TYPE_PACKAGE = 2
        const val TYPE_SHIP = 3
        const val TYPE_SUCCESS = 4
        const val TYPE_CANCEL = 5
        const val REQUEST_CODE_DETAIL = 123
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityManagerOrderBinding =
        ActivityManagerOrderBinding.inflate(inflate)

    override fun initView() = binding.rvOrders.run {
        layoutManager = LinearLayoutManager(this@ManagerOrderActivity)
        adapter = confirmAdapter
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[ManagerOrderVM::class.java]
        listenVM()
        updateOrder()
    }

    override fun initListener() = binding.run {
        tabConfirm.setOnClickListener { listenTab(TYPE_CONFIRM) }
        tabShip.setOnClickListener { listenTab(TYPE_SHIP) }
        tabPackage.setOnClickListener { listenTab(TYPE_PACKAGE) }
        tabSuccess.setOnClickListener { listenTab(TYPE_SUCCESS) }
        tabCancelOrder.setOnClickListener { listenTab(TYPE_CANCEL) }
        tvTitle.setOnClickListener { finish() }
    }

    private fun listenTab(type: Int) = binding.run {
        if (binding.clLoading.visibility == View.GONE) {
            changeViewTab(tabConfirm, type == TYPE_CONFIRM)
            changeViewTab(tabPackage, type == TYPE_PACKAGE)
            changeViewTab(tabShip, type == TYPE_SHIP)
            changeViewTab(tabSuccess, type == TYPE_SUCCESS)
            changeViewTab(tabCancelOrder, type == TYPE_CANCEL)
            val adapterCurrent = when (type) {
                TYPE_CONFIRM -> confirmAdapter
                TYPE_PACKAGE -> packageAdapter
                TYPE_SHIP -> shipAdapter
                TYPE_SUCCESS -> successAdapter
                TYPE_CANCEL -> cancelAdapter
                else -> confirmAdapter
            }
            rvOrders.adapter = adapterCurrent
        }
    }

    private fun updateOrder() {
        binding.clLoading.visibility = View.VISIBLE
        viewModel.getOrders { error ->
            binding.clLoading.visibility = View.GONE
            toast("ERROR: $error")
            finish()
        }
    }

    private fun changeViewTab(view: TextView, isSelect: Boolean) {
        view.setBackgroundResource(if (isSelect) R.drawable.bg_btn else R.drawable.bg_option)
    }

    private fun goToDetail(id: String) {
        startActivityForResult(
            AdminDetailOrderActivity.newIntent(this, id), REQUEST_CODE_DETAIL
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listenVM() {
        viewModel.orders.observe(this) {
            binding.clLoading.visibility = View.GONE
            if (it.isNotEmpty()) {
                listCancel.clear()
                listConfirm.clear()
                listShip.clear()
                listPackage.clear()
                listSuccess.clear()
                it.forEach {
                    when (it.statusOrder) {
                        Constants.ORDER_CANCEL -> listCancel.add(it)
                        Constants.ORDER_CONFIRM -> listConfirm.add(it)
                        Constants.ORDER_COMPLETE -> listSuccess.add(it)
                        Constants.ORDER_SHIP -> listShip.add(it)
                        Constants.ORDER_WAIT_SHIP -> listPackage.add(it)
                    }
                }
                confirmAdapter.notifyDataSetChanged()
                cancelAdapter.notifyDataSetChanged()
                successAdapter.notifyDataSetChanged()
                shipAdapter.notifyDataSetChanged()
                packageAdapter.notifyDataSetChanged()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_DETAIL) {
            updateOrder()
        }
    }

}