package com.example.balo.client.clientorder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.adapter.product.ClientProductOrderAdapter
import com.example.balo.client.clientAddress.ClientAddressActivity
import com.example.balo.data.model.OrderDetail
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

    private val order = mutableListOf<OrderDetail>()

    private val orderAdapter by lazy { ClientProductOrderAdapter(order) }

    companion object {

        const val CODE_ADDRESS = 111
        const val KEY_ORDER = "client_address"
        fun newIntent(context: Context, response: List<String>): Intent {
            return Intent(context, ClientOrderActivity::class.java).apply {
                putStringArrayListExtra(KEY_ORDER, ArrayList(response))
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

    @SuppressLint("NotifyDataSetChanged")
    override fun initData() {
        viewModel = ViewModelProvider(this)[ClientOrderVM::class.java]
        listenVM()
        val intent = intent
        val receive = intent.getStringArrayListExtra(KEY_ORDER)
        if (intent.hasExtra(KEY_ORDER) && receive != null) {
            updateInfo()
            receive.forEach {
                order.add(Gson().fromJson(it, OrderDetail::class.java))
            }
            orderAdapter.notifyDataSetChanged()
        } else {
            finish()
        }
    }

    private fun updateInfo() {
        if (!dialog.isShowing) dialog.show()
        viewModel.loadUser { error ->
            if (dialog.isShowing) dialog.dismiss()
            toast("error: $error")
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
            ClientAddressActivity.newIntent(
                this, Gson().toJson(user), ClientAddressActivity.TYPE_ORDER
            ), CODE_ADDRESS
        )
    }

    private fun handleBuy() {

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == CODE_ADDRESS) {
            binding.tvAddress.text = Pref.address
        }
    }

    private fun listenVM() {
        viewModel.isLoading.observe(this) {
            if (dialog.isShowing) dialog.dismiss()
            user = viewModel.account
        }
    }

    private fun setPrice() = binding.run {
        var price = 0
        val ship = Constants.INIT_SHIP + order.size * Constants.MAX_SHIP
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