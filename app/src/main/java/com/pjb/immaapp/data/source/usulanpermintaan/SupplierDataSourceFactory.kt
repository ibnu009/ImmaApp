package com.pjb.immaapp.data.source.usulanpermintaan

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.pjb.immaapp.data.entity.upb.Supplier
import com.pjb.immaapp.service.webservice.usulan.UsulanPermintaanBarangService
import io.reactivex.disposables.CompositeDisposable

class SupplierDataSourceFactory(
    private val apiService: UsulanPermintaanBarangService,
    private val compositeDisposable: CompositeDisposable,
    private val token: String
): DataSource.Factory<Int, Supplier>() {
    val supplierLiveDataSource = MutableLiveData<SupplierDataSource>()

    override fun create(): DataSource<Int, Supplier> {
        val supplierDataSource = SupplierDataSource(apiService, compositeDisposable, token)

        supplierLiveDataSource.postValue(supplierDataSource)
        return supplierDataSource
    }
}