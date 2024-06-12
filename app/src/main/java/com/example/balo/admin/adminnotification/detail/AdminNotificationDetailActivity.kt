package com.example.balo.admin.adminnotification.detail

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.balo.admin.adminnotification.AdminNotificationVM
import com.example.balo.admin.managerorder.detail.AdminDetailOrderActivity
import com.example.balo.data.model.NotificationEntity
import com.example.balo.databinding.ActivityAdminNotificationDetailBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Utils
import com.google.gson.Gson

class AdminNotificationDetailActivity : BaseActivity<ActivityAdminNotificationDetailBinding>() {

    private lateinit var viewModel: AdminNotificationVM

    private var notification: NotificationEntity? = null

    companion object {

        const val KEY_DETAIL = "detail"
        const val REQUEST_CODE_DETAIL = 123
        fun newIntent(context: Context, notification: String): Intent {
            return Intent(context, AdminNotificationDetailActivity::class.java).apply {
                putExtra(KEY_DETAIL, notification)
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityAdminNotificationDetailBinding =
        ActivityAdminNotificationDetailBinding.inflate(inflate)

    override fun initView() = binding.run {
        tvDate.text = notification!!.datatime
        tvDes.text = notification!!.notification
        val txtOrder = "MÃ ĐƠN HÀNG: ${notification!!.idOrder}"
        tvOrder.text = txtOrder
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[AdminNotificationVM::class.java]
        listenVM()
        val intent = intent
        val gson = intent.getStringExtra(KEY_DETAIL)
        if (intent.hasExtra(KEY_DETAIL) && gson != null) {
            notification = Gson().fromJson(gson, NotificationEntity::class.java)
            updateStatusNotification()
            getOrder()
        } else {
            finish()
        }
    }

    override fun initListener() = binding.run {
        tvTitle.setOnClickListener { finish() }
        clOrder.setOnClickListener { goToDetailOrder() }
    }

    private fun listenVM() {
        viewModel.order.observe(this) {
            if (it != null) {
                val detail = it.detail.firstOrNull()
                binding.run {
                    clOrder.visibility = View.VISIBLE
                    if (detail != null) {
                        tvName.text = detail.nameBalo
                        tvPrice.text = detail.price.toString()
                        Utils.displayBase64Image(detail.picProduct, imgPic)
                        tvQuantity.text = detail.quantity.toString()
                    }
                    val total = "${it.detail.size} sản phẩm"
                    tvTotalProduct.text = total
                    tvStatus.text = it.statusOrder
                    val price = it.totalPrice + it.priceShip
                    tvTotalPrice.text = price.toString()
                    clLoading.visibility = View.GONE
                }
            }
        }
    }

    private fun getOrder() {
        binding.clLoading.visibility = View.VISIBLE
        viewModel.getOrderById(notification!!.idOrder) {
            binding.clLoading.visibility = View.GONE
            toast(it)
        }
    }

    private fun updateStatusNotification() {
        viewModel.updateStatusNotification(notification!!) { toast(it) }
    }

    private fun goToDetailOrder() {
        startActivityForResult(
            AdminDetailOrderActivity.newIntent(this, notification!!.idOrder), REQUEST_CODE_DETAIL
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DETAIL) {
            getOrder()
        }
    }
}