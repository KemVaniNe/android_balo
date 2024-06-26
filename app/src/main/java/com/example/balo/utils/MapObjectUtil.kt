package com.example.balo.utils

import com.example.balo.data.model.BaloEntity
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

object MapObjectUtil {
    fun orderToMap(order: OrderEntity): Map<String, Any> {
        return hashMapOf(
            Order.ID_USER.property to order.iduser,
            Order.TOTAL_PRICE.property to order.totalPrice,
            Order.PRICESHIP.property to order.priceShip,
            Order.DATE.property to order.date,
            Order.ADDRESS.property to order.address,
            Order.STATUS_ORDER.property to order.statusOrder,
            Order.IDPAY.property to order.idpay,
            Order.DETAIL.property to order.detail.map { detail -> orderDetailToMap(detail) }
        )
    }

    fun orderDetailToMap(detail: OrderDetailEntity): Map<String, Any> {
        return hashMapOf(
            OrderDetail.ID_BALO.property to detail.idBalo,
            OrderDetail.NAMEBALO.property to detail.nameBalo,
            OrderDetail.QUANTITY.property to detail.quantity,
            OrderDetail.PRICE.property to detail.price,
            OrderDetail.PICBALO.property to detail.picProduct,
            OrderDetail.RATE.property to detail.rate,
            OrderDetail.COMMENT.property to detail.comment
        )
    }

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
            Balo.TOTALIMPORT.property to product.totalImport,
            Balo.TOTALSELL.property to product.totalSell,
            Balo.ISSELL.property to product.isSell
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
            Cart.QUANTITY.property to cart.quantity,
        )
    }

    fun userToMap(user: UserEntity): Map<String, Any> {
        return hashMapOf(
            User.PASSWORD.property to user.password,
            User.NAME.property to user.username,
            User.ADDRESS.property to user.address,
            User.EMAIL.property to user.email,
            User.AUTHCODE.property to user.authcode,
            User.ROLE.property to user.role,
            User.PHONE.property to user.phone
        )
    }

    fun sellToMaps(sell: Double, priceSell: Double): Map<String, Any> {
        return hashMapOf(
            Balo.SELL.property to sell,
            Balo.TOTALSELL.property to priceSell
        )
    }

    fun statusCancelToMap(): Map<String, Any> {
        return hashMapOf(
            Order.STATUS_ORDER.property to Constants.ORDER_CANCEL
        )
    }

    fun notificationToMap(notification: NotificationEntity): Map<String, Any> {
        return hashMapOf(
            Notification.idUser.property to notification.idUser,
            Notification.notification.property to notification.notification,
            Notification.datatime.property to notification.datatime,
            Notification.isSeen.property to notification.isSeen,
            Notification.role.property to notification.roleUser,
            Notification.idOrder.property to notification.idOrder
        )
    }
}