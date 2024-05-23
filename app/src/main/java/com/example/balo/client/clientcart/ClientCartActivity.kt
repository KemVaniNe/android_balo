package com.example.balo.client.clientcart

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.R
import com.example.balo.adapter.ClientCartAdapter
import com.example.balo.client.clientorder.ClientOrderActivity
import com.example.balo.data.model.CartEntity
import com.example.balo.data.model.OrderDetailEntity
import com.example.balo.databinding.ActivityClientCartBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Constants
import com.example.balo.utils.Option
import com.example.balo.utils.Pref
import com.example.balo.utils.Utils
import com.example.balo.utils.Utils.calculate
import com.example.balo.utils.Utils.stringToInt
import com.google.gson.Gson

class ClientCartActivity : BaseActivity<ActivityClientCartBinding>() {

    private var carts = mutableListOf<CartEntity>()

    private lateinit var viewModel: ClientCartVM

    private val chooses = mutableListOf<CartEntity>()
    
    private val REQUEST_CODE_ORDER = 123

    private val cartAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ClientCartAdapter(carts,
            listener = { pos -> handleCartItem(pos) },
            listenChange = { pair -> changeQuantityCartItem(pair) })
    }

    override fun viewBinding(inflate: LayoutInflater): ActivityClientCartBinding =
        ActivityClientCartBinding.inflate(inflate)

    override fun initView() = binding.rvFavorite.run {
        layoutManager = LinearLayoutManager(context)
        adapter = cartAdapter
    }

    override fun initData() {
        viewModel = ViewModelProvider(this)[ClientCartVM::class.java]
        listenVM()
        updateCart()
    }

    override fun initListener() = binding.run {
        tvTitle.setOnClickListener { finish() }
        tvBuy.setOnClickListener { handleBuy() }
        imgDelete.setOnClickListener { handleDelete() }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listenVM() {
        viewModel.carts.observe(this) {
            if (it != null) {
                carts.run {
                    clear()
                    addAll(it)
                }
                cartAdapter.notifyDataSetChanged()
                if (dialog.isShowing) dialog.dismiss()
                binding.llNone.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun updateCart() {
        if (Pref.idUser != Constants.ID_GUEST) {
            if (!dialog.isShowing) dialog.show()
            viewModel.getCart(Pref.idUser) { error -> toast("ERROR: $error") }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleDelete() {
        Utils.showOption(this, Option.DELETE) {
            if (!dialog.isShowing) dialog.show()
            viewModel.deleteCart(chooses, handleSuccess = {
                showToast(getString(R.string.delete_suceess))
                chooses.forEach { carts.remove(it) }
                cartAdapter.notifyDataSetChanged()
                binding.imgDelete.visibility = if (chooses.size > 0) View.VISIBLE else View.GONE
            }, handleFail = { error ->
                showToast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
            })
        }
    }

    private fun handleBuy() {
        if (chooses.isEmpty()) {
            toast(getString(R.string.you_not_choose_cart))
        } else {
            val list = mutableListOf<String>()
            val listId = mutableListOf<String>()
            chooses.forEach {
                val orderDetailEntity = OrderDetailEntity(
                    idBalo = it.idBalo,
                    nameBalo = it.nameBalo,
                    quantity = it.quantity,
                    price = it.price,
                )
                list.add(Gson().toJson(orderDetailEntity))
                listId.add(it.idCart)
            }
            startActivityForResult(ClientOrderActivity.newIntent(this, list,listId), REQUEST_CODE_ORDER)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteCart(cart: CartEntity) {
        Utils.showOption(this, Option.DELETE) {
            if (!dialog.isShowing) dialog.show()
            viewModel.deleteCart(listOf(cart), handleSuccess = {
                showToast(getString(R.string.delete_suceess))
                carts.remove(cart)
                cartAdapter.notifyDataSetChanged()
                binding.imgDelete.visibility = if (chooses.size > 0) View.VISIBLE else View.GONE
            }, handleFail = { error ->
                showToast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
            })
        }
    }

    private fun updateCart(pair: Pair<Int, String>) {
        if (!dialog.isShowing) dialog.show()
        viewModel.updateCart(carts[pair.first], handleSuccess = {
            if (dialog.isShowing) dialog.dismiss()
            carts[pair.first].quantity = pair.second
            cartAdapter.notifyDataSetChanged()
        }, handleFail = { error ->
            showToast("${getString(R.string.error)}: ${error}. ${getString(R.string.try_again)}")
        })
    }

    private fun changeQuantityCartItem(pair: Pair<Int, String>) {
        val newNum = stringToInt(pair.second)
        if (newNum == 0) {
            deleteCart(carts[pair.first])
        } else {
            updateCart(pair)
        }
    }

    private fun handleCartItem(pos: Int) = binding.run {
        carts[pos].isSelect = !carts[pos].isSelect
        val cart = carts[pos]
        if (cart.isSelect) {
            chooses.add(cart)
        } else {
            chooses.remove(cart)
        }
        imgDelete.visibility = if (chooses.size > 0) View.VISIBLE else View.GONE
        val price = stringToInt(cart.price) * stringToInt(cart.quantity)
        tvPrice.text = calculate(tvPrice.text.toString(), price.toString(), !cart.isSelect)
    }

    private fun showToast(notification: String) {
        if (dialog.isShowing) dialog.dismiss()
        toast(notification)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE_ORDER && resultCode == RESULT_OK) {
            updateCart()
        }
    }
}