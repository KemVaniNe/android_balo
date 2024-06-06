package com.example.balo.utils

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.balo.R
import com.example.balo.data.model.BaloEntity
import com.example.balo.databinding.DialogChooseSellBinding
import com.example.balo.databinding.DialogConfirmBinding
import com.example.balo.databinding.DialogQuantityChooseBinding
import com.example.balo.databinding.DialogRateBinding
import com.example.balo.databinding.DialogTimeBinding
import java.util.Calendar

enum class Option {
    EXIT, DELETE, CANCEL
}

object DialogUtil {

    private fun showStart(
        img2: ImageView,
        img3: ImageView,
        img4: ImageView,
        img5: ImageView,
        num: Int
    ) {
        img2.setImageResource(if (num < 2) R.drawable.ic_start_not_choose else R.drawable.ic_star)
        img3.setImageResource(if (num < 3) R.drawable.ic_start_not_choose else R.drawable.ic_star)
        img4.setImageResource(if (num < 4) R.drawable.ic_start_not_choose else R.drawable.ic_star)
        img5.setImageResource(if (num < 5) R.drawable.ic_start_not_choose else R.drawable.ic_star)
    }

    fun showTimeDialog(context: Context, listener: (String) -> Unit) {
        var timeChoose = Utils.getToDay()
        val dialog = Dialog(context)
        val dialogBinding = DialogTimeBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(context, R.color.transparent))
        )
        dialogBinding.run {
            val calendar = Calendar.getInstance()
            datePicker.init(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ) { _, year, month, dayOfMonth ->
                timeChoose = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            }
            btnAll.setOnClickListener {
                listener.invoke(Constants.ALL_DATE)
                dialog.dismiss()
            }
            btnAdd.setOnClickListener {
                listener.invoke(timeChoose)
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    fun showRating(context: Context, listener: (String) -> Unit) {
        var currentRate = 5
        val dialog = Dialog(context)
        val dialogBinding = DialogRateBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(context, R.color.transparent))
        )
        dialogBinding.run {
            img1.setOnClickListener {
                currentRate = 1
                showStart(img2, img3, img4, img5, currentRate)
            }
            img2.setOnClickListener {
                currentRate = 2
                showStart(img2, img3, img4, img5, currentRate)
            }

            img3.setOnClickListener {
                currentRate = 3
                showStart(img2, img3, img4, img5, currentRate)
            }

            img4.setOnClickListener {
                currentRate = 4
                showStart(img2, img3, img4, img5, currentRate)
            }

            img5.setOnClickListener {
                currentRate = 5
                showStart(img2, img3, img4, img5, currentRate)
            }
            tvRate.setOnClickListener {
                listener.invoke("$currentRate${tvComment.text.toString().trim()}")
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    fun showOption(context: Context, type: Option, listener: () -> Unit) {
        val dialog = Dialog(context)
        val dialogBinding = DialogConfirmBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(context, R.color.transparent))
        )
        dialogBinding.run {
            when (type) {
                Option.EXIT -> {
                    tvTitle.text = context.getString(R.string.exit_app)
                    tvDes.text = context.getString(R.string.exit_app_message)
                }

                Option.DELETE -> {
                    tvTitle.text = context.getString(R.string.confirm)
                    tvDes.text = context.getString(R.string.delete_confirm)
                }

                Option.CANCEL -> {
                    tvTitle.text = context.getString(R.string.confirm)
                    tvDes.text = context.getString(R.string.cancel_mess)
                }
            }
            tvYes.setOnClickListener {
                listener.invoke()
                dialog.dismiss()
            }
            tvNo.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    fun showQuantityChoose(context: Context, product: BaloEntity, listener: (String) -> Unit) {
        val dialog = Dialog(context)
        val dialogBinding = DialogQuantityChooseBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(context, R.color.transparent))
        )
        dialogBinding.run {
            val max = product.quantitiy.toInt() - product.sell.toInt()
            imgAdd.setOnClickListener {
                imgMinus.visibility = View.VISIBLE
                val nextValue = tvQuantity.text.toString().toInt() + 1
                tvQuantity.text = nextValue.toString()
                if (nextValue == max) imgAdd.visibility = View.INVISIBLE
            }
            imgMinus.setOnClickListener {
                imgAdd.visibility = View.VISIBLE
                val nextValue = tvQuantity.text.toString().toInt() - 1
                tvQuantity.text = nextValue.toString()
                if (nextValue == 1) imgMinus.visibility = View.INVISIBLE
            }
            tvYes.setOnClickListener {
                listener.invoke(tvQuantity.text.toString())
                dialog.dismiss()
            }
            tvNo.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    fun showRevenueOption(context: Context, preType: Int, listener: (Int) -> Unit) {
        var type = preType
        val dialog = Dialog(context)
        val dialogBinding = DialogChooseSellBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(context, R.color.transparent))
        )
        dialogBinding.run {
            changeViewButtonRevenue(btnRevenue, btnProfit, btnSell, type)
            btnRevenue.setOnClickListener {
                type = Constants.TYPE_REVENUE
                changeViewButtonRevenue(btnRevenue, btnProfit, btnSell, Constants.TYPE_REVENUE)
            }
            btnProfit.setOnClickListener {
                type = Constants.TYPE_PROFIT
                changeViewButtonRevenue(btnRevenue, btnProfit, btnSell, Constants.TYPE_PROFIT)
            }
            btnSell.setOnClickListener {
                type = Constants.TYPE_SELL
                changeViewButtonRevenue(btnRevenue, btnProfit, btnSell, Constants.TYPE_SELL)
            }
            btnConfirm.setOnClickListener {
                listener.invoke(type)
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun changeViewButtonRevenue(revenue: Button, profit: Button, sell: Button, type: Int) {
        changeBgButton(revenue, type == Constants.TYPE_REVENUE)
        changeBgButton(profit, type == Constants.TYPE_PROFIT)
        changeBgButton(sell, type == Constants.TYPE_SELL)
    }

    private fun changeBgButton(button: Button, isSelect: Boolean) {
        button.setBackgroundResource(if (isSelect) R.drawable.bg_btn else R.drawable.bg_option)
    }
}
