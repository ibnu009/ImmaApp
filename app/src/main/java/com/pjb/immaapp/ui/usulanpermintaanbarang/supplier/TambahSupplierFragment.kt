package com.pjb.immaapp.ui.usulanpermintaanbarang.supplier

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.findNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.upb.Supplier
import com.pjb.immaapp.databinding.FragmentTambahSupplierBinding
import com.pjb.immaapp.handler.RabAddSupplierHandler
import com.pjb.immaapp.handler.RabAddSupplierListener
import com.pjb.immaapp.ui.usulanpermintaanbarang.UsulanViewModel
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.global.ViewModelFactory
import com.pjb.immaapp.utils.global.snackbar
import com.pjb.immaapp.webservice.RetrofitApp.Companion.API_KEY
import timber.log.Timber

class TambahSupplierFragment : Fragment(), RabAddSupplierListener, RabAddSupplierHandler {
    private var _tambahSupplierFragment: FragmentTambahSupplierBinding? = null
    private val binding get() = _tambahSupplierFragment

    private lateinit var adapter: ArrayAdapter<Supplier>

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var token: String
    private lateinit var apiKey: String

    private val viewModel by lazy {
        val factory = this.context?.applicationContext?.let { ViewModelFactory.getInstance(it) }
        factory?.let { ViewModelProvider(this, factory).get(UsulanViewModel::class.java) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("requestKey") { requestKey, bundle ->
            val idSupplier = bundle.getInt("idSupplier")
            Timber.d("Id Supplier : $idSupplier")
            val namaSupplier = bundle.getString("namaSupplier")
            Toast.makeText(context?.applicationContext, idSupplier.toString(), Toast.LENGTH_SHORT).show()
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

        sharedPreferences =
            activity?.getSharedPreferences(SharedPreferencesKey.PREFS_NAME, Context.MODE_PRIVATE)!!
        token =
            sharedPreferences.getString(SharedPreferencesKey.KEY_TOKEN, "Not Found")
                ?: "Shared Prefences Not Found"
        apiKey = sharedPreferences.getString(SharedPreferencesKey.KEY_API, "Not Found")
            ?: "Shared Preferences Not Found"

        val btnBack = toolbar.findViewById(R.id.btn_back_menu) as ImageView
        btnBack.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding?.edtNamaSupplier?.setOnClickListener {
            it.findNavController().navigate(R.id.action_tambahSupplierFragment_to_listSupplierFragment)
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
                    if (p0?.areAllPermissionsGranted() == true) {
                    }
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
        TODO("Not yet implemented")
    }

    override fun onClickSelectFile(view: View) {
        TODO("Not yet implemented")
    }

}
