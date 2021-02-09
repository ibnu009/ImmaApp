package com.pjb.immaapp.di

import android.app.Application
import android.content.Context
import com.pjb.immaapp.data.local.db.ImmaDatabase
import com.pjb.immaapp.data.mediator.DataPoMediator
import com.pjb.immaapp.data.repository.DataPoRepository
import com.pjb.immaapp.data.repository.DataUpbRepository
import com.pjb.immaapp.data.repository.LoginRepository
import com.pjb.immaapp.utils.PoMapper
import com.pjb.immaapp.webservice.RetrofitApp
import com.pjb.immaapp.webservice.po.PurchaseOrderService
import io.reactivex.disposables.CompositeDisposable

object Injection {
    //    Bagian untuk menyediakan semua kebutuhan viewmodel
    private fun provideDatabase(context: Context): ImmaDatabase = ImmaDatabase.getDataBase(context)
    private fun provideDataPoApiService(): PurchaseOrderService =
        RetrofitApp.getPurchaseOrderService()

    fun provideLoginRepository(): LoginRepository = LoginRepository.getInstance()
    fun provideDataPoRepository(
        context: Context,
        token: String?,
        keyword: String?
    ): DataPoRepository? =
        token?.let {
            DataPoMediator(
                provideDatabase(context),
                apiKey = "12345",
                apiService = provideDataPoApiService(),
                token = it,
                keywords = keyword,
                mapper = PoMapper()
            )
        }?.let {
            DataPoRepository.getInstance(
                database = provideDatabase(context),
                it
            )
        }

    fun provideDataUpbRepository(): DataUpbRepository = DataUpbRepository.getInstance()

    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

}