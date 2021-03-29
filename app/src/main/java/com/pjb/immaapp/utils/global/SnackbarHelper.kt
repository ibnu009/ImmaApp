package com.pjb.immaapp.utils.global

import android.app.Activity
import android.content.Context
import android.content.Intent
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

