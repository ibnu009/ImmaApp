package com.pjb.immaapp.utils

import android.view.View
import com.pjb.immaapp.utils.global.snackbar
import timber.log.Timber
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class ConverterHelper {
//    Convert Anggaran
    fun convertAnggaranFormat(anggaran: Int): String {
        val decimalFormat: DecimalFormat =
            NumberFormat.getInstance(Locale.getDefault()) as DecimalFormat
        decimalFormat.applyPattern("#,###,###,###")
        val anggaranFix = decimalFormat.format(anggaran)

        return anggaranFix
    }

    fun convertNetworkStateErrorToSnackbar(view: View?, network: NetworkState) {
        when (network) {
            NetworkState.USERNOTFOUND -> {
                view?.snackbar("User Not Found")
            }
            NetworkState.BAD_GATEAWAY ->{
                view?.snackbar("Kesalahan Server")
            }
            NetworkState.EXPIRETOKEN ->{
                view?.snackbar("Token anda Expire")
            }
            NetworkState.UNKNOWN -> {
                view?.snackbar("Unknown Error")
            }
            else -> {
                view?.snackbar("Unknown Error")
            }
        }
    }
}