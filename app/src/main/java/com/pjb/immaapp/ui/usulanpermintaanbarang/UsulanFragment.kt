package com.pjb.immaapp.ui.usulanpermintaanbarang

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentUsulanBinding
import com.pjb.immaapp.handler.OnClickedActionDataUpb
import com.pjb.immaapp.main.MainActivity
import com.pjb.immaapp.ui.usulanpermintaanbarang.adapter.DataUpbPagedListAdapter
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.utils.SharedPreferencesKey.KEY_TOKEN
import com.pjb.immaapp.utils.SharedPreferencesKey.PREFS_NAME
import com.pjb.immaapp.utils.ViewModelFactory
import timber.log.Timber


class UsulanFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var upbPagedListAdapter: DataUpbPagedListAdapter
    private lateinit var token: String

    private val upbViewModel by lazy {
        val factory = this.context?.applicationContext?.let { ViewModelFactory.getInstance(it) }
        factory?.let { ViewModelProvider(this, it).get(UsulanViewModel::class.java) }
    }

    private val onItemClicked = object : OnClickedActionDataUpb {
        override fun onClicked(idPermintaan: Int) {
            val action =
                UsulanFragmentDirections.actionNavUsulanToDetailUsulanPermintaanBarangFragment(
                    idPermintaan
                )
            findNavController().navigate(action)
        }
    }

    private var _bindingFragmentUpb: FragmentUsulanBinding? = null
    private val binding get() = _bindingFragmentUpb

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingFragmentUpb = FragmentUsulanBinding.inflate(inflater, container, false)

        return _bindingFragmentUpb?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        upbPagedListAdapter = DataUpbPagedListAdapter(onItemClicked)
        with(binding?.rvUsulan) {
            this?.adapter = upbPagedListAdapter
            this?.layoutManager =
                LinearLayoutManager(
                    this?.context?.applicationContext,
                    LinearLayoutManager.VERTICAL,
                    false
                )
        }

        sharedPreferences =
            activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        token =
            sharedPreferences.getString(KEY_TOKEN, "Not Found") ?: "Shared Prefences Not Found"

        binding?.shimmerViewContainer?.visibility = View.VISIBLE
        binding?.layoutEmptyList?.visibility = View.GONE

        binding?.fabTambah?.setOnClickListener {
            it.findNavController().navigate(R.id.action_nav_usulan_to_tambahUsulanFragment)
        }

        binding?.svUsulan?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                showData(token, p0)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0.isNullOrEmpty()) {
                    showData(token, null)
                }
                return false
            }
        })

        showData(token, null)

    }

    private fun showData(token: String, keywords: String?) {
        upbViewModel?.getListDataUpb(token, keywords)
            ?.observe(viewLifecycleOwner, { dataUpb ->
                    upbPagedListAdapter.submitList(dataUpb)
            })

        upbViewModel?.networkState?.observe(viewLifecycleOwner, { network ->
            when  {
               network == NetworkState.LOADING -> {
                    binding?.shimmerViewContainer?.visibility = View.VISIBLE
                    binding?.shimmerViewContainer?.startShimmer()
                }
                network == NetworkState.LOADED -> {
                    binding?.shimmerViewContainer?.stopShimmer()
                    binding?.shimmerViewContainer?.visibility = View.GONE
                }
                upbViewModel!!.listIsEmpty(token, keywords) ->{
                    binding?.layoutEmptyList?.visibility = View.VISIBLE
                }
                else -> {
                    Timber.e("Unknown Error")
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        showData(token, null)
    }

    override fun onPause() {
        super.onPause()
        binding?.shimmerViewContainer?.visibility = View.GONE
        binding?.shimmerViewContainer?.stopShimmer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingFragmentUpb = null
    }

}