package com.pjb.immaapp.utils.mapper

import com.pjb.immaapp.data.entity.local.supplier.Suppliers
import com.pjb.immaapp.data.remote.response.ResponseSupplier

class SupplierMapper {
    fun transform(response: ResponseSupplier): Suppliers{
        return with(response) {
            Suppliers(data = data.map {
                Suppliers.SupplierEntity(
                    id_supplier = 0,
                    id = it.id,
                    nama = it.nama,
                )
            })
        }
    }
}