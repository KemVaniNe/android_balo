package com.example.balo.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.R
import com.example.balo.adapter.brand.BrandFilterAdapter
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.model.UserEntity
import com.example.balo.databinding.BottomChangeInfoClientBinding
import com.example.balo.databinding.BottomChangePassBinding
import com.example.balo.databinding.BottomFilterBinding
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
            edtUsername.setText(user.username)
            edtAuth.setText(user.authcode)
            edtEmail.setText(user.email)
            btnConfirm.setOnClickListener {
                val pass = edtPassword.text.toString().trim()
                val newName = edtUsername.text.toString().trim()
                val auth = edtAuth.text.toString().trim()
                val email = edtEmail.text.toString().trim()
                if (pass == "" || newName == "" || auth == "" || email == "") {
                    changeViewError(context.getString(R.string.not_fill_all), tvError)
                } else if (!Utils.verifyPassword(pass, user.password)) {
                    changeViewError(context.getString(R.string.password_confirm_wrong), tvError)
                } else {
                    user.username = newName
                    user.email = email
                    user.authcode = auth
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

    fun bottomFilter(
        context: Context,
        soft: Int,
        brand: BrandEntity,
        list: List<BrandEntity>,
        listener: (Pair<Int, BrandEntity>) -> Unit,
    ) {
        var softChoose = soft
        var brandChoose = brand
        val adapterBrand = BrandFilterAdapter(list) { data ->
            brandChoose = list[data]
            list.map { it.isSelected = it == list[data] }
        }
        val bottomSheetBinding = BottomFilterBinding.inflate(LayoutInflater.from(context))
        val bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetBinding.run {
            tvZA.setBackgroundResource(if (softChoose == Constants.TYPE_ZA) R.drawable.bg_btn else R.drawable.bg_option)
            tvAZ.setBackgroundResource(if (softChoose == Constants.TYPE_AZ) R.drawable.bg_btn else R.drawable.bg_option)
            rvBrand.run {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = adapterBrand
            }
            btnClear.setOnClickListener {
                listener.invoke(Pair(Constants.TYPE_NONE, Utils.brandAll()))
                bottomSheetDialog.dismiss()
            }
            btnConfirm.setOnClickListener {
                listener.invoke(Pair(softChoose, brandChoose))
                bottomSheetDialog.dismiss()
            }
            tvAZ.setOnClickListener {
                softChoose = Constants.TYPE_AZ
                tvAZ.setBackgroundResource(R.drawable.bg_btn)
                tvZA.setBackgroundResource(R.drawable.bg_option)
            }
            tvZA.setOnClickListener {
                softChoose = Constants.TYPE_ZA
                tvZA.setBackgroundResource(R.drawable.bg_btn)
                tvAZ.setBackgroundResource(R.drawable.bg_option)
            }
        }
        bottomSheetDialog.show()
    }

    private fun changeViewError(text: String, error: TextView) {
        error.visibility = View.VISIBLE
        error.text = text
    }
}