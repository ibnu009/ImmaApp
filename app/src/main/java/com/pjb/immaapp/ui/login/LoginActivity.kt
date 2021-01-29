package com.pjb.immaapp.ui.login

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.request.Credential
import com.pjb.immaapp.utils.ViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    private val viewModel by lazy {
        val factory = ViewModelFactory.getInstance()
        ViewModelProvider(this, factory).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        fab_login.setOnClickListener {
            viewModel.getLoginRequest(getCredential()).observe(this, Observer {
                Timber.d("Have user ${it.username}")
            })
        }
    }


    private fun getCredential(): Credential {
        val username = edt_username.text.toString().trim()
        val password = edt_password.text.toString().trim()
        val apiKey = 12345
        return Credential(
            username = username,
            password = password,
            apiKey = apiKey.toString()
        )
    }

    private fun insertIntoSharedPreference(credential: Credential) {
//        TODO:(Membuat fungsi memasukkan data ke shared preference)
    }
}