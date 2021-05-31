package com.pjb.immaapp.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pjb.immaapp.main.MainActivity
import com.pjb.immaapp.data.entity.User
import com.pjb.immaapp.data.entity.request.Credential
import com.pjb.immaapp.databinding.ActivityLoginBinding
import com.pjb.immaapp.utils.*
import com.pjb.immaapp.utils.NetworkState.Companion.USERNOTFOUND
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.utils.NetworkState.Companion.BAD_GATEAWAY
import com.pjb.immaapp.utils.NetworkState.Companion.EXPIRETOKEN
import com.pjb.immaapp.utils.NetworkState.Companion.LOADED
import com.pjb.immaapp.utils.NetworkState.Companion.LOADING
import com.pjb.immaapp.utils.NetworkState.Companion.UNKNOWN
import com.pjb.immaapp.utils.global.ConstVal
import com.pjb.immaapp.utils.global.ViewModelFactory
import com.pjb.immaapp.utils.global.snackbar
import timber.log.Timber

class LoginActivity : AppCompatActivity(), AuthListener, LogInHandler {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var viewModel: LoginViewModel

    private var _activityLoginBinding: ActivityLoginBinding? = null
    val binding get() = _activityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        binding?.lifecycleOwner = this

        setContentView(binding?.root)

        sharedPreferences =
            this.getSharedPreferences(SharedPreferencesKey.PREFS_NAME, Context.MODE_PRIVATE)
        binding?.progressLogin?.visibility = View.GONE

        val factory = ViewModelFactory.getInstance(this.applicationContext)
        viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        binding?.viewModel = viewModel
        viewModel.authListener = this
        binding?.handler = this
    }


    private fun insertIntoSharedPreference(credential: Credential, user: User) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        with(editor) {
            putString(SharedPreferencesKey.KEY_USERNAME, user.username)
            putString(SharedPreferencesKey.KEY_NAME, user.name)
            putString(SharedPreferencesKey.KEY_TOKEN, user.token)
            putString(SharedPreferencesKey.KEY_ID_SDM, user.idSdm)
            putString(SharedPreferencesKey.KEY_PASSWORD, credential.password)
            putString(SharedPreferencesKey.KEY_API, credential.apiKey)
            putBoolean(SharedPreferencesKey.KEY_LOGIN, true)
            apply()
        }
    }

    override fun onAuthenticating() {
        Timber.d("Initiating MVVM Login Loading")
    }

    override fun onSuccess(credential: Credential, responseSuccess: LiveData<User>) {
        Timber.d("MVVM Login Logged")
        responseSuccess.observe(this, Observer { user ->
                insertIntoSharedPreference(credential, user)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
        })
    }

    override fun onStop() {
        super.onStop()
        _activityLoginBinding = null
    }

    override fun onFailure(message: String, type: Int) {
        when (type) {
            ConstVal.ERROR_USERNAME_EMPTY -> {
                binding?.edtUsername?.error = "Username tidak boleh kosong"
            }
            ConstVal.ERROR_PASSWORD_EMPTY -> {
                binding?.edtPassword?.error = "Password tidak boleh kosong"
            }
        }
    }

    override fun onLogInClicked(view: View) {
        Timber.d("clicked")
        viewModel.logInAndStoreResult()

        viewModel.checkState().observe(this, Observer { network ->
            Timber.d("check result : ${network.status}")
            when (network) {
                LOADING -> {
                    isLoading(true)
                }
                LOADED -> {
                    isLoading(false)
                }
//                HTTP Error
                else -> {
                    ConverterHelper().convertNetworkStateErrorToSnackbar(binding?.root, network)
                    isLoading(false)
                }
            }
        })
    }

    private fun isLoading(status: Boolean) {

        if (!status) {
            binding?.progressLogin?.visibility = View.GONE
            binding?.backgroundDim?.visibility = View.GONE
            binding?.edtUsername?.isEnabled = true
            binding?.edtPassword?.isEnabled = true
        } else {
            binding?.progressLogin?.visibility = View.VISIBLE
            binding?.backgroundDim?.visibility = View.VISIBLE
            binding?.edtUsername?.isEnabled = false
            binding?.edtPassword?.isEnabled = false
        }
    }
}