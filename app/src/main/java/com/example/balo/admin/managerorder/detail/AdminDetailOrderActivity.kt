package com.example.balo.admin.managerorder.detail

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.adapter.order.ShareOrderDetailAdapter
import com.example.balo.client.clientdetail.ClientDetailActivity
import com.example.balo.data.model.OrderDetailEntity
import com.example.balo.data.model.OrderEntity
import com.example.balo.databinding.ActivityAdminDetailOrderBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Constants
import com.example.balo.utils.Utils

class AdminDetailOrderActivity : BaseActivity<ActivityAdminDetailOrderBinding>() {
    private lateinit var viewModel: AdminDetailOrderVM

    private var id = ""

    private var order: OrderEntity? = null

    private val orderDetail = mutableListOf<OrderDetailEntity>()

    private lateinit var detailAdapter: ShareOrderDetailAdapter

    companion object {

        const val KEY_DETAIL = "detail"
        fun newIntent(context: Context, response: String): Intent {
            return Intent(context, AdminDetailOrderActivity::class.java).apply {
                putExtra(KEY_DETAIL, response)
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityAdminDetailOrderBinding =
        ActivityAdminDetailOrderBinding.inflate(inflate)

    override fun initView() = binding.run {
        rvOrder.layoutManager = LinearLayoutManager(this@AdminDetailOrderActivity)
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[AdminDetailOrderVM::class.java]
        listenVM()
        val intent = intent
        if (intent.hasExtra(KEY_DETAIL) && intent.getStringExtra(KEY_DETAIL) != null) {
            id = intent.getStringExtra(KEY_DETAIL)!!
            updateOrder()
        } else {
            finish()
        }
    }

    override fun initListener() = binding.run {
        tvTitle.setOnClickListener { finish() }
        tvConfirm.setOnClickListener { handleConfirm() }
    }

    private fun handleConfirm() {
        if (!dialog.isShowing) dialog.show()
        newStatusOrder()
        viewModel.updateOrder(order!!, handleSuccess = {
            showToast("Thanh đổi trạng thái thành công")
            setResult(RESULT_OK)
            finish()
        }, handleFail = { showToast("ERROR $it") })
    }

    private fun newStatusOrder() {
        when (order!!.statusOrder) {
            Constants.ORDER_CONFIRM -> order!!.statusOrder = Constants.ORDER_WAIT_SHIP
            Constants.ORDER_WAIT_SHIP -> order!!.statusOrder = Constants.ORDER_SHIP
            Constants.ORDER_SHIP -> order!!.statusOrder = Constants.ORDER_COMPLETE
        }
    }

    private fun showToast(mess: String) {
        if (dialog.isShowing) dialog.dismiss()
        toast(mess)
    }

    private fun updateOrder() {
        if (!dialog.isShowing) dialog.show()
        viewModel.getDetail(id) { showToast("ERROR $it") }
    }

    private fun listenVM() {
        viewModel.detail.observe(this) {
            if (it != null) {
                if (dialog.isShowing) dialog.dismiss()
                order = it
                orderDetail.run {
                    clear()
                    addAll(order!!.detail)
                }
                detailAdapter = ShareOrderDetailAdapter(
                    orderDetail,
                    order!!.statusOrder == Constants.ORDER_COMPLETE,
                    isUser = false,
                    listener = { pos ->
                        startActivity(ClientDetailActivity.newIntent(this, orderDetail[pos].idBalo))
                    },
                    listenerRate = {})
                binding.run {
                    rvOrder.adapter = detailAdapter
                    if (it.statusOrder == Constants.ORDER_CANCEL
                        || it.statusOrder == Constants.ORDER_COMPLETE
                    ) {
                        tvConfirm.visibility = View.GONE
                    }
                    tvStatus.text = it.statusOrder
                    tvPriceShip.text = it.priceShip
                    tvTotalOrder.text = it.totalPrice
                    val price = Utils.stringToInt(it.totalPrice) + Utils.stringToInt(it.priceShip)
                    tvTotalPrice.text = price.toString()
                    tvAddress.text = it.address
                }
            }
        }
    }
}