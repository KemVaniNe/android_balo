package com.example.balo.utils

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.example.balo.R
import com.example.balo.data.model.BaloEntity
import com.example.balo.databinding.DialogConfirmBinding
import com.example.balo.databinding.DialogQuantityChooseBinding

enum class Option {
    EXIT, DELETE
}

object DialogUtil {
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
}
