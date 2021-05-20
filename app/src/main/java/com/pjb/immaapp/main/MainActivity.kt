package com.pjb.immaapp.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.notification.NotificationModel
import com.pjb.immaapp.ui.login.LoginViewModel
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.global.ViewModelFactory
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var viewModel: MainViewModel
    private lateinit var listToken: List<NotificationModel>
    private lateinit var apiKey: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val factory = ViewModelFactory.getInstance(this.applicationContext)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        sharedPreferences = this.getSharedPreferences(SharedPreferencesKey.PREFS_NAME, Context.MODE_PRIVATE)
        val token: String =
            sharedPreferences.getString(SharedPreferencesKey.KEY_TOKEN, "Not Found")?: "Shared Preference Not Found"
        val name: String =
            sharedPreferences.getString(SharedPreferencesKey.KEY_NAME, "Not Found")?: "Shared Preference Not Found"
        val idUser: String =
            sharedPreferences.getString(SharedPreferencesKey.KEY_ID_SDM, "Not Found")?: "Shared Preference Not Found"

        initiateToken(token, name, idUser)
    }

    private fun initiateToken(token:String, name: String, idUser: String) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Timber.e("Fetching FCM registration token failed, %s", task.exception)
                return@OnCompleteListener
            }

            apiKey = "12345"

            val tokenFcm = task.result
            Timber.d("Token FCM adalah $tokenFcm")

            viewModel.getFcmTokenFromServer(apiKey, token).observe(this, Observer {
                Timber.d("size is ${it.size}")
                for (i in it.indices) {
                    if (it[i].idUser.toString() == idUser) {
                        Timber.d("Token is already stored, not Storing data")
                        break
                    }
                    saveToken(apiKey, token, name, idUser, tokenFcm?:"-")
                }
            })
        })
    }

    private fun saveToken(apiKey: String, token: String, name: String, idUser: String, tokenFcm: String) {
        Timber.d("Token $token")
        viewModel.storeFcmTokenToServer(apiKey, token, name, idUser, tokenFcm)
    }

}