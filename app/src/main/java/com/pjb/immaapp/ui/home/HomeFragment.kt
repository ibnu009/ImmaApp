package com.pjb.immaapp.ui.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentHomeBinding
import com.pjb.immaapp.ui.login.LoginActivity
import com.pjb.immaapp.utils.SharedPreferencesKey.KEY_NAME
import com.pjb.immaapp.utils.SharedPreferencesKey.PREFS_NAME
import com.pjb.immaapp.utils.ViewModelFactory
import timber.log.Timber

class HomeFragment : Fragment() {

    private val viewModel by lazy {
        val factory = ViewModelFactory.getInstance()
        ViewModelProvider(this, factory).get(HomeViewModel::class.java)
    }

    private lateinit var sharedPreferences: SharedPreferences
    private var _bindingHomeFragment: FragmentHomeBinding? = null
    private val binding get() = _bindingHomeFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingHomeFragment = FragmentHomeBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        val userName: String = sharedPreferences.getString(KEY_NAME, "Not Found") ?: "Shared Preference Not Found"
        Timber.d("User is $userName")
        tx_nama.text = userName

        btn_logout.setOnClickListener {
            openLogoutDialog()
        }

        btn_po.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_home_to_nav_po)
        }

        btn_usulan.setOnClickListener{
            it.findNavController().navigate(R.id.action_nav_home_to_nav_usulan)
        }
    }

    private fun openLogoutDialog() {
        val alertDialog = AlertDialog.Builder(this.context)
        alertDialog.setTitle("Logout?")
            .setPositiveButton("Logout"
            ) { _, _ -> logout() }
            .setNegativeButton("Cancel", null)
        val alert = alertDialog.create()
        alert.show()
    }

    private fun logout() {
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear().apply()
        val intent = Intent(this.context, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingHomeFragment = null
    }
}