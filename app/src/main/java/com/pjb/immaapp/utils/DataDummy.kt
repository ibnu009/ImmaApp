package com.pjb.immaapp.utils

import com.pjb.immaapp.data.entity.PermintaanBarang

object DataDummy {
    fun getPermintaanBarang(): ArrayList<PermintaanBarang>{
        val listPermintaanBarang = ArrayList<PermintaanBarang>()

        listPermintaanBarang.add(
            PermintaanBarang(
                no = 1,
                idPermintaan = 52,
                noPermintaan = "PB2209200001",
                tanggalPermohonan = "2020-09-22"
            )
        )

        listPermintaanBarang.add(
            PermintaanBarang(
                no = 2,
                idPermintaan = 53,
                noPermintaan = "PB2409200001",
                tanggalPermohonan = "2020-09-24"
            )
        )

        return listPermintaanBarang
    }

}