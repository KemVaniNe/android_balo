package com.example.balo.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.example.balo.R
import org.mindrot.jbcrypt.BCrypt

object Utils {
    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(password, hashedPassword)
    }

    fun convertNumberVerify(phone: String): String {
        return "+84" + phone.trim().substring(1)
    }

    fun showProgressDialog(context: Context): AlertDialog {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val progressDialogView = inflater.inflate(R.layout.dialog_progress, null)
        builder.setView(progressDialogView)
        val dialog = builder.create()
        dialog.setCancelable(false)
        return dialog
    }

}