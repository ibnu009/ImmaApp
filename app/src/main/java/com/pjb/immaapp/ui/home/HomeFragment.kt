package com.pjb.immaapp.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.pjb.immaapp.R
import com.pjb.immaapp.utils.SharedPreferencesKey
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        sharedPreferences = context?.getSharedPreferences(SharedPreferencesKey.PREFS_NAME, Context.MODE_PRIVATE)!!
        val nama = sharedPreferences.getString(SharedPreferencesKey.KEY_USERNAME, null)
        root.tx_nama.text = nama

        return root
    }
}