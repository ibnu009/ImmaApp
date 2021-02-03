package com.pjb.immaapp.utils

import com.pjb.immaapp.data.entity.GudangPermintaanBarang
import com.pjb.immaapp.data.entity.upb.PermintaanBarang
import com.pjb.immaapp.data.entity.po.PurchaseOrder

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

    fun getPurchaseOrder() : ArrayList<PurchaseOrder>{
        val listPurchaseOrder = ArrayList<PurchaseOrder>()

        listPurchaseOrder.add(
            PurchaseOrder(
                ponum = "169.SP/SOT/600/UJKT/2020",
                encodePonum = "MTY5LlNQL1NPVC82MDAvVUpLVC8yMDIw",
                jobTitle =  "USULAN PENGADAAN TABUNG OKSIGEN PORTABEL",
                orderDate = "2020-07-14",
                anggaran = 3445000
            )
        )

        listPurchaseOrder.add(
            PurchaseOrder(
                ponum = "FK.0011.SP/SEK/600/UJKT/2020",
                encodePonum = "RksuMDAxMS5TUC9TRUsvNjAwL1VKS1QvMjAyMA==",
                jobTitle =  "DO KP EKTRAFOODING JANUARI 2020",
                orderDate = "2020-01-23",
                anggaran = 34371800
            )
        )

        return listPurchaseOrder
    }

    fun getPermintaanGudang() : ArrayList<GudangPermintaanBarang>{
        val listPermintaanGudang = ArrayList<GudangPermintaanBarang>()

        listPermintaanGudang.add(
            GudangPermintaanBarang(
                nomorPO = "FK0204.SP/OMG/600/UJKT/2020"
            )
        )

        listPermintaanGudang.add(
            GudangPermintaanBarang(
                nomorPO = "FK0302.SP/OMG/620/UJKT/2022"
            )
        )

        return listPermintaanGudang
    }

}