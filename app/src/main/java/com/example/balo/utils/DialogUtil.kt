package com.example.balo.utils

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.example.balo.R
import com.example.balo.databinding.DialogConfirmBinding

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
                    tvDes.text = context.getString(R.string.delete_brand_mess)
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
}
