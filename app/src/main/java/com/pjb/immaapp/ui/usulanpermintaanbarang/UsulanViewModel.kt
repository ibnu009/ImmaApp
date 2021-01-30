package com.pjb.immaapp.ui.usulanpermintaanbarang

import androidx.lifecycle.ViewModel
import com.pjb.immaapp.utils.DataDummy

class UsulanViewModel : ViewModel(){
    fun getUsulan() = DataDummy.getPermintaanBarang()
}