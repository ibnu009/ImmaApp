package com.pjb.immaapp.ui.gudangpermintaanbarang

import androidx.lifecycle.ViewModel
import com.pjb.immaapp.utils.DataDummy

class GudangViewModel : ViewModel() {
    fun getGudang() = DataDummy.getPermintaanGudang()
}