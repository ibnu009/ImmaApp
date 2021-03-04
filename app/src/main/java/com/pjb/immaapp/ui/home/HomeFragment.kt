package com.pjb.immaapp.ui.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentHomeBinding
import com.pjb.immaapp.ui.login.LoginActivity
import com.pjb.immaapp.utils.SharedPreferencesKey.KEY_NAME
import com.pjb.immaapp.utils.SharedPreferencesKey.PREFS_NAME
import com.pjb.immaapp.utils.ViewModelFactory
import timber.log.Timber

class HomeFragment : Fragment() {

    private val viewModel by lazy {
        val factory = this.context?.applicationContext?.let { ViewModelFactory.getInstance(it) }
        factory?.let { ViewModelProvider(this, it).get(HomeViewModel::class.java) }
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

        (activity as AppCompatActivity).supportActionBar?.hide()

        return _bindingHomeFragment?.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        val userName: String =
            sharedPreferences.getString(KEY_NAME, "Not Found") ?: "Shared Preference Not Found"
        Timber.d("User is $userName")
        binding?.txNama?.text = userName

        binding?.btnLogout?.setOnClickListener {
            openLogoutDialog()
        }

        binding?.btnPo?.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_home_to_nav_po)
        }

        binding?.btnStockOpname?.setOnClickListener{
            it.findNavController().navigate(R.id.action_nav_home_to_nav_opname)
        }

        binding?.btnUsulan?.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_home_to_nav_usulan)
        }

        binding?.btnToNotification?.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_home_to_notificationFragment)
        }
    }

    private fun openLogoutDialog() {
        val alertDialog = AlertDialog.Builder(this.context)
        alertDialog.setTitle("Logout?")
            .setPositiveButton(
                "Logout"
            ) { _, _ -> logout() }
            .setNegativeButton("Cancel", null)
        val alert = alertDialog.create()
        alert.show()
    }

    private fun logout() {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.clear().apply()
        val intent = Intent(this.context, LoginActivity::class.java)
        activity?.finish()
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingHomeFragment = null
    }

}