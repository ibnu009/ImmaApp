package com.pjb.immaapp.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pjb.immaapp.main.MainActivity
import com.pjb.immaapp.data.entity.User
import com.pjb.immaapp.data.entity.request.Credential
import com.pjb.immaapp.databinding.ActivityLoginBinding
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.ViewModelFactory
import timber.log.Timber

class LoginActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    private val viewModel by lazy {
        val factory = ViewModelFactory.getInstance()
        ViewModelProvider(this, factory).get(LoginViewModel::class.java)
    }

    private var _activtyLoginBinding: ActivityLoginBinding? = null
    val binding get() = _activtyLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activtyLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.edtUsername?.addTextChangedListener(textWatcher)
        binding?.edtPassword?.addTextChangedListener(textWatcher)

        sharedPreferences = this.getSharedPreferences(SharedPreferencesKey.PREFS_NAME, Context.MODE_PRIVATE)

        binding?.fabLogin?.setOnClickListener {
            viewModel.getLoginRequest(getCredential()).observe(this, Observer {
                Timber.d("Have user ${it.name}")
                insertIntoSharedPreference(getCredential(), it)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            })
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val username = binding?.edtUsername?.text.toString().trim()
            val password = binding?.edtPassword?.text.toString().trim()

            binding?.fabLogin?.isEnabled = username.isNotEmpty() && password.isNotEmpty()
        }

        override fun afterTextChanged(p0: Editable?) {

        }

    }

    private fun getCredential(): Credential {
        val username = binding?.edtUsername?.text.toString().trim()
        val password = binding?.edtPassword?.text.toString().trim()
        val apiKey = 12345

        return Credential(
            username = username,
            password = password,
            apiKey = apiKey.toString()
        )
    }

    private fun insertIntoSharedPreference(credential: Credential, user: User) {
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        with(editor){
            putString(SharedPreferencesKey.KEY_USERNAME, user.username)
            putString(SharedPreferencesKey.KEY_NAME, user.name)
            putString(SharedPreferencesKey.KEY_TOKEN, user.token)
            putString(SharedPreferencesKey.KEY_PASSWORD, credential.password)
            putString(SharedPreferencesKey.KEY_API, credential.apiKey)
            putBoolean(SharedPreferencesKey.KEY_LOGIN, true)
            apply()
        }
    }
}