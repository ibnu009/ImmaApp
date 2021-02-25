package com.pjb.immaapp.ui.login

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pjb.immaapp.main.MainActivity
import com.pjb.immaapp.data.entity.User
import com.pjb.immaapp.data.entity.request.Credential
import com.pjb.immaapp.databinding.ActivityLoginBinding
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.Status
import com.pjb.immaapp.utils.ViewModelFactory
import timber.log.Timber

class LoginActivity : AppCompatActivity(), AuthListener, LogInHandler {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var viewModel: LoginViewModel

//    private val viewModel by lazy {
//
//    }

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


//        binding?.fabLogin?.setOnClickListener {
//
////            viewModel.getLoginRequest(getCredential()).observe(this, Observer {user ->
////
////                Timber.d("Have user ${user.name}")
////                insertIntoSharedPreference(getCredential(), user)
////                val intent = Intent(this, MainActivity::class.java)
////                startActivity(intent)
////                finish()
////            })
//
////            viewModel.networkState.observe(this, Observer {
////                when (it) {
////                    NetworkState.USERNOTFOUND -> {
////                        Timber.d("checkCalled UNF")
////                        val title = "User Not Found"
////                        initiateLoginDialog(
////                            NetworkState.USERNOTFOUND,
////                            "Please enter correct username or password",
////                            title
////                        )
////                    }
////                    NetworkState.LOADED -> {
////                        binding?.progressLogin?.visibility = View.GONE
////                    }
////                    NetworkState.LOADING -> {
////                        Timber.d("checkCalled Loading")
////                        initiateLoginDialog(NetworkState.LOADING, null, "Authenticating..")
////                    }
////                    else -> {
////                        Timber.e("Unknown Error")
////                    }
////                }
////            })
//
//            viewModel.getNetworkState.observe(this, Observer {
//                it.getContentIfNotHandled()?.let { network ->
//                    Timber.d("check result : ${network.status}")
//                    Timber.d("checkHandler : ${it.hasBeenHandled}")
//                    when (network) {
//                        NetworkState.USERNOTFOUND -> {
//                            val title = "User Not Found"
//                            initiateLoginDialog(
//                                NetworkState.USERNOTFOUND,
//                                "Please enter correct username or password",
//                                title
//                            )
//                        }
//                        NetworkState.LOADED -> {
//                            binding?.progressLogin?.visibility = View.GONE
//                        }
//                        NetworkState.LOADING -> {
//                            initiateLoginDialog(NetworkState.LOADING, null, "Authenticating..")
//                            Timber.d("checkHandler : ${it.hasBeenHandled}")
//                        }
//                        else -> {
//                            Timber.e("Unknown Error")
//                        }
//                    }
//                }
//            })
//        }
    }


    private fun insertIntoSharedPreference(credential: Credential, user: User) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        with(editor) {
            putString(SharedPreferencesKey.KEY_USERNAME, user.username)
            putString(SharedPreferencesKey.KEY_NAME, user.name)
            putString(SharedPreferencesKey.KEY_TOKEN, user.token)
            putString(SharedPreferencesKey.KEY_PASSWORD, credential.password)
            putString(SharedPreferencesKey.KEY_API, credential.apiKey)
            putBoolean(SharedPreferencesKey.KEY_LOGIN, true)
            apply()
        }
    }

    private fun initiateLoginDialog(status: NetworkState, message: String?, title: String?) {
        val dialogLoadingBuilder =
            AlertDialog.Builder(this).apply {
                setTitle(title)
            }
        val dialogLoading: AlertDialog = dialogLoadingBuilder.create()


        val dialogWrongCredentialBuilder = AlertDialog.Builder(this).apply {
            setMessage(message)
            setTitle(title)
            setPositiveButton(
                "OK"
            ) { p0, _ -> p0?.dismiss() }
            setCancelable(false)
        }
        val dialogWrongCredential: AlertDialog = dialogWrongCredentialBuilder.create()

        dialogLoading.dismiss()
        dialogWrongCredential.dismiss()

        when (status) {
            NetworkState.LOADING -> {
                dialogLoading.show()
                dialogWrongCredential.dismiss()
            }
            NetworkState.USERNOTFOUND -> {
                dialogLoading.dismiss()
                dialogWrongCredential.show()
            }
            NetworkState.LOADED -> {
                dialogLoading.dismiss()
                dialogWrongCredential.dismiss()
            }
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

    override fun onFailure(message: String) {
        Timber.d(message)
    }

    override fun onLogInClicked(view: View) {
        Timber.d("clicked")
        viewModel.logInAndStoreResult()
    }
}