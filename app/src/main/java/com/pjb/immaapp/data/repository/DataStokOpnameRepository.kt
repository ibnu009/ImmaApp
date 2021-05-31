package com.pjb.immaapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pjb.immaapp.data.entity.stockopname.StockOpname
import com.pjb.immaapp.data.remote.response.ResponseCreateStokOpname
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.utils.global.ImmaEventHandler
import com.pjb.immaapp.service.webservice.RetrofitApp
import com.pjb.immaapp.ui.stokopname.CreateStockListener
import com.pjb.immaapp.utils.ConverterHelper
import com.pjb.immaapp.utils.UploadListener
import com.pjb.immaapp.utils.utilsentity.GeneralErrorHandler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber

class DataStokOpnameRepository {
    private val apiService = RetrofitApp.getStockOpnameService()
    private val uploadService = RetrofitApp.getUploadService()
    val networkState = ImmaEventHandler<NetworkState>()

    companion object {
        @Volatile
        private var instance: DataStokOpnameRepository? = null
        fun getInstance(): DataStokOpnameRepository =
            instance ?: synchronized(this) {
                instance ?: DataStokOpnameRepository()
            }
    }

    fun requestGetDataStockOpname(
        compositeDisposable: CompositeDisposable,
        apiKey: String,
        token: String,
        itemNum: Int
    ): LiveData<StockOpname> {
        networkState.postValue(NetworkState.LOADING)
        val resultData = MutableLiveData<StockOpname>()
        compositeDisposable.add(
            apiService.getStockOpnameResponse(apiKey, token, itemNum)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map {
                    it.data
                }
                .subscribe(
                    {
                        resultData.postValue(it[0])
                        networkState.postValue(NetworkState.LOADED)
                    }, {
                        Timber.e(it)
                        val error = GeneralErrorHandler().getError(it)
                        networkState.postValue(error)
                    }
                ))
        return resultData
    }

    fun createStokOpname(
        compositeDisposable: CompositeDisposable,
        apiKey: String,
        token: String,
        itemNum: Int,
        notes: String,
        stock: Int,
        kondisi: String,
        status: CreateStockListener
    ) {
        compositeDisposable.add(
            apiService.postStokOpname(apiKey, token, itemNum, notes, stock, kondisi)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        status.onSuccess()
                    }, {
                        Timber.e("$it")
                        val errorMessage = ConverterHelper().convertExceptionToMessage(it)
                        status.onFailure(errorMessage)
                    }
                )
        )
    }

    fun uploadMaterialWithoutImage(
        compositeDisposable: CompositeDisposable,
        apiKey: String,
        token: String,
        itemNum: String,
        notes: String,
        qty: String,
        idPermintaan: String,
        lineType: String,
        status: UploadListener
    ) {

        val mToken = token.toRequestBody(MultipartBody.FORM)
        val mApiKey = apiKey.toRequestBody(MultipartBody.FORM)
        val mItemNum = itemNum.toRequestBody(MultipartBody.FORM)
        val mNotes = notes.toRequestBody(MultipartBody.FORM)
        val mQuantity = qty.toRequestBody(MultipartBody.FORM)
        val mIdPermintaan = idPermintaan.toRequestBody(MultipartBody.FORM)
        val mLineType = lineType.toRequestBody(MultipartBody.FORM)

        val fileBody = "".toRequestBody("image/png".toMediaTypeOrNull())
        val mFile = MultipartBody.Part.createFormData("img", "", fileBody)

        compositeDisposable.add(
            uploadService.uploadMaterial(
                token = mToken,
                apiKey = mApiKey,
                itemNum = mItemNum,
                notes = mNotes,
                qty = mQuantity,
                idPermintaan = mIdPermintaan,
                lineType = mLineType,
                img = mFile
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    status.onSuccess(it.message)
                }, {
                    val message = ConverterHelper().convertExceptionToMessage(it)
                    status.onError(message)
                })
        )
    }

}