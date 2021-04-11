package com.pjb.immaapp.utils

import android.view.View
import com.pjb.immaapp.utils.global.snackbar
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class ConverterHelper {

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
            NetworkState.CONFLICT -> {
                view?.snackbar("Terjadi Konflik pada server")
            }
            NetworkState.UNKNOWN -> {
                view?.snackbar("Unknown Error")
            }
            else -> {
                view?.snackbar("Unknown Error")
            }
        }
    }

    fun convertExceptionToMessage(throwable: Throwable): String {
        var result = ""
         when (throwable) {
             is IOException -> result = "Pastikan anda terkoneksi ke internet"
             is HttpException -> {
                 result = when(throwable.code()){
                     HttpURLConnection.HTTP_NOT_FOUND -> "Mohon Maaf terjadi kesahalan server"

                     HttpURLConnection.HTTP_CONFLICT -> "Terjadi Conflict pada server"

                     HttpURLConnection.HTTP_UNAVAILABLE -> "Service ini tidak tersedia sekarang.."

                     HttpURLConnection.HTTP_CLIENT_TIMEOUT -> "Token anda telah kadaluarsa"

                     else -> "Terjadi Kesalhan"
                 }
             }
         }
        return result
    }
}