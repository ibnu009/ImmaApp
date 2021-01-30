package com.pjb.immaapp.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pjb.immaapp.MainActivity
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.request.Credential
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.ViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    private val viewModel by lazy {
        val factory = ViewModelFactory.getInstance()
        ViewModelProvider(this, factory).get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edt_username.addTextChangedListener(textWatcher)
        edt_password.addTextChangedListener(textWatcher)

        sharedPreferences = this.getSharedPreferences(SharedPreferencesKey.PREFS_NAME, Context.MODE_PRIVATE)

        fab_login.setOnClickListener {
            viewModel.getLoginRequest(getCredential()).observe(this, Observer {
                Timber.d("Have user ${it.username}")
                insertIntoSharedPreference(getCredential())
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            })
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val username = edt_username.text.toString().trim()
            val password = edt_password.text.toString().trim()

            fab_login.isEnabled = !username.isEmpty() && !password.isEmpty()
        }

        override fun afterTextChanged(p0: Editable?) {

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
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        with(editor){
            putString(SharedPreferencesKey.KEY_USERNAME, credential.username)
            putString(SharedPreferencesKey.KEY_PASSWORD, credential.password)
            putString(SharedPreferencesKey.KEY_API, credential.apiKey)
            putBoolean(SharedPreferencesKey.KEY_LOGIN, true)
            apply()
        }
    }
}