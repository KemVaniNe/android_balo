package com.example.balo.utils

import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.BillEntity
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.model.CartEntity
import com.example.balo.data.model.NotificationEntity
import com.example.balo.data.model.OrderDetailEntity
import com.example.balo.data.model.OrderEntity
import com.example.balo.data.model.UserEntity
import com.example.balo.data.model.enum.Balo
import com.example.balo.data.model.enum.Brand
import com.example.balo.data.model.enum.Cart
import com.example.balo.data.model.enum.Notification
import com.example.balo.data.model.enum.Order
import com.example.balo.data.model.enum.OrderDetail
import com.example.balo.data.model.enum.User
import com.google.firebase.firestore.DocumentSnapshot

object DocumentUtil {
    fun convertDocToBProduct(document: DocumentSnapshot): BaloEntity {
        return BaloEntity(
            id = document.id,
            name = document.getString(Balo.NAME.property) ?: "",
            idBrand = document.getString(Balo.ID_BRAND.property)
                ?: Constants.ID_BRAND_OTHER,
            priceSell = document.getDouble(Balo.PRICESELL.property) ?: 0.0,
            priceImport = document.getDouble(Balo.PRICEINPUT.property) ?: 0.0,
            des = document.getString(Balo.DES.property) ?: "",
            pic = document.getString(Balo.PIC.property) ?: "",
            sell = document.getDouble(Balo.SELL.property) ?: 0.0,
            quantitiy = document.getDouble(Balo.QUANTITY.property) ?: 0.0,
            rate = document.getDouble(Balo.RATE.property) ?: 0.0,
            comment = document.get(Balo.COMMENT.property) as? List<String> ?: emptyList(),
            totalImport = document.getDouble(Balo.TOTALIMPORT.property) ?: 0.0,
            totalSell = document.getDouble(Balo.TOTALSELL.property) ?: 0.0,
            isSell = document.getBoolean(Balo.ISSELL.property) ?: true
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
            quantity = document.getDouble(Cart.QUANTITY.property) ?: 0.0
        )
    }

    fun convertDocToUser(document: DocumentSnapshot): UserEntity {
        return UserEntity(
            document.id,
            document.getString(User.NAME.property) ?: "",
            document.getString(User.EMAIL.property) ?: "",
            document.getString(User.PHONE.property) ?: "",
            document.getString(User.PASSWORD.property) ?: "",
            document.getBoolean(User.ROLE.property) ?: false,
            document.getString(User.AUTHCODE.property) ?: "",
            document.get(User.ADDRESS.property) as? List<String> ?: emptyList()
        )
    }

    fun convertDocToOrder(document: DocumentSnapshot): OrderEntity {
        return OrderEntity(
            document.id,
            document.getString(Order.ID_USER.property) ?: "",
            document.getString(Order.DATE.property) ?: "",
            document.getDouble(Order.TOTAL_PRICE.property) ?: 0.0,
            document.getString(Order.STATUS_ORDER.property) ?: Constants.ORDER_CONFIRM,
            document.getDouble(Order.PRICESHIP.property) ?: 0.0,
            document.getString(Order.ADDRESS.property) ?: "",
            convertDocToOrderDetail(document)
        )
    }

    private fun convertDocToOrderDetail(document: DocumentSnapshot): List<OrderDetailEntity> {
        val orderDetails =
            (document.get(Order.DETAIL.property) as? List<Map<String, Any>>)?.map { detailMap ->
                OrderDetailEntity(
                    idBalo = detailMap[OrderDetail.ID_BALO.property] as? String ?: "",
                    nameBalo = detailMap[OrderDetail.NAMEBALO.property] as? String ?: "",
                    quantity = detailMap[OrderDetail.QUANTITY.property] as? Double ?: 0.0,
                    price = detailMap[OrderDetail.PRICE.property] as? Double ?: 0.0,
                    picProduct = detailMap[OrderDetail.PICBALO.property] as? String ?: "",
                    rate = detailMap[OrderDetail.RATE.property] as? Double ?: 0.0,
                    comment = detailMap[OrderDetail.COMMENT.property] as? String ?: ""
                )
            } ?: emptyList()
        return orderDetails
    }

    fun convertDocToBill(document: DocumentSnapshot): BillEntity {
        return BillEntity(
            document.id,
            document.getDouble(Order.TOTAL_PRICE.property) ?: 0.0,
            document.getString(Order.DATE.property) ?: "",
            document.getString(Order.STATUS_ORDER.property) ?: ""
        )
    }

    fun convertDocToNotification(document: DocumentSnapshot): NotificationEntity {
        return NotificationEntity(
            id = document.id,
            idUser = document.getString(Notification.idUser.property) ?: "",
            notification = document.getString(Notification.notification.property) ?: "",
            datatime = document.getString(Notification.datatime.property) ?: "",
            idOrder = document.getString(Notification.idOrder.property) ?: "",
            isSeen = document.getBoolean(Notification.isSeen.property) ?: false,
            roleUser = document.getBoolean(Notification.role.property) ?: false,
        )
    }
}