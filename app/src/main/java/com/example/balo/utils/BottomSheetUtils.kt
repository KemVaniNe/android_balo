package com.example.balo.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.R
import com.example.balo.adapter.brand.BrandBottomAdapter
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.model.UserEntity
import com.example.balo.databinding.BottomChangePassBinding
import com.example.balo.databinding.BottomSheetBrandsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

object BottomSheetUtils {
    fun showBottomBrand(
        context: Context,
        brands: List<BrandEntity>,
        listener: (BrandEntity?) -> Unit
    ) {
        var brand: BrandEntity? = null
        val brandAdapter = BrandBottomAdapter(brands) { pos ->
            brand = brands[pos]
            brands.map { it.isSelected = it == brands[pos] }
        }
        val bottomSheetBinding = BottomSheetBrandsBinding.inflate(LayoutInflater.from(context))
        val bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetBinding.run {
            rvBrand.layoutManager = LinearLayoutManager(context)
            rvBrand.adapter = brandAdapter
            btnChoose.setOnClickListener {
                listener.invoke(brand)
                bottomSheetDialog.dismiss()
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