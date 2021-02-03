package com.pjb.immaapp.ui.usulanpermintaanbarang

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pjb.immaapp.R
import com.pjb.immaapp.ui.usulanpermintaanbarang.adapter.DataUpbPagedListAdapter
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.utils.SharedPreferencesKey.KEY_TOKEN
import com.pjb.immaapp.utils.SharedPreferencesKey.PREFS_NAME
import com.pjb.immaapp.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_usulan.*


class UsulanFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var upbPagedListAdapter : DataUpbPagedListAdapter

    private val viewModel by lazy{
        val factory = ViewModelFactory.getInstance()
        ViewModelProvider(this, factory).get(UsulanViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_usulan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        upbPagedListAdapter = DataUpbPagedListAdapter()

        with(rv_usulan){
            adapter = upbPagedListAdapter
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        }
        sharedPreferences =
            activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!

        val token =
            sharedPreferences.getString(KEY_TOKEN, "Not Found") ?: "Shared Prefences Not Found"

        shimmer_view_container.visibility = View.VISIBLE

        showData(token, null)

    }

    private fun showData(token: String, keywords: String?) {
        viewModel.getListDataUpb(token, keywords)
            .observe(viewLifecycleOwner, { dataUpb ->
                upbPagedListAdapter.submitList(dataUpb)
            })

        viewModel.networkState.observe(viewLifecycleOwner, { network ->
            if (viewModel.listIsEmpty(
                    token, keywords
                ) && network == NetworkState.LOADING
            ) {
                shimmer_view_container.startShimmer()
            } else {
                shimmer_view_container.stopShimmer()
                shimmer_view_container.visibility = View.GONE
            }
        })
    }

    override fun onResume() {
        super.onResume()
        shimmer_view_container.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        shimmer_view_container.startShimmer()
    }

}