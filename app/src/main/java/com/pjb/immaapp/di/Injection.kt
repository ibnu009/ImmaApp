package com.pjb.immaapp.di

import com.pjb.immaapp.data.repository.DataPoRepository
import com.pjb.immaapp.data.repository.DataUpbRepository
import com.pjb.immaapp.data.repository.LoginRepository
import io.reactivex.disposables.CompositeDisposable

object Injection {
//    Bagian untuk menyediakan semua kebutuhan viewmodel
    fun provideLoginRepository(): LoginRepository = LoginRepository.getInstance()
    fun provideDataPoRepository(): DataPoRepository = DataPoRepository.getInstance()
    fun provideDataUpbRepository(): DataUpbRepository = DataUpbRepository.getInstance()

    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

}