package com.pjb.immaapp.utils.validator

import android.text.TextUtils
import android.widget.EditText
import androidx.core.text.TextUtilsCompat
import androidx.databinding.BindingAdapter

class ImmaFormValidator {
    @BindingAdapter("usernameValidator")
    fun usernamevalidator(editText: EditText, username: String) {
        if (TextUtils.isEmpty(username)) {
            editText.error = "Username tidak boleh kosong!"
            return
        }
    }
}