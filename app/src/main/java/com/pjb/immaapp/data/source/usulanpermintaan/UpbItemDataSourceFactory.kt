package com.pjb.immaapp.data.source.usulanpermintaan

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.pjb.immaapp.data.entity.upb.ItemPermintaanBarang
import com.pjb.immaapp.webservice.usulan.UsulanPermintaanBarangService
import io.reactivex.disposables.CompositeDisposable

class UpbItemDataSourceFactory(
    private val apiService: UsulanPermintaanBarangService,
    private val compositeDisposable: CompositeDisposable,
    private val token: String,
    private val idPermintaan: Int
) : DataSource.Factory<Int, ItemPermintaanBarang>() {

    val upbLiveDataSource = MutableLiveData<UpbItemDataSource>()

    override fun create(): DataSource<Int, ItemPermintaanBarang> {
        val upbItemDataSource =
            UpbItemDataSource(apiService, compositeDisposable, token, idPermintaan)

        upbLiveDataSource.postValue(upbItemDataSource)
        return upbItemDataSource
    }
}