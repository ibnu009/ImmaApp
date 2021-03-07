package com.pjb.immaapp.utils.global

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar

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

fun Context.welcomeDialog(message: String?): AlertDialog {
    val dialogLoadingBuilder =
        AlertDialog.Builder(this).apply {
            setTitle("Welcome!")
            setMessage(message)
            setPositiveButton("OK"
            ) { p0, _ ->  p0.dismiss()}
        }
    return dialogLoadingBuilder.create()
}