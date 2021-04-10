package com.pjb.immaapp.ui.usulanpermintaanbarang.supplier

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentTambahSupplierBinding
import com.pjb.immaapp.handler.RabAddSupplierHandler
import com.pjb.immaapp.handler.RabAddSupplierListener
import com.pjb.immaapp.utils.FIleHelper
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.global.ViewModelFactory
import com.pjb.immaapp.utils.global.snackbar
import timber.log.Timber

class TambahSupplierFragment : Fragment(), RabAddSupplierListener, RabAddSupplierHandler {
    private var _tambahSupplierFragment: FragmentTambahSupplierBinding? = null
    private val binding get() = _tambahSupplierFragment

    private var imagePath: String? = null

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String
    private lateinit var apiKey: String
    private var idDetail: Int? = null
    private var idSupplier: Int? = null
    private var namaSupplier: String? = null

    private val viewModel by lazy {
        val factory = this.context?.applicationContext?.let { ViewModelFactory.getInstance(it) }
        factory?.let { ViewModelProvider(this, factory).get(TambahSupplierViewModel::class.java) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("requestKey") { requestKey, bundle ->
            idSupplier = bundle.getInt("idSupplier")
            namaSupplier = bundle.getString("namaSupplier")
            binding?.edtNamaSupplier?.setText(namaSupplier)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _tambahSupplierFragment = FragmentTambahSupplierBinding.inflate(inflater, container, false)
        return _tambahSupplierFragment?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding?.customToolbarTambahSupplier
        val txTitle = toolbar?.findViewById(R.id.tx_title_page) as TextView
        txTitle.text = getString(R.string.tambah_supplier)

        val safeArgs = arguments?.let { TambahSupplierFragmentArgs.fromBundle(it) }
        idDetail = safeArgs?.passIdDetailToAddSupplier

        this.context?.let { initiatePermission(it) }
        initiateKeys()

        binding?.handler = this
        binding?.lifecycleOwner = this
        viewModel?.rabAddSupplierListener = this
        binding?.viewModel = viewModel

        val btnBack = toolbar.findViewById(R.id.btn_back_menu) as ImageView
        btnBack.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

    private fun isLoading(status: Boolean) {
        if (!status) {
            binding?.progressBar?.visibility = View.GONE
            binding?.backgroundDim?.visibility = View.GONE
        } else {
            binding?.progressBar?.visibility = View.VISIBLE
            binding?.backgroundDim?.visibility = View.VISIBLE
        }
    }

    private fun initiatePermission(context: Context) {
        Dexter.withContext(context)
            .withPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    Timber.d("Accepted Credential")
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            }).withErrorListener {
                binding?.root?.snackbar("Error!!!")
            }.onSameThread()
            .check()
    }

    private fun initiateKeys() {
        sharedPreferences =
            activity?.getSharedPreferences(SharedPreferencesKey.PREFS_NAME, Context.MODE_PRIVATE)!!
        apiKey = sharedPreferences.getString(SharedPreferencesKey.KEY_API, "Not Found")
            ?: "Shared Preference Not Found"
        token = sharedPreferences.getString(SharedPreferencesKey.KEY_TOKEN, "Not Found")
            ?: "Shared Preference Not Found"
    }

    private val pickFileImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            imagePath = FIleHelper().getFilePathFromURI(this.context?.applicationContext, uri)
        }


    override fun onInitiating() {
        isLoading(true)
    }

    override fun onSuccess(message: String) {
        isLoading(false)
        binding?.root?.snackbar(message)
    }

    override fun onFailure(message: String) {
        isLoading(false)
        binding?.root?.snackbar(message)
    }

    override fun onClickAddSupplier(view: View) {
        viewModel?.validateRabUpload(requireContext(), viewLifecycleOwner, apiKey, token, idDetail, idSupplier, imagePath)
    }

    override fun onClickSelectFile(view: View) {
        pickFileImage.launch("application/*")
    }

    override fun onClickSearchSupplier(View: View) {
        view?.findNavController()?.navigate(R.id.action_tambahSupplierFragment_to_listSupplierFragment)
    }

}
