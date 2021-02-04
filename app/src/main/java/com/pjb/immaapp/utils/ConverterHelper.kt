package com.pjb.immaapp.utils

import com.pjb.immaapp.R
import kotlinx.android.synthetic.main.po_item.view.*
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
}