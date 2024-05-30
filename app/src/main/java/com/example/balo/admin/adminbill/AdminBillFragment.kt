package com.example.balo.admin.adminbill

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.adapter.AdminBillAdapter
import com.example.balo.admin.managerorder.detail.AdminDetailOrderActivity
import com.example.balo.data.model.BillEntity
import com.example.balo.databinding.FragmentAdminOrderBinding
import com.example.balo.shareview.base.BaseFragment

class AdminBillFragment : BaseFragment<FragmentAdminOrderBinding>() {

    private lateinit var viewModel: AdminBillVM

    private val bills = mutableListOf<BillEntity>()

    private val billAdapter by lazy {
        AdminBillAdapter(bills) { pos ->
            context?.let {
                startActivity(AdminDetailOrderActivity.newIntent(it, bills[pos].idOrder))
            }
        }
    }

    override fun initView() = binding.run {
        rvBill.layoutManager = LinearLayoutManager(context)
        rvBill.adapter = billAdapter
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[AdminBillVM::class.java]
        listenVM()
        getBill()
    }

    override fun initListener() {
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAdminOrderBinding = FragmentAdminOrderBinding.inflate(inflater)

    private fun getBill() {
        if (!dialog.isShowing) dialog.show()
        viewModel.getBills {
            if (dialog.isShowing) dialog.dismiss()
            toast(it)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listenVM() {
        viewModel.bills.observe(this) {
            if (dialog.isShowing) dialog.dismiss()
            binding.run {
                if (it.isEmpty()) {
                    llNone.visibility = View.VISIBLE
                    rvBill.visibility = View.GONE
                } else {
                    llNone.visibility = View.GONE
                    rvBill.visibility = View.VISIBLE
                    bills.run {
                        clear()
                        addAll(it)
                    }
                    billAdapter.notifyDataSetChanged()
                    val total = "${it.size} hóa đơn"
                    tvTotal.text = total
                }
            }

        }
    }
}