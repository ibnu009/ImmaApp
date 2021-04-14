package com.pjb.immaapp.ui.usulanpermintaanbarang.supplier

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.pjb.immaapp.data.entity.upb.Supplier
import com.pjb.immaapp.data.repository.DataUpbRepository
import com.pjb.immaapp.handler.RabAddSupplierListener
import com.pjb.immaapp.service.webservice.RetrofitApp
import com.pjb.immaapp.utils.UploadListener
import io.reactivex.disposables.CompositeDisposable
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.lang.Exception

class TambahSupplierViewModel(
    private val dataUpbRepository: DataUpbRepository,
    private val compositeDisposable: CompositeDisposable
) : ViewModel() {

    var harga: String? = null
    var rabAddSupplierListener: RabAddSupplierListener? = null

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

    fun validateRabUpload(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        apiKey: String,
        token: String,
        idDetail: Int?,
        idSupplier: Int?,
        path: String?
    ) {
        Timber.d("Check result : harga : $harga, id_supplier = $idSupplier")
        when {
            harga.isNullOrEmpty() -> {
                rabAddSupplierListener?.onFailure("Masukkan Harga")
            }
            idSupplier == null -> {
                rabAddSupplierListener?.onFailure("Pilih Supplier terlebih dahulu")
            }
            idDetail == null -> {
                rabAddSupplierListener?.onFailure("Terjadi Error")
            }
            path.isNullOrEmpty() -> {
                rabAddSupplierListener?.onInitiating()
                dataUpbRepository.uploadMaterialWithoutFile(
                    compositeDisposable,
                    apiKey,
                    token,
                    idSupplier.toString(),
                    idDetail.toString(),
                    harga ?: ")",
                    object : UploadListener {
                        override fun onSuccess(message: String) {
                            rabAddSupplierListener?.onSuccess(message)
                        }

                        override fun onError(message: String) {
                           rabAddSupplierListener?.onFailure(message)
                        }

                    })
            }
            else -> {
                uploadSupplerWithImage(
                    context, lifecycleOwner, apiKey, token, idSupplier, idDetail, harga, path ?: ""
                )
            }
        }
    }

    private fun uploadSupplerWithImage(
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
                .addParameter("api_key", apiKey)
                .addParameter("token", token)
                .addParameter("vendor", vendor.toString())
                .addParameter("id_detail", idDetail.toString())
                .addParameter("harga", harga.toString())
                .addFileToUpload(path, "file")
                .setMaxRetries(2)
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
                        rabAddSupplierListener?.onSuccess("Upload Berhasil!")
                    }

                })
        } catch (ex: Exception) {
            Timber.d("Cause error : ${ex.message}")
            rabAddSupplierListener?.onFailure("Mohon maaf sedang ada trouble")
        }
    }

    private fun uploadSupplier(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        apiKey: String,
        token: String,
        vendor: Int?,
        idDetail: Int,
        harga: String?
    ) {
        try {
            Timber.d("Initiating upload with param : token $token, apiKey : $apiKey, idSupplier: $vendor, idDetail : $idDetail, harga: $harga")
            MultipartUploadRequest(context, RetrofitApp.UPLOAD_RAB_SUPPLIER)
                .setMethod("POST")
                .addParameter("api_key", apiKey)
                .addParameter("token", token)
                .addParameter("vendor", vendor.toString())
                .addParameter("id_detail", idDetail.toString())
                .addParameter("harga", harga.toString())
                .setMaxRetries(2)
                .addFileToUpload(" ", "file", "", "")
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
                        rabAddSupplierListener?.onSuccess("Success!")
                    }

                })
        } catch (ex: Exception) {
            Timber.d("Cause error : ${ex.message}")
            rabAddSupplierListener?.onFailure("Mohon maaf sedang ada trouble")
        }
    }

    val networkState by lazy {
        dataUpbRepository.getSupplierNetworkState()
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

}