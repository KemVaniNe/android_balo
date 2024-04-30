package com.example.balo.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.balo.R
import firebase.com.protolitewrapper.BuildConfig.APPLICATION_ID

object PermissionUtil {

    private const val REQUEST_CODE_SETTING = 123

    private val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    }

    fun requireGalleryPermission(context: Context, listener: () -> Unit) {
        if (ContextCompat.checkSelfPermission(
                context, permissions
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            listener.invoke()
        } else {
            ActivityCompat.requestPermissions(
                context as Activity, arrayOf(permissions), REQUEST_CODE_SETTING
            )
        }
    }

    fun showRequirePermission(activity: Activity, listener: () -> Unit) {
        androidx.appcompat.app.AlertDialog.Builder(activity)
            .setTitle(R.string.permission)
            .setCancelable(false)
            .setMessage(R.string.you_must)
            .setPositiveButton(R.string.goto_setting) { _, _ ->
                gotoSettingMyPhone(activity)
            }
            .setNegativeButton(R.string.cancel) { _, _ ->
                listener.invoke()
            }.create().show()
    }

    private fun gotoSettingMyPhone(activity: Activity) {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${APPLICATION_ID}")
        )
        intent.data = Uri.fromParts("package", activity.packageName, null)
        activity.startActivityForResult(intent, REQUEST_CODE_SETTING)
    }
}