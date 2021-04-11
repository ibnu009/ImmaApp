package com.pjb.immaapp.ui.usulanpermintaanbarang.tambah.material

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pjb.immaapp.data.entity.stockopname.StockOpname
import com.pjb.immaapp.data.repository.DataStokOpnameRepository
import com.pjb.immaapp.handler.UpbCreateMaterialListener
import com.pjb.immaapp.service.webservice.RetrofitApp
import com.pjb.immaapp.utils.UploadListener
import io.reactivex.disposables.CompositeDisposable
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import timber.log.Timber

class TambahMaterialViewModel(
    private val dataStokOpnameRepository: DataStokOpnameRepository,
    private val compositeDisposable: CompositeDisposable
) :
    ViewModel() {
    var jumlah: String? = null
    var catatan: String? = null

    var upbCreateMaterialListener: UpbCreateMaterialListener? = null

    fun getDataMaterial(
        apiKey: String,
        token: String,
        itemNum: Int
    ): LiveData<StockOpname> {
        return dataStokOpnameRepository.requestGetDataStockOpname(
            compositeDisposable,
            apiKey,
            token,
            itemNum
        )
    }

    fun validateMaterialUpload(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        apiKey: String,
        token: String,
        itemNum: String?,
        idPermintaan: Int,
        lineType: Int,
        path: String?
    ) {
        Timber.d("check Result. jumlah = $jumlah, catatan = $catatan")
        when {
            jumlah.isNullOrEmpty() -> {
                Timber.e("error jumlah, jumlah = $jumlah")
                upbCreateMaterialListener?.onFailure("Invalid Input pada Jumlah")
            }
            itemNum.isNullOrEmpty() -> {
                upbCreateMaterialListener?.onFailure("Masukkan material yang diperlukan")
            }
            path.isNullOrEmpty() -> {
                upbCreateMaterialListener?.onInitiating()
                dataStokOpnameRepository.uploadMaterialWithoutImage(
                    compositeDisposable,
                    apiKey,
                    token,
                    itemNum,
                    catatan ?: "-",
                    jumlah ?: "0",
                    idPermintaan.toString(),
                    lineType.toString(),
                    object : UploadListener{
                        override fun onSuccess(message: String) {
                            upbCreateMaterialListener?.onSuccess(message)
                        }
                        override fun onError(message: String) {
                            upbCreateMaterialListener?.onFailure(message)
                        }
                    }
                )
            }
            else -> {
                uploadMaterial(
                    context,
                    lifecycleOwner,
                    apiKey,
                    token,
                    itemNum,
                    catatan,
                    jumlah,
                    idPermintaan,
                    lineType,
                    path
                )
            }
        }
    }

    private fun uploadMaterial(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        apiKey: String,
        token: String,
        itemNum: String,
        notes: String?,
        qty: String?,
        idPermintaan: Int,
        lineType: Int,
        path: String
    ) {
        try {
            Timber.d("Initiating upload with Params: token = $token, \n apiKey = $apiKey, \n itemNum = $itemNum,\n notes = $notes,\n qty = $qty, \nidPermintaan = $idPermintaan,\n linetype = $lineType")
            Timber.d("Initiating upload with path = $path")
            MultipartUploadRequest(context, RetrofitApp.UPLOAD_MATERIAL_URL)
                .setMethod("POST")
                .addParameter("token", token)
                .addParameter("api_key", apiKey)
                .addParameter("itemnum", itemNum)
                .addParameter("notes", notes ?: "-")
                .addParameter("qty", qty.toString())
                .addParameter("id_permintaan", idPermintaan.toString())
                .addParameter("linetype", lineType.toString())
                .setMaxRetries(2)
                .addFileToUpload(path, "img", contentType = "image/*")
                .subscribe(context = context, lifecycleOwner = lifecycleOwner, delegate = object :
                    RequestObserverDelegate {
                    override fun onCompleted(context: Context, uploadInfo: UploadInfo) {
                        Timber.d("Material Sudah $uploadInfo")
                    }

                    override fun onCompletedWhileNotObserving() {
                        Timber.d("Material loh weh")
                    }

                    override fun onError(
                        context: Context,
                        uploadInfo: UploadInfo,
                        exception: Throwable
                    ) {
                        exception.stackTrace
                        upbCreateMaterialListener?.onFailure("Mohon maaf sedang ada trouble")
                    }

                    override fun onProgress(context: Context, uploadInfo: UploadInfo) {
                        Timber.d("Material lagi proses")
                        upbCreateMaterialListener?.onInitiating()
                    }

                    override fun onSuccess(
                        context: Context,
                        uploadInfo: UploadInfo,
                        serverResponse: ServerResponse
                    ) {
                        Timber.d("Material done terupload $uploadInfo,\n ${serverResponse.bodyString}")
                        upbCreateMaterialListener?.onSuccess("File telah terupload")
                    }
                })
        } catch (e: Exception) {
            Timber.e("Errorkarena $e")
            upbCreateMaterialListener?.onFailure("Mohon maaf sedang ada trouble")
        }
    }

    val networkState by lazy {
        dataStokOpnameRepository.networkState
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}