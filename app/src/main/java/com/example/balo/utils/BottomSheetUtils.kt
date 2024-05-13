package com.example.balo.utils

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.balo.adapter.brand.BrandBottomAdapter
import com.example.balo.data.model.BrandEntity
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
}