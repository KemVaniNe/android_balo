package com.example.balo.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.view.LayoutInflater
import android.widget.ImageView
import com.example.balo.R
import com.example.balo.data.model.BaloEntity
import com.example.balo.data.model.BrandEntity
import com.example.balo.data.model.CartEntity
import com.example.balo.data.model.OrderDetailEntity
import com.example.balo.data.model.OrderEntity
import com.example.balo.data.model.UserEntity
import com.example.balo.data.model.enum.Balo
import com.example.balo.data.model.enum.Order
import com.example.balo.data.model.enum.OrderDetail
import com.example.balo.data.model.enum.User
import com.google.firebase.firestore.DocumentSnapshot
import org.mindrot.jbcrypt.BCrypt
import java.io.ByteArrayOutputStream

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

    fun displayBase64Image(base64String: String, imageView: ImageView) {
        if (base64String != "") {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            imageView.setImageBitmap(bitmap)
        } else {
            imageView.setImageResource(R.drawable.ic_tag)
        }
    }

    fun requireGalleryPermission(context: Context, listener: () -> Unit) {
        return PermissionUtil.requireGalleryPermission(context, listener)
    }

    fun showRequirePermission(activity: Activity, listener: () -> Unit) {
        return PermissionUtil.showRequirePermission(activity, listener)
    }

    @SuppressLint("Recycle")
    fun uriToBase64(context: Context, uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bytes = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (inputStream?.read(buffer).also { bytesRead = it!! } != -1) {
            bytes.write(buffer, 0, bytesRead)
        }
        val byteArray = bytes.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun showOption(context: Context, type: Option, listener: () -> Unit) {
        return DialogUtil.showOption(context, type, listener)
    }

    fun bottomUpdatePass(
        context: Context,
        user: UserEntity,
        listener: (UserEntity) -> Unit,
    ) {
        return BottomSheetUtils.bottomUpdatePass(context, user, listener)
    }

    fun bottomUpdateInfo(
        context: Context,
        user: UserEntity,
        listener: (UserEntity) -> Unit,
    ) {
        return BottomSheetUtils.bottomUpdateInfo(context, user, listener)
    }

    fun otherBrand(id: String): BrandEntity {
        return BrandEntity(
            id = Constants.ID_BRAND_OTHER,
            name = "Thương hiệu khác",
            des = "Không có",
            isSelected = id == Constants.ID_BRAND_OTHER
        )
    }

    fun convertDocToBProduct(document: DocumentSnapshot): BaloEntity {
        return DocumentUtil.convertDocToBProduct(document)
    }

    fun convertDocToBrand(document: DocumentSnapshot): BrandEntity {
        return DocumentUtil.convertDocToBrand(document)
    }

    fun productToMap(product: BaloEntity): Map<String, Any> {
        return MapObjectUtil.productToMap(product)
    }

    fun orderToMap(order: OrderEntity): Map<String, Any> {
        return MapObjectUtil.orderToMap(order)
    }

    fun orderDetailToMap(order: OrderDetailEntity): Map<String, Any> {
        return MapObjectUtil.orderDetailToMap(order)
    }

    fun brandToMap(brand: BrandEntity): Map<String, Any> {
        return MapObjectUtil.brandToMap(brand)
    }

    fun cartToMap(cart: CartEntity): Map<String, Any> {
        return MapObjectUtil.cartToMap(cart)
    }

    fun convertDocToCart(document: DocumentSnapshot): CartEntity {
        return DocumentUtil.convertDocToCart(document)
    }

    fun showQuantityChoose(context: Context, product: BaloEntity, listener: (String) -> Unit) {
        return DialogUtil.showQuantityChoose(context, product, listener)
    }

    fun calculate(quantity: String, sell: String, isMinus: Boolean = false): String {
        return if (isMinus) {
            (stringToInt(quantity) - stringToInt(sell)).toString()
        } else {
            (stringToInt(quantity) + stringToInt(sell)).toString()
        }
    }

    fun stringToInt(value: String): Int {
        return value.toIntOrNull() ?: 0
    }

    fun convertDocToUser(document: DocumentSnapshot): UserEntity {
        return DocumentUtil.convertDocToUser(document)
    }

    fun userToMap(user: UserEntity): Map<String, Any> {
        return MapObjectUtil.userToMap(user)
    }

    fun convertDocToOrder(document: DocumentSnapshot): OrderEntity {
        return DocumentUtil.convertDocToOrder(document)
    }

    fun sellToMap(sell: String): Map<String, Any> {
        return MapObjectUtil.sellToMaps(sell)
    }

    fun statusCancelToMap(): Map<String, Any> {
        return MapObjectUtil.statusCancelToMap()
    }

}