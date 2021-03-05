package com.pjb.immaapp.ui.usulanpermintaanbarang.tambah.material

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.pjb.immaapp.handler.UpbCreateMaterialListener
import com.pjb.immaapp.webservice.RetrofitApp
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import timber.log.Timber

class TambahMaterialViewModel : ViewModel() {
    var jumlah: Int? = null
    var catatan: String? = null

    var upbCreateMaterialListener: UpbCreateMaterialListener? = null

    fun validateMaterialUpload(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        apiKey: String,
        token: String,
        itemNum: String,
        idPermintaan: Int,
        lineType: Int,
        path: String
    ) {
        when {
            jumlah == -1 || jumlah == null -> {
                upbCreateMaterialListener?.onFailure("Invalid Input pada Jumlah")
            }
            catatan.isNullOrEmpty() -> {
                upbCreateMaterialListener?.onFailure("Catatan tidak boleh kosong")
            }
            else -> {
                uploadMaterial(context, lifecycleOwner, apiKey, token, itemNum, catatan, jumlah ?: 0, idPermintaan, lineType, path)
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
        qty: Int,
        idPermintaan: Int,
        lineType: Int,
        path: String
    ) {
        try {
            Timber.d("Initiating upload")
            upbCreateMaterialListener?.onInitiating()
            MultipartUploadRequest(context, RetrofitApp.UPLOAD_URL)
                .setMethod("POST")
                .addParameter("token", token)
                .addParameter("api_key", apiKey)
                .addParameter("itemnum", itemNum)
                .addParameter("notes", notes ?: "-")
                .addParameter("qty", qty.toString())
                .addParameter("id_permintaan", idPermintaan.toString())
                .addParameter("linetype", lineType.toString())
                .setMaxRetries(2)
                .addFileToUpload(path, "img")
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
                        Timber.d("Material Error becauseof $exception")
                        upbCreateMaterialListener?.onFailure("Mohon maaf sedang ada trouble")
                    }

                    override fun onProgress(context: Context, uploadInfo: UploadInfo) {
                        Timber.d("Material lagi proses")
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
}