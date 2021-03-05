package com.pjb.immaapp.utils.global

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pjb.immaapp.data.repository.DataPoRepository
import com.pjb.immaapp.data.repository.DataStokOpnameRepository
import com.pjb.immaapp.data.repository.DataUpbRepository
import com.pjb.immaapp.data.repository.LoginRepository
import com.pjb.immaapp.di.Injection
import com.pjb.immaapp.ui.gudangpermintaanbarang.GudangViewModel
import com.pjb.immaapp.ui.login.LoginViewModel
import com.pjb.immaapp.ui.purchaseorder.PurchaseOrderViewModel
import com.pjb.immaapp.ui.stokopname.StokOpnameViewModel
import com.pjb.immaapp.ui.usulanpermintaanbarang.UsulanViewModel
import com.pjb.immaapp.ui.usulanpermintaanbarang.tambah.material.TambahMaterialViewModel
import com.pjb.immaapp.ui.usulanpermintaanbarang.tambah.usulan.CreateUpbViewModel
import io.reactivex.disposables.CompositeDisposable

class ViewModelFactory(
    private val loginRepository: LoginRepository,
    private val dataPoRepository: DataPoRepository,
    private val dataUpbRepository: DataUpbRepository,
    private val dataStokOpnameRepository: DataStokOpnameRepository,
    private val compositeDisposable: CompositeDisposable,
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            (instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideLoginRepository(),
                    Injection.provideDataPoRepository(context),
                    Injection.provideDataUpbRepository(),
                    Injection.provideDataStokOpnameRepository(),
                    Injection.provideCompositeDisposable()
                )
            })
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(loginRepository, compositeDisposable) as T
            }
            modelClass.isAssignableFrom(UsulanViewModel::class.java) -> {
                UsulanViewModel(dataUpbRepository, compositeDisposable) as T
            }
            modelClass.isAssignableFrom(GudangViewModel::class.java) -> {
                GudangViewModel() as T
            }
            modelClass.isAssignableFrom(PurchaseOrderViewModel::class.java) -> {
                PurchaseOrderViewModel(dataPoRepository, compositeDisposable) as T
            }
            modelClass.isAssignableFrom(StokOpnameViewModel::class.java) -> {
                StokOpnameViewModel(dataStokOpnameRepository, compositeDisposable) as T
            }
            modelClass.isAssignableFrom(CreateUpbViewModel::class.java) -> {
                CreateUpbViewModel(compositeDisposable) as T
            }
            modelClass.isAssignableFrom(TambahMaterialViewModel::class.java) -> {
                TambahMaterialViewModel() as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }

}