package com.pjb.immaapp.ui.usulanpermintaanbarang.detail

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentDetailUsulanBinding
import com.pjb.immaapp.handler.OnClickedActionDataPo
import com.pjb.immaapp.main.MainViewModel
import com.pjb.immaapp.ui.purchaseorder.PurchaseOrderFragmentDirections
import com.pjb.immaapp.ui.usulanpermintaanbarang.UsulanViewModel
import com.pjb.immaapp.ui.usulanpermintaanbarang.adapter.DataItemUpbPagedListAdapter
import com.pjb.immaapp.ui.usulanpermintaanbarang.tambah.material.MaterialOnclick
import com.pjb.immaapp.utils.ConverterHelper
import com.pjb.immaapp.utils.NetworkState
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.SharedPreferencesKey.PREFS_NAME
import com.pjb.immaapp.utils.global.ViewModelFactory
import com.pjb.immaapp.utils.global.tokenExpired
import timber.log.Timber


class DetailUsulanFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var itemPagedListAdapter: DataItemUpbPagedListAdapter
    private lateinit var token: String
    private var idPermintaan: Int? = null

    private lateinit var viewModel: MainViewModel

    private val upbViewModel by lazy {
        val factory = this.context?.applicationContext?.let { ViewModelFactory.getInstance(it) }
        factory?.let { ViewModelProvider(this, it) }?.get(UsulanViewModel::class.java)
    }

    private val onItemClicked = object : MaterialOnclick {
        override fun onClicked(idDetail: Int) {
            val action =
                DetailUsulanFragmentDirections.actionDetailUsulanPermintaanBarangFragmentToDetailMaterialFragment(
                    idDetail, idPermintaan ?: 0
                )
            findNavController().navigate(action)
        }
    }

    private val onBackCallBack = object: OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().popBackStack(R.id.nav_usulan, false)
            Timber.d("back is called ")
        }
    }

    private var _bindingFragmentDetailUsulan: FragmentDetailUsulanBinding? = null
    private val binding get() = _bindingFragmentDetailUsulan

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackCallBack)

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


        val toolbar = binding?.customToolbarDetailUsulan
        val txView = toolbar?.root?.findViewById(R.id.tx_title_page) as TextView
        val btnBack = toolbar.root.findViewById(R.id.btn_back_menu) as ImageView
        btnBack.setOnClickListener {
            it.findNavController().popBackStack()
        }

        txView.text = context?.resources?.getString(R.string.detail_usulan_npermintaan_barang)

        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.root)

        itemPagedListAdapter = DataItemUpbPagedListAdapter(onItemClicked)
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

        Timber.d("Check idPermintaan : $idPermintaan")

        sharedPreferences =
            activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)!!
        token = sharedPreferences.getString(SharedPreferencesKey.KEY_TOKEN, "Not Found")
            ?: "Shared Preferences Not Found"

        initiateDetail(token, idPermintaan!!)
        initiateItemUpb(token, idPermintaan!!)

        binding?.fabTambahMaterial?.setOnClickListener {
            navigateToTambahMaterial(idPermintaan!!)
        }
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

        upbViewModel?.networkStateDetail?.observe(viewLifecycleOwner, Observer { network ->
            when (network) {
                NetworkState.LOADING -> {
                    binding?.shimmerViewContainerDetailUpb?.startShimmer()
                }
                NetworkState.LOADED -> {
                    binding?.shimmerViewContainerDetailUpb?.stopShimmer()
                    binding?.shimmerViewContainerDetailUpb?.visibility = View.GONE
                    binding?.layoutKeteranganUpb?.visibility = View.VISIBLE
                    binding?.txTitleDaftarMaterial?.visibility = View.VISIBLE
                }
                NetworkState.EXPIRETOKEN -> {
                    context?.tokenExpired()?.show()
                }
                else -> {
                    ConverterHelper().convertNetworkStateErrorToSnackbar(binding?.root, network)
                }
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

    private fun navigateToTambahMaterial(idPermintaan: Int) {
        val action =
            DetailUsulanFragmentDirections.actionDetailUsulanPermintaanBarangFragmentToTambahMaterialUpbFragment(
                idPermintaan
            )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingFragmentDetailUsulan = null
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
        initiateDetail(token, idPermintaan!!)
        initiateItemUpb(token, idPermintaan!!)
    }

}