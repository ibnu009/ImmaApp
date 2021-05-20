package com.pjb.immaapp.utils

import android.view.View
import com.pjb.immaapp.utils.global.snackbar
import retrofit2.HttpException
import timber.log.Timber
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

        return decimalFormat.format(anggaran)
    }

    fun convertNetworkStateErrorToSnackbar(view: View?, network: NetworkState) {
        Timber.d("Received network : ${network.status}")
        when (network.status) {
            Status.UNAUTHORISED -> {
                view?.snackbar("User tidak ditemukan")
            }
            Status.BAD_GATEAWAY -> {
                view?.snackbar("Kesalahan Server")
            }
            Status.SERVER_NOT_FOUND -> {
                view?.snackbar("Server tidak ditemukan")
            }
            Status.CONFLICT -> {
                view?.snackbar("Terjadi kesalahan pada Server")
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