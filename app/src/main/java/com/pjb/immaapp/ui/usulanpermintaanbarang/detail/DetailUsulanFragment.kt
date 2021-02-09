package com.pjb.immaapp.ui.usulanpermintaanbarang.detail

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pjb.immaapp.databinding.FragmentDetailUsulanBinding
import com.pjb.immaapp.ui.usulanpermintaanbarang.UsulanViewModel
import com.pjb.immaapp.ui.usulanpermintaanbarang.adapter.DataItemUpbPagedListAdapter
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.ViewModelFactory
import timber.log.Timber

class DetailUsulanFragment : Fragment() {

    companion object {
        const val EXTRA_ID_PERMINTAAN = "EXTRA_ID_UPB"
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var itemPagedListAdapter: DataItemUpbPagedListAdapter
    private lateinit var token: String


    private val upbViewModel by lazy {
        val factory = ViewModelFactory.getInstance(requireContext(), token, null)
        ViewModelProvider(this, factory)[UsulanViewModel::class.java]
    }

    private var _bindingFragmentDetailUsulan: FragmentDetailUsulanBinding? = null
    private val binding get() = _bindingFragmentDetailUsulan

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingFragmentDetailUsulan =
            FragmentDetailUsulanBinding.inflate(inflater, container, false)
        return _bindingFragmentDetailUsulan?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemPagedListAdapter = DataItemUpbPagedListAdapter()
        with(binding?.rvItemDataUpb) {
            this?.adapter = itemPagedListAdapter
            this?.layoutManager =
                LinearLayoutManager(this?.context, LinearLayoutManager.VERTICAL, false)
        }

        binding?.shimmerViewContainerDetailUpb?.visibility = View.VISIBLE
        binding?.shimmerViewContainerDetailUpbRv?.visibility = View.VISIBLE

        val safeArgs = arguments?.let { DetailUsulanFragmentArgs.fromBundle(it) }
        val idPermintaan = safeArgs?.passIdPermintaan

        sharedPreferences =
            activity?.getSharedPreferences(SharedPreferencesKey.PREFS_NAME, Context.MODE_PRIVATE)!!
        token = sharedPreferences.getString(SharedPreferencesKey.KEY_TOKEN, "Not Found")
            ?: "Shared Preferences Not Found"

        initiateDetail(token, idPermintaan!!)
        initiateItemUpb(token, idPermintaan)
    }

    private fun initiateDetail(token: String, idPermintaan: Int) {
        upbViewModel.getDetailDataUpb("12345", token, idPermintaan)
            .observe(viewLifecycleOwner, Observer {
                Timber.d("Check data $it")
                binding?.txNamaPemohon?.text = it.pemohon
                binding?.txJudulPekerjaan?.text = it.jobTitle
                binding?.txTanggalDibutuhkan?.text = it.tanggalDibutuhkan
                binding?.txTanggalPermohonan?.text = it.tanggalPermohonan

            })

        upbViewModel.networkStateDetail.observe(viewLifecycleOwner, Observer {
            if (upbViewModel.listItemIsEmpty(token, idPermintaan) && it == NetworkState.LOADING) {
                binding?.shimmerViewContainerDetailUpb?.startShimmer()
            } else {
                binding?.shimmerViewContainerDetailUpb?.stopShimmer()
                binding?.shimmerViewContainerDetailUpb?.visibility = View.GONE
                binding?.layoutKeteranganUpb?.visibility = View.VISIBLE
                binding?.txDetailUsulan?.visibility = View.VISIBLE
            }
        })
    }

    private fun initiateItemUpb(token: String, idPermintaan: Int) {
        upbViewModel.getListItemUpb(token, idPermintaan).observe(viewLifecycleOwner, Observer {
            itemPagedListAdapter.submitList(it)
            Timber.d("Receive data $it")
        })

        upbViewModel.networkStateDetail.observe(viewLifecycleOwner, Observer {
            if (upbViewModel.listItemIsEmpty(token, idPermintaan) && it == NetworkState.LOADING){
                binding?.shimmerViewContainerDetailUpbRv?.startShimmer()
            } else {
                binding?.shimmerViewContainerDetailUpbRv?.stopShimmer()
                binding?.shimmerViewContainerDetailUpbRv?.visibility = View.GONE
            }
        })
    }

}