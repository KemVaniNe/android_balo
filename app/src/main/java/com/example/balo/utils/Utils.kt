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
import com.example.balo.data.model.enum.Balo
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
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

    fun showBottomBrand(
        context: Context,
        brands: List<BrandEntity>,
        listener: (BrandEntity?) -> Unit
    ) {
        return showBottomBrand(context, brands, listener)
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
}