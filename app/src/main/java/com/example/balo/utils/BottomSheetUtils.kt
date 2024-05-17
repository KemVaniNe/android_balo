package com.example.balo.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.balo.R
import com.example.balo.data.model.UserEntity
import com.example.balo.databinding.BottomChangeInfoClientBinding
import com.example.balo.databinding.BottomChangePassBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

object BottomSheetUtils {
    fun bottomUpdateInfo(
        context: Context,
        user: UserEntity,
        listener: (UserEntity) -> Unit,
    ) {
        val bottomSheetBinding = BottomChangeInfoClientBinding.inflate(LayoutInflater.from(context))
        val bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetBinding.run {
            btnConfirm.setOnClickListener {
                val pass = edtPassword.text.toString().trim()
                val newName = edtUsername.text.toString().trim()
                if (pass == "" || newName == "") {
                    changeViewError(context.getString(R.string.not_fill_all), tvError)
                } else if (!Utils.verifyPassword(pass, user.password)) {
                    changeViewError(context.getString(R.string.password_confirm_wrong), tvError)
                } else {
                    user.username = newName
                    listener.invoke(user)
                    bottomSheetDialog.dismiss()
                }
            }
        }
        bottomSheetDialog.show()
    }

    fun bottomUpdatePass(
        context: Context,
        user: UserEntity,
        listener: (UserEntity) -> Unit,
    ) {
        val bottomSheetBinding = BottomChangePassBinding.inflate(LayoutInflater.from(context))
        val bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetBinding.run {
            btnConfirm.setOnClickListener {
                val pass = edtPassword.text.toString().trim()
                val newPass = edtNewPass.text.toString().trim()
                val confirmPass = edtConfirm.text.toString().trim()
                if (pass == "" || newPass == "" || confirmPass == "") {
                    changeViewError(context.getString(R.string.not_fill_all), tvError)
                } else if (!Utils.verifyPassword(pass, user.password)) {
                    changeViewError(context.getString(R.string.wrong_password), tvError)
                } else if (newPass != confirmPass) {
                    changeViewError(context.getString(R.string.password_confirm_wrong), tvError)
                } else {
                    user.password = Utils.hashPassword(newPass)
                    listener.invoke(user)
                    bottomSheetDialog.dismiss()
                }
            }
        }
        bottomSheetDialog.show()
    }

    private fun changeViewError(text: String, error: TextView) {
        error.visibility = View.VISIBLE
        error.text = text
    }
}