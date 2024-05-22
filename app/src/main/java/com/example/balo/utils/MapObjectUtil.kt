package com.example.balo.utils

import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.model.CartEntity
import com.example.balo.data.model.UserEntity
import com.example.balo.data.model.enum.Balo
import com.example.balo.data.model.enum.Brand
import com.example.balo.data.model.enum.Cart
import com.example.balo.data.model.enum.User

object MapObjectUtil {
    fun productToMap(product: BaloEntity): Map<String, Any> {
        return hashMapOf(
            Balo.NAME.property to product.name,
            Balo.ID_BRAND.property to product.idBrand,
            Balo.PRICESELL.property to product.priceSell,
            Balo.DES.property to if (product.des == "") "Không có!" else product.des,
            Balo.PIC.property to product.pic,
            Balo.PRICEINPUT.property to product.priceImport,
            Balo.QUANTITY.property to product.quantitiy,
            Balo.SELL.property to product.sell,
            Balo.RATE.property to product.rate,
            Balo.COMMENT.property to product.comment,
        )
    }

    fun brandToMap(brand: BrandEntity): Map<String, Any> {
        return hashMapOf(
            Brand.NAME.property to brand.name,
            Brand.DES.property to if (brand.des == "") "Không có!" else brand.des,
            Brand.PIC.property to brand.pic
        )
    }

    fun cartToMap(cart: CartEntity): Map<String, Any> {
        return hashMapOf(
            Cart.ID_BALO.property to cart.idBalo,
            Cart.ID_USER.property to cart.idUser,
            Cart.QUANTITY.property to cart.quantity
        )
    }

    fun userToMap(user: UserEntity): Map<String, Any> {
        return hashMapOf(
            User.PASSWORD.property to user.password,
            User.NAME.property to user.username,
            User.ADDRESS.property to user.address
        )
    }
}