package com.example.balo.client.clientorderstatus.detail

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
import com.example.balo.databinding.ActivityClientOrderDetailBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Constants
import com.example.balo.utils.Constants.ORDER_CONFIRM
import com.example.balo.utils.DialogUtil
import com.example.balo.utils.Option
import com.example.balo.utils.Utils

class ClientOrderDetailActivity : BaseActivity<ActivityClientOrderDetailBinding>() {
    private lateinit var viewModel: ClientOrderDetailVM

    private var id = ""

    private var order: OrderEntity? = null

    private val orderDetail = mutableListOf<OrderDetailEntity>()

    private val detailAdapter by lazy {
        ShareOrderDetailAdapter(orderDetail, false, isUser = true,
            listener = { pos ->
                startActivity(ClientDetailActivity.newIntent(this, orderDetail[pos].idBalo))
            },
            listenerRate = { pos ->
                DialogUtil.showRating(this) { rate ->
                    order!!.detail[pos].rate = rate.take(1)
                    order!!.detail[pos].comment = rate.drop(1).trim()
                    if (!dialog.isShowing) dialog.show()
                    viewModel.updateRate(
                        order!!,
                        order!!.detail[pos],
                        handleSuccess = {
                            showToast("Đánh giá thành công")
                            updateOrder()
                        },
                        handleFail = { showToast("ERROR: $it") })
                }
            })
    }

    companion object {

        const val KEY_DETAIL = "detail"
        fun newIntent(context: Context, response: String): Intent {
            return Intent(context, ClientOrderDetailActivity::class.java).apply {
                putExtra(KEY_DETAIL, response)
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityClientOrderDetailBinding =
        ActivityClientOrderDetailBinding.inflate(inflate)

    override fun initView() = binding.run {
        rvOrder.layoutManager = LinearLayoutManager(this@ClientOrderDetailActivity)
        rvOrder.adapter = detailAdapter
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[ClientOrderDetailVM::class.java]
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
        tvCancel.setOnClickListener { handleCancel() }
    }

    private fun handleCancel() {
        Utils.showOption(this, Option.CANCEL) {
            if (!dialog.isShowing) dialog.show()
            viewModel.cancelOrder(
                id,
                handleSuccess = { updateProduct() },
                handleFail = { showToast(it) })
        }
    }

    private fun updateProduct() {
        if (!dialog.isShowing) dialog.show()
        viewModel.cancelOrderByUser(
            order!!,
            handleSuccess = {
                showToast("Hủy đơn thành công")
                setResult(RESULT_OK)
                finish()
            },
            handleFail = { showToast(it) })
    }

    private fun showToast(mess: String) {
        if (dialog.isShowing) dialog.dismiss()
        toast(mess)
    }

    private fun updateOrder() {
        if (!dialog.isShowing) dialog.show()
        viewModel.getDetail(id) { showToast(it) }
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
                detailAdapter.updateRate(order!!.statusOrder == Constants.ORDER_COMPLETE)
                binding.run {
                    if (it.statusOrder == ORDER_CONFIRM) tvCancel.visibility = View.VISIBLE
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