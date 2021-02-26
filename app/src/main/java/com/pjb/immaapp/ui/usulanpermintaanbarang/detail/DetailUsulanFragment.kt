package com.pjb.immaapp.ui.usulanpermintaanbarang.detail

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentDetailUsulanBinding
import com.pjb.immaapp.main.MainActivity
import com.pjb.immaapp.main.MainViewModel
import com.pjb.immaapp.ui.usulanpermintaanbarang.UsulanViewModel
import com.pjb.immaapp.ui.usulanpermintaanbarang.adapter.DataItemUpbPagedListAdapter
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.SharedPreferencesKey.PREFS_NAME
import com.pjb.immaapp.utils.ViewModelFactory
import timber.log.Timber


class DetailUsulanFragment : Fragment() {

    companion object {
        const val EXTRA_ID_PERMINTAAN = "EXTRA_ID_UPB"
        private const val TITLE = "Detail Usulan Permintaan"
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var itemPagedListAdapter: DataItemUpbPagedListAdapter
    private lateinit var token: String
    private var idPermintaan: Int? = null

    private lateinit var viewModel: MainViewModel

    private val upbViewModel by lazy {
        val factory = this.context?.applicationContext?.let { ViewModelFactory.getInstance(it) }
        factory?.let { ViewModelProvider(this, it) }?.get(UsulanViewModel::class.java)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.run {
            viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        } ?: throw Throwable("invalid activity")
        viewModel.updateActionBarTitle("Detail Usulan Fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemPagedListAdapter = DataItemUpbPagedListAdapter()
        with(binding?.rvItemDataUpb) {
            this?.adapter = itemPagedListAdapter
            this?.layoutManager =
                LinearLayoutManager(
                    this?.context?.applicationContext,
                    LinearLayoutManager.VERTICAL,
                    false
                )
        }

        binding?.shimmerViewContainerDetailUpb?.visibility = View.VISIBLE
        binding?.shimmerViewContainerDetailUpbRv?.visibility = View.VISIBLE
        binding?.layoutKeteranganUpb?.visibility = View.INVISIBLE

        val safeArgs = arguments?.let { DetailUsulanFragmentArgs.fromBundle(it) }
        idPermintaan = safeArgs?.passIdPermintaan

        sharedPreferences =
            activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        token = sharedPreferences.getString(SharedPreferencesKey.KEY_TOKEN, "Not Found")
            ?: "Shared Preferences Not Found"

        initiateDetail(token, idPermintaan!!)
        initiateItemUpb(token, idPermintaan!!)
    }

    private fun initiateDetail(token: String, idPermintaan: Int) {
        upbViewModel?.getDetailDataUpb("12345", token, idPermintaan)
            ?.observe(viewLifecycleOwner, Observer {
                Timber.d("Check data $it")

                binding?.txNamaPemohon?.text = it.pemohon
                binding?.txJudulPekerjaan?.text = it.jobTitle
                binding?.txTanggalDibutuhkan?.text = it.tanggalDibutuhkan
                binding?.txTanggalPermohonan?.text = it.tanggalPermohonan
            })

        upbViewModel?.networkStateDetail?.observe(viewLifecycleOwner, Observer {
            if (it == NetworkState.LOADING) {
                binding?.shimmerViewContainerDetailUpb?.startShimmer()
            } else if (it == NetworkState.LOADED) {
                binding?.shimmerViewContainerDetailUpb?.stopShimmer()
                binding?.shimmerViewContainerDetailUpb?.visibility = View.GONE
                binding?.layoutKeteranganUpb?.visibility = View.VISIBLE
            }
        })
    }

    private fun initiateItemUpb(token: String, idPermintaan: Int) {
        upbViewModel?.getListItemUpb(token, idPermintaan)?.observe(viewLifecycleOwner, Observer {
            itemPagedListAdapter.submitList(it)
            Timber.d("Receive data $it")
        })

        upbViewModel?.netWorkItemUpb?.observe(viewLifecycleOwner, Observer {
            if (upbViewModel?.listItemIsEmpty(
                    token,
                    idPermintaan
                ) == true && it == NetworkState.LOADING
            ) {
                binding?.shimmerViewContainerDetailUpbRv?.startShimmer()
            } else {
                Timber.d("Network : $it")
                binding?.shimmerViewContainerDetailUpbRv?.stopShimmer()
                binding?.shimmerViewContainerDetailUpbRv?.visibility = View.GONE
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingFragmentDetailUsulan = null
    }

    override fun onResume() {
        super.onResume()
        initiateDetail(token, idPermintaan!!)
        initiateItemUpb(token, idPermintaan!!)
    }

}