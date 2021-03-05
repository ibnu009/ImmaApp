package com.pjb.immaapp.ui.usulanpermintaanbarang.tambah.usulan

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentTambahUsulanBinding
import com.pjb.immaapp.handler.OnClickHandlerUpbCreate
import com.pjb.immaapp.handler.UpbFileUploadListener
import com.pjb.immaapp.utils.*
import com.pjb.immaapp.utils.global.ViewModelFactory
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class TambahUsulanFragment : Fragment(), OnClickHandlerUpbCreate, UpbFileUploadListener {

    private lateinit var token: String
    private lateinit var idSdm: String
    private lateinit var apiKey: String
    private lateinit var sharedPreferences: SharedPreferences
    private var date: String? = null
    var path: String? = null
    private val calendar: Calendar = Calendar.getInstance()

    private val viewModel by lazy {
        val factory = this.context?.applicationContext?.let { ViewModelFactory.getInstance(it) }
        factory?.let { ViewModelProvider(this, it) }?.get(CreateUpbViewModel::class.java)
    }

    private var _bindingFragmentTambahlUsulan: FragmentTambahUsulanBinding? = null
    private val binding get() = _bindingFragmentTambahlUsulan

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingFragmentTambahlUsulan =
            FragmentTambahUsulanBinding.inflate(inflater, container, false)
        return _bindingFragmentTambahlUsulan?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding?.customToolbarTambahUsulan
        val txView = toolbar?.root?.findViewById(R.id.tx_title_page) as TextView
        val btnBack = toolbar.root.findViewById(R.id.btn_back_menu) as ImageView
        btnBack.setOnClickListener {
            it.findNavController().popBackStack()
        }

        txView.text = getString(R.string.tambah_usulan_permintaan_barang)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.root)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this
        viewModel?.upbFileUploadListener = this
        binding?.handler = this

        sharedPreferences = activity?.getSharedPreferences(SharedPreferencesKey.PREFS_NAME, Context.MODE_PRIVATE)!!
        initiateKeys()
        initiateDateDialogPicker()
        this.context?.let { initiatePermission(it) }

        binding?.btnPilihFile?.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "application/pdf"
                val mimes = arrayOf("application/pdf")
                it.putExtra(Intent.EXTRA_MIME_TYPES, mimes)
                startActivityForResult(it, REQUEST_CODE_PICK_DOCUMENT)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == AppCompatActivity.RESULT_OK) {
            val uri = data?.data
            path = FIleHelper().getFilePathFromURI(this.context?.applicationContext, uri)
            binding?.txPath?.text = File(path?:"-").name
            Timber.d("getPathIs $path")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initiateDateDialogPicker() {
        val cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                showDate(cal)
            }

        binding?.edtTanggalDibutuhkan?.setOnClickListener {
            DatePickerDialog(
                requireActivity(),
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun showDate(calendar: Calendar) {
//        Formatter untuk dikirim ke Server
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        date = format.format(calendar.time)

//        Formatter untuk tampil ke User
        val myFormat = "dd/MMM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        val convertedDate = sdf.format(calendar.time)
        binding?.edtTanggalDibutuhkan?.setText(convertedDate)
    }


    override fun onClickUpload(view: View) {
        this.context?.let { viewModel?.checkField(it, this, token, apiKey, path ?: "-", date, idSdm) }
    }

    override fun onInitiating() {
        Timber.d("Loading...")
    }

    override fun onSuccess(message: String, idPermintaan: Int?) {
        binding?.root?.snackbar(message)
        if (idPermintaan != null) {
            val action =
                TambahUsulanFragmentDirections.actionTambahUsulanFragmentToDetailUsulanPermintaanBarangFragment(
                    idPermintaan
                )
            findNavController().navigate(action)
        } else {
            Timber.d("idPermintaan Null")
        }
    }

    override fun onFailure(message: String) {
        binding?.root?.snackbar(message)
    }

    private fun initiatePermission(context: Context) {
        Dexter.withContext(context)
            .withPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                    if (p0?.areAllPermissionsGranted() == true) {
                        binding?.root?.snackbar("All Permissions are granted")
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

    private fun initiateKeys() {
        apiKey = sharedPreferences.getString(SharedPreferencesKey.KEY_API, "Not Found")
            ?: "Shared Preference Not Found"
        token = sharedPreferences.getString(SharedPreferencesKey.KEY_TOKEN, "Not Found")
            ?: "Shared Preference Not Found"
        idSdm = sharedPreferences.getString(SharedPreferencesKey.KEY_ID_SDM, "Not Found")
            ?: "Shared Preference Not Found"
    }

    companion object {
        const val REQUEST_CODE_PICK_DOCUMENT = 100
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingFragmentTambahlUsulan = null
    }


}