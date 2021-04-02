package com.pjb.immaapp.ui.usulanpermintaanbarang.supplier

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.pjb.immaapp.data.entity.upb.Supplier
import com.pjb.immaapp.data.repository.DataUpbRepository
import com.pjb.immaapp.handler.RabAddSupplierListener
import com.pjb.immaapp.webservice.RetrofitApp
import io.reactivex.disposables.CompositeDisposable
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import timber.log.Timber
import java.lang.Exception

class TambahSupplierViewModel(
    private val dataUpbRepository: DataUpbRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    var harga: String? = null
    var id_Supplier: Int? = null

    var rabAddSupplierListener: RabAddSupplierListener? = null

    fun selectedItem(idSupplier: Int) {
        id_Supplier = idSupplier
    }

    fun getListSupplier(
        apiKey: String,
        token: String,
    ): LiveData<PagedList<Supplier>> {
        return dataUpbRepository.requestListDataSupplier(
            compositeDisposable,
            apiKey,
            token
        )
    }

    private fun validateRabUpload(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        apiKey: String,
        token: String,
        idDetail: Int,
        path: String?
    ) {
        Timber.d("Check result : harga : $harga, id_supplier = $id_Supplier")
        when {
            harga.isNullOrEmpty() -> {
                Timber.e("Error harga, harga : $harga, id")
                rabAddSupplierListener?.onFailure("Harga tidak valid")
            }
            path.isNullOrEmpty() -> {
                rabAddSupplierListener?.onFailure("File wajib di upload")
            }
            id_Supplier.toString().isNullOrEmpty() -> {
                rabAddSupplierListener?.onFailure("Pilih Supplier terlebih dahulu")
            }
            else -> {
                uploadMaterial(
                    context, lifecycleOwner, apiKey, token, id_Supplier, idDetail, harga, path
                )
            }
        }
    }

    private fun uploadMaterial(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        apiKey: String,
        token: String,
        vendor: Int?,
        idDetail: Int,
        harga: String?,
        path: String
    ) {
        try {
            Timber.d("Initiating upload with param : token $token, apiKey : $apiKey, idSupplier: $vendor, idDetail : $idDetail, harga: $harga")
            Timber.d("Initiating upload with path : $path")
            MultipartUploadRequest(context, RetrofitApp.UPLOAD_RAB_SUPPLIER)
                .setMethod("POST")
                .addParameter("vendor", vendor.toString())
                .addParameter("id_detail", idDetail.toString())
                .addParameter("harga", harga.toString())
                .addParameter("api_key", apiKey)
                .addParameter("token", token)
                .setMaxRetries(2)
                .addFileToUpload(path, "docx", contentType = "doc/")
                .subscribe(context = context, lifecycleOwner = lifecycleOwner, delegate = object :
                    RequestObserverDelegate {
                    override fun onCompleted(context: Context, uploadInfo: UploadInfo) {
                        Timber.d("RAB Supplier sudah $uploadInfo")
                    }

                    override fun onCompletedWhileNotObserving() {
                        Timber.d("RAB complete while not observing")
                    }

                    override fun onError(
                        context: Context,
                        uploadInfo: UploadInfo,
                        exception: Throwable
                    ) {
                        exception.stackTrace
                        rabAddSupplierListener?.onFailure("Mohon maaf sedang ada gangguan")
                    }

                    override fun onProgress(context: Context, uploadInfo: UploadInfo) {
                        Timber.d("RAB Supplier sedang diupload")
                        rabAddSupplierListener?.onInitiating()
                    }

                    override fun onSuccess(
                        context: Context,
                        uploadInfo: UploadInfo,
                        serverResponse: ServerResponse
                    ) {
                        Timber.d("RAB sudah terupload $uploadInfo, \n ${serverResponse.bodyString}")
                    }

                })
        } catch (ex: Exception) {
            Timber.d("Cause error : ${ex.message}")
            rabAddSupplierListener?.onFailure("Mohon maaf sedang ada trouble")
        }
    }

    val networkState by lazy {
        dataUpbRepository.networkState
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}