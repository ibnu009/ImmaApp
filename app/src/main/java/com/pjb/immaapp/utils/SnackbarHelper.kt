package com.pjb.immaapp.utils

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

fun Context.loadingDialog(message: String?): AlertDialog {
    val dialogLoadingBuilder =
        AlertDialog.Builder(this).apply {
            setTitle(message)
        }
    return dialogLoadingBuilder.create()
}