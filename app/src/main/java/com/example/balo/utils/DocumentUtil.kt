package com.example.balo.utils

import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.model.enum.Balo
import com.example.balo.data.model.enum.Brand
import com.google.firebase.firestore.DocumentSnapshot

object DocumentUtil {
    fun convertDocToBProduct(document: DocumentSnapshot): BaloEntity {
        return BaloEntity(
            id = document.id,
            name = document.getString(Balo.NAME.property) ?: "",
            idBrand = document.getString(Balo.ID_BRAND.property)
                ?: Constants.ID_BRAND_OTHER,
            priceSell = document.getString(Balo.PRICESELL.property) ?: "",
            priceImport = document.getString(Balo.PRICEINPUT.property) ?: "",
            des = document.getString(Balo.DES.property) ?: "",
            pic = document.getString(Balo.PIC.property) ?: "",
            sell = document.getString(Balo.SELL.property) ?: "",
            quantitiy = document.getString(Balo.QUANTITY.property) ?: "",
            rate = document.getString(Balo.RATE.property) ?: "0",
            comment = document.get(Balo.COMMENT.property) as? List<String> ?: emptyList()
        )
    }

    fun convertDocToBrand(document: DocumentSnapshot): BrandEntity {
        return BrandEntity(
            id = document.id,
            name = document.getString(Brand.NAME.property) ?: "",
            des = document.getString(Brand.DES.property) ?: "",
            pic = document.getString(Brand.PIC.property) ?: ""
        )
    }
}