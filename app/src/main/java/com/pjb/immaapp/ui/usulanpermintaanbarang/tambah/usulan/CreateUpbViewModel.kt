package com.pjb.immaapp.ui.usulanpermintaanbarang.tambah.usulan

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.pjb.immaapp.handler.UpbFileUploadListener
import com.pjb.immaapp.service.webservice.RetrofitApp.Companion.UPLOAD_URL
import io.reactivex.disposables.CompositeDisposable
import net.gotev.uploadservice.data.UploadInfo
import net.gotev.uploadservice.network.ServerResponse
import net.gotev.uploadservice.observer.request.RequestObserverDelegate
import net.gotev.uploadservice.protocols.multipart.MultipartUploadRequest
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber

class CreateUpbViewModel(private val compositeDisposable: CompositeDisposable) : ViewModel() {

    var description: String? = null
    var notes: String? = null
    var critical: Int? = null
    var upbFileUploadListener: UpbFileUploadListener? = null

    fun checkField(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        token: String,
        apiKey: String,
        path: String?,
        requiredDate: String?,
        idSdm: String
    ) {

        Timber.d("checking date: $requiredDate, desc: $description, note: $notes, critical: $critical")
        when {
            requiredDate.isNullOrEmpty() -> {
                upbFileUploadListener?.onFailure("Tanggal dibutuhkan tidak boleh kosong")
            }
            description.isNullOrEmpty() -> {
                upbFileUploadListener?.onFailure("Deskripsi pekerjaan tidak boleh kosong")
            }
            description.isNullOrEmpty() && requiredDate.isNullOrEmpty() -> {
                upbFileUploadListener?.onFailure("Tanggal dan Deskripsi tidak boleh kosong")
            }
            description?.length?:0 < 10 -> {
                upbFileUploadListener?.onFailure("Deskripsi pekerjaan minimal 10 huruf")
            }
            else -> {
                uploadFile(context, lifecycleOwner, token, apiKey, path?:"", requiredDate,idSdm)
            }
        }
    }

    private fun uploadFile(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        token: String,
        apiKey: String,
        path: String,
        requiredDate: String,
        idSdm: String
    ) {
        try {
            Timber.d("Initiating upload with path = $path")
            MultipartUploadRequest(context, UPLOAD_URL)
                .setMethod("POST")
                .addParameter("token", token)
                .addParameter("api_key", apiKey)
                .addParameter("requireddate", requiredDate)
                .addParameter("description", description!!)
                .addParameter("notes", notes ?: "-")
                .addParameter("critical", critical.toString())
                .addParameter("id_sdm", idSdm)
                .setMaxRetries(2)
                .addFileToUpload(path, "file")
                .subscribe(context = context, lifecycleOwner = lifecycleOwner, delegate = object :
                    RequestObserverDelegate {
                    override fun onCompleted(context: Context, uploadInfo: UploadInfo) {
                        Timber.d("Barang Sudah $uploadInfo")
                    }

                    override fun onCompletedWhileNotObserving() {
                        Timber.d("Barang loh weh")
                    }

                    override fun onError(
                        context: Context,
                        uploadInfo: UploadInfo,
                        exception: Throwable
                    ) {
                        Timber.d("Barang Error becauseof $exception")
                        upbFileUploadListener?.onFailure("Mohon maaf sedang ada trouble")
                    }

                    override fun onProgress(context: Context, uploadInfo: UploadInfo) {
                        Timber.d("Barang lagi proses")
                        upbFileUploadListener?.onInitiating()
                    }

                    override fun onSuccess(
                        context: Context,
                        uploadInfo: UploadInfo,
                        serverResponse: ServerResponse
                    ) {
                        Timber.d("Barang done terupload $uploadInfo,\n ${serverResponse.bodyString}")
                        var idPermintaan: Int? = null
                        try {
                            val jsonObject = JSONObject(serverResponse.bodyString)
                            idPermintaan = jsonObject.getInt("id_permintaan")
                        } catch (e: JSONException) {
                            Timber.e("json error $e")
                        }
                        upbFileUploadListener?.onSuccess("File telah terupload", idPermintaan)
                    }
                })
        } catch (e: Exception) {
            Timber.e("Errorkarena $e")
            upbFileUploadListener?.onFailure("Mohon maaf sedang ada trouble")
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}