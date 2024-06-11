package com.example.balo.client.clientcart

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.R
import com.example.balo.adapter.ClientCartAdapter
import com.example.balo.client.clientdetail.ClientDetailActivity
import com.example.balo.client.clientorder.ClientOrderActivity
import com.example.balo.data.model.CartEntity
import com.example.balo.data.model.OrderDetailEntity
import com.example.balo.databinding.ActivityClientCartBinding
import com.example.balo.shareview.base.BaseActivity
import com.example.balo.utils.Constants
import com.example.balo.utils.Option
import com.example.balo.utils.Pref
import com.example.balo.utils.Utils
import com.google.gson.Gson

class ClientCartActivity : BaseActivity<ActivityClientCartBinding>() {
    private lateinit var viewModel: ClientCartVM
    private var carts = mutableListOf<CartEntity>()
    private val chooses = mutableListOf<CartEntity>()

    companion object {
        const val REQUEST_CODE_ORDER = 123
    }

    private val cartAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ClientCartAdapter(
            carts,
            handleChoose = { handleCartItem(it) },
            handleChangeQuantity = { changeQuantityCartItem(it) },
            handleViewDetail = { goToDetail(carts[it].idBalo) })
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
                binding.run {
                    clLoading.visibility = View.GONE
                    llNone.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun goToDetail(id: String) {
        startActivity(ClientDetailActivity.newIntent(this, id))
    }

    private fun updateCart() {
        if (Pref.idUser != Constants.ID_GUEST) {
            binding.clLoading.visibility = View.VISIBLE
            viewModel.getCart(Pref.idUser) { showToast(it) }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleDelete() {
        Utils.showOption(this, Option.DELETE) {
            binding.clLoading.visibility = View.VISIBLE
            viewModel.deleteCart(chooses,
                handleSuccess = {
                    showToast(getString(R.string.delete_suceess))
                    chooses.forEach { carts.remove(it) }
                    chooses.clear()
                    cartAdapter.notifyDataSetChanged()
                    binding.run {
                        imgDelete.visibility = if (chooses.size > 0) View.VISIBLE else View.GONE
                        tvPrice.text = "0"
                    }
                },
                handleFail = { showToast(it) })
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
                    totalPriceSellCurrent = it.totalPriceSell
                )
                list.add(Gson().toJson(orderDetailEntity))
                listId.add(it.idCart)
            }
            startActivityForResult(
                ClientOrderActivity.newIntent(this, list, listId),
                REQUEST_CODE_ORDER
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteCart(cart: CartEntity) = binding.run {
        Utils.showOption(this@ClientCartActivity, Option.DELETE) {
            binding.clLoading.visibility = View.VISIBLE
            viewModel.deleteCart(
                ids = listOf(cart),
                handleSuccess = {
                    showToast(getString(R.string.delete_suceess))
                    carts.remove(cart)
                    cartAdapter.notifyDataSetChanged()
                    binding.run {}
                    imgDelete.visibility = if (chooses.size > 0) View.VISIBLE else View.GONE
                },
                handleFail = { showToast(it) })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateCart(pair: Pair<Int, Double>) {
        binding.clLoading.visibility = View.VISIBLE
        viewModel.updateCart(
            cart = carts[pair.first],
            handleSuccess = {
                binding.clLoading.visibility = View.GONE
                carts[pair.first].quantity = pair.second
                cartAdapter.notifyDataSetChanged()
            },
            handleFail = { showToast(it) })
    }

    private fun changeQuantityCartItem(pair: Pair<Int, Double>) {
        val newNum = pair.second
        if (newNum == 0.0) {
            deleteCart(carts[pair.first])
        } else {
            updateCart(pair)
        }
    }

    private fun handleCartItem(pair: Pair<Int, Boolean>) = binding.run {
        val cart = carts[pair.first]
        carts[pair.first].isSelect = pair.second
        if (pair.second) {
            chooses.add(cart)
        } else {
            chooses.remove(cart)
        }
        imgDelete.visibility = if (chooses.size > 0) View.VISIBLE else View.GONE
        val price = cart.price * cart.quantity
        var newPrice = 0.0
        if(pair.second) {
            newPrice = Utils.stringToDouble(tvPrice.text.toString()) + price
        } else {
            newPrice = Utils.stringToDouble(tvPrice.text.toString()) - price
        }
        tvPrice.text = newPrice.toString()
    }

    private fun showToast(notification: String) {
        binding.clLoading.visibility = View.GONE
        toast(notification)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_ORDER && resultCode == RESULT_OK) {
            binding.run {
                tvPrice.text = "0"
                imgDelete.visibility = View.GONE
                chooses.clear()
            }
            updateCart()
        }
    }
}