package com.example.balo.client.clientorder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.R
import com.example.balo.adapter.product.ClientProductOrderAdapter
import com.example.balo.client.clientAddress.ClientAddressActivity
import com.example.balo.data.model.OrderDetailEntity
import com.example.balo.data.model.OrderEntity
import com.example.balo.data.model.UserEntity
import com.example.balo.databinding.ActivityClientOrderBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Constants
import com.example.balo.utils.Pref
import com.example.balo.utils.Utils
import com.google.gson.Gson

class ClientOrderActivity : BaseActivity<ActivityClientOrderBinding>() {

    private var user: UserEntity? = null

    private lateinit var viewModel: ClientOrderVM

    private val order = mutableListOf<OrderDetailEntity>()

    private var cart: List<String> = emptyList()

    private val orderAdapter by lazy { ClientProductOrderAdapter(order) }

    companion object {

        const val CODE_ADDRESS = 111
        const val KEY_ORDER = "product"
        const val KEY_CART = "cart"
        fun newIntent(context: Context, response: List<String>, cart: List<String>): Intent {
            return Intent(context, ClientOrderActivity::class.java).apply {
                putStringArrayListExtra(KEY_ORDER, ArrayList(response))
                putStringArrayListExtra(KEY_CART, ArrayList(cart))
            }
        }
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityClientOrderBinding =
        ActivityClientOrderBinding.inflate(inflate)

    override fun initView() = binding.run {
        rvOrder.layoutManager = LinearLayoutManager(this@ClientOrderActivity)
        rvOrder.adapter = orderAdapter
        setPrice()
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[ClientOrderVM::class.java]
        listenVM()
        val intent = intent
        val receive = intent.getStringArrayListExtra(KEY_ORDER)
        val cartReive = intent.getStringArrayListExtra(KEY_CART)
        if (intent.hasExtra(KEY_ORDER) && intent.hasExtra(KEY_CART) && receive != null && cartReive != null) {
            receive.forEach {
                order.add(Gson().fromJson(it, OrderDetailEntity::class.java))
            }
            updateData()
            cart = cartReive
        } else {
            finish()
        }
    }

    private fun updateData() {
        binding.clLoading.visibility = View.VISIBLE
        viewModel.loadData(order) {
            showToast(it)
            finish()
        }
    }

    override fun initListener() = binding.run {
        tvTitle.setOnClickListener { finish() }
        tvBuy.setOnClickListener { handleBuy() }
        tvAddress.setOnClickListener { handleAddress() }
    }

    private fun handleAddress() {
        startActivityForResult(
            ClientAddressActivity.newIntent(this, ClientAddressActivity.TYPE_ORDER), CODE_ADDRESS
        )
    }

    private fun handleBuy() {
        if (binding.tvAddress.text != getString(R.string.click_to_choose)) {
            binding.clLoading.visibility = View.VISIBLE
            val orderEntity = OrderEntity(
                iduser = user!!.id,
                date =  Utils.getToDay(),
                totalPrice = binding.tvTotalOrder.text.toString(),
                address = binding.tvAddress.text.toString(),
                priceShip = binding.tvPriceShip.text.toString(),
                statusOrder = Constants.ORDER_CONFIRM,
                detail = order
            )
            viewModel.createOrder(
                order = orderEntity,
                handleSuccess = { deleteCart() },
                handleFail = { showToast(it) })
        } else {
            toast("Bạn phải chọn địa chỉ")
        }
    }

    private fun showToast(mess: String) {
        binding.clLoading.visibility = View.GONE
        toast(mess)
    }

    private fun deleteCart() {
        viewModel.deleteCards(
            cart,
            handleSuccess = {
                showToast("Đặt hàng thành công")
                setResult(RESULT_OK)
                finish()
            },
            handleFail = { showToast(it) })
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == CODE_ADDRESS) {
            binding.tvAddress.text = Pref.address
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listenVM() {
        viewModel.account.observe(this) {
            user = it
        }

        viewModel.orderDetail.observe(this) {
            order.run {
                clear()
                addAll(it)
            }
            orderAdapter.notifyDataSetChanged()
            binding.clLoading.visibility = View.GONE
        }
    }

    private fun setPrice() = binding.run {
        var price = 0
        val ship = Constants.INIT_SHIP + order.size * Constants.STEP_SHIP
        val endShip = if (ship > Constants.MAX_SHIP) Constants.MAX_SHIP else ship
        order.forEach {
            price += Utils.stringToInt(it.price) * Utils.stringToInt(it.quantity)
        }
        val total = price + ship
        tvTotalOrder.text = price.toString()
        tvPriceShip.text = endShip.toString()
        tvTotalPrice.text = total.toString()
        tvPrice.text = total.toString()
    }
}