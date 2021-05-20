package com.pjb.immaapp.di

import android.content.Context
import com.pjb.immaapp.data.local.db.ImmaDatabase
import com.pjb.immaapp.data.repository.DataPoRepository
import com.pjb.immaapp.data.repository.DataStokOpnameRepository
import com.pjb.immaapp.data.repository.DataUpbRepository
import com.pjb.immaapp.data.repository.LoginRepository
import com.pjb.immaapp.main.MainRepository
import io.reactivex.disposables.CompositeDisposable

object Injection {
    //    Bagian untuk menyediakan semua kebutuhan viewmodel
    private fun provideDatabase(context: Context): ImmaDatabase = ImmaDatabase.getDataBase(context)

    fun provideLoginRepository(): LoginRepository = LoginRepository.getInstance()
    fun provideDataPoRepository(context: Context): DataPoRepository = DataPoRepository.getInstance(provideDatabase(context))
    fun provideDataStokOpnameRepository() : DataStokOpnameRepository = DataStokOpnameRepository.getInstance()
    fun provideMainRepository(): MainRepository = MainRepository.getInstance()

    fun provideDataUpbRepository(context: Context): DataUpbRepository = DataUpbRepository.getInstance(provideDatabase(context))
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()
}