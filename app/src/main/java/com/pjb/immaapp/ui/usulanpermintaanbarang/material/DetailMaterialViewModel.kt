package com.pjb.immaapp.ui.usulanpermintaanbarang.material

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pjb.immaapp.data.entity.upb.Company
import com.pjb.immaapp.data.entity.upb.Material
import com.pjb.immaapp.data.repository.DataUpbRepository
import com.pjb.immaapp.utils.NetworkState
import io.reactivex.disposables.CompositeDisposable

class DetailMaterialViewModel(
    private val dataUpbRepository: DataUpbRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    fun getDetailMaterial(apiKey: String, token: String, detailId: Int): LiveData<Material> {
        return dataUpbRepository.requestMaterialDetail(compositeDisposable, apiKey, token, detailId)
    }

    fun getCompanyList(apiKey: String, token: String, detailId: Int): LiveData<List<Company>> {
        return dataUpbRepository.requestCompanyList(compositeDisposable, apiKey, token, detailId)
    }

    val networkState: LiveData<NetworkState> by lazy {
        dataUpbRepository.networkState
    }
}