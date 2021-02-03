package com.pjb.immaapp.data.source.usulanpermintaan

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.pjb.immaapp.data.entity.PermintaanBarang
import com.pjb.immaapp.webservice.usulan.UsulanPermintaanBarangService
import io.reactivex.disposables.CompositeDisposable

class UpbDataSourceFactory(
    private val apiService : UsulanPermintaanBarangService,
    private val compositeDisposable: CompositeDisposable,
    private val token: String,
    private val keyword: String?
) : DataSource.Factory<Int, PermintaanBarang>() {
    val usulanPermintaanLiveDataSource = MutableLiveData<UpbDataSource>()

    override fun create(): DataSource<Int, PermintaanBarang> {
        val usulanDataSource = UpbDataSource(apiService, compositeDisposable, token, keyword)

        usulanPermintaanLiveDataSource.postValue(usulanDataSource)
        return usulanDataSource
    }
}