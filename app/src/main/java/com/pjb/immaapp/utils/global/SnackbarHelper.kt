package com.pjb.immaapp.utils.global

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.pjb.immaapp.ui.login.LoginActivity

fun View.snackbar(message: String) {
    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_LONG
    ).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}

fun ContentResolver.getFileName(fileUri: Uri): String{
    var name = ""
    val returnCursor = this.query(fileUri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }
    return name
}

fun Context.tokenExpired(): AlertDialog {
    val dialogLoadingBuilder =
        AlertDialog.Builder(this).apply {
            setTitle("Oops Token anda telah usang!")
            setMessage("anda akan dipindah ke login screen untuk memperbarui token anda")
            setPositiveButton("OK"
            ) { p0, _ ->
                val intent = Intent(this@tokenExpired, LoginActivity::class.java)
                startActivity(intent)
                p0.dismiss()}
        }
    return dialogLoadingBuilder.create()
}

