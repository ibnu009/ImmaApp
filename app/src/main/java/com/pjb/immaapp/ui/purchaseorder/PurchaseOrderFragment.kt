package com.pjb.immaapp.ui.purchaseorder

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pjb.immaapp.R
import com.pjb.immaapp.ui.purchaseorder.adapter.PurchaseOrderAdapter
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.SharedPreferencesKey.KEY_API
import com.pjb.immaapp.utils.SharedPreferencesKey.KEY_TOKEN
import com.pjb.immaapp.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_po.*

class PurchaseOrderFragment : Fragment() {

    private lateinit var purchaseOrderAdapter: PurchaseOrderAdapter

    private lateinit var sharedPreferences: SharedPreferences

    private val purchaseOrderViewModel by lazy {
        val factory = ViewModelFactory.getInstance()
        ViewModelProvider(this, factory).get(PurchaseOrderViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_po, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shimmer_view_container.startShimmer()

        sharedPreferences = activity?.getSharedPreferences(SharedPreferencesKey.PREFS_NAME, Context.MODE_PRIVATE)!!
        val apiKey = sharedPreferences.getString(KEY_API, "12345") ?: "Shared Preference Not Found"
        val token = sharedPreferences.getString(KEY_TOKEN,"Not Found") ?: "Shared Preference Not Found"

        purchaseOrderViewModel.getListDataPo(apiKey, token).observe(viewLifecycleOwner, Observer {

        })
    }
}