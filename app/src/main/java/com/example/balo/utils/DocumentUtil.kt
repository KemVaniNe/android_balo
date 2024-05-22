package com.example.balo.utils

import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.model.CartEntity
import com.example.balo.data.model.UserEntity
import com.example.balo.data.model.enum.Balo
import com.example.balo.data.model.enum.Brand
import com.example.balo.data.model.enum.Cart
import com.example.balo.data.model.enum.User
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

    fun convertDocToCart(document: DocumentSnapshot): CartEntity {
        return CartEntity(
            idCart = document.id,
            idBalo = document.getString(Cart.ID_BALO.property) ?: "",
            idUser = document.getString(Cart.ID_USER.property) ?: "",
            quantity = document.getString(Cart.QUANTITY.property) ?: "0"
        )
    }

    fun convertDocToUser(document: DocumentSnapshot): UserEntity {
        return UserEntity(
            document.id,
            document.getString(User.NAME.property) ?: "",
            document.getString(User.PHONE.property) ?: "",
            document.getString(User.PASSWORD.property) ?: "",
            document.getBoolean(User.ROLE.property) ?: false,
            document.get(User.ADDRESS.property) as? List<String> ?: emptyList()
        )
    }
}