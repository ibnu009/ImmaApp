package com.pjb.immaapp.ui.usulanpermintaanbarang.tambah

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.pjb.immaapp.R
import com.pjb.immaapp.data.remote.response.ResponseCreateUpb
import com.pjb.immaapp.data.source.usulanpermintaan.UpbRequestBody
import com.pjb.immaapp.databinding.FragmentTambahUsulanBinding
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.getFileName
import com.pjb.immaapp.utils.snackbar
import com.pjb.immaapp.webservice.RetrofitApp
import com.pjb.immaapp.webservice.RetrofitApp.Companion.API_KEY
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class TambahUsulanFragment : Fragment(), UpbRequestBody.UploadCallback {

    private var _bindingFragmentTambahUsulan: FragmentTambahUsulanBinding? = null
    private val binding get() = _bindingFragmentTambahUsulan

    private var selectedDocumentUri: Uri? = null
    private lateinit var token: String
    private lateinit var sharedPreferences: SharedPreferences

    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingFragmentTambahUsulan =
            FragmentTambahUsulanBinding.inflate(inflater, container, false)
        return _bindingFragmentTambahUsulan?.root
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

        sharedPreferences =
            activity?.getSharedPreferences(SharedPreferencesKey.PREFS_NAME, Context.MODE_PRIVATE)!!
        token =
            sharedPreferences.getString(SharedPreferencesKey.KEY_TOKEN, "Not Found")
                ?: "Shared Preference Not Found"


        initiateDateDialogPicker()

        binding?.btnPilihFile?.setOnClickListener {
            openDocumentChooser()
        }

        binding?.btnUploadFile?.setOnClickListener {
            uploadDocument()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_DOCUMENT -> {
                    selectedDocumentUri = data?.data
                    binding?.txPath?.text = selectedDocumentUri?.lastPathSegment
                }
            }
        }
    }

    private fun initiateDateDialogPicker() {
        val cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                showDate(cal)
            }

        binding?.edtTanggalDibutuhkan?.setOnClickListener {
            requireActivity().let {
                DatePickerDialog(
                    it,
                    dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
    }

    private fun showDate(calendar: Calendar) {
        val myFormat = "dd/MMM/yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding?.edtTanggalDibutuhkan?.setText(sdf.format(calendar.time))
    }

    private fun openDocumentChooser() {
        Intent(Intent.ACTION_GET_CONTENT).also {
            it.type = "*/*"
            val mimeType =
                arrayOf(
                    "application/msword",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                    "application/pdf",
                    "text/plain"
                )
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
            startActivityForResult(it, REQUEST_CODE_PICK_DOCUMENT)
        }
    }

    private fun uploadDocument() {
        Timber.d("uploading...")
        if (selectedDocumentUri == null) {
            Timber.d("Null")
            return
        }
        val parcelFileDescriptor =
            requireActivity().contentResolver.openFileDescriptor(selectedDocumentUri!!, "r", null)
                ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(
            requireActivity().cacheDir, requireActivity().contentResolver.getFileName(
                selectedDocumentUri!!
            )
        )
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        binding?.progressCreateUpb?.progress = 0

        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedDocumentUri.toString())
        val contentType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
            fileExtension.toLowerCase(
                Locale.ROOT
            )
        )

        Timber.d("contenttype is $contentType")

        val body = UpbRequestBody(file, contentType!!, this)

        try {
            RetrofitApp.getUploadUpbService().createPermintaanBarang(
                token = MultipartBody.Part.createFormData("token", token),
                apiKey = MultipartBody.Part.createFormData("api_key", API_KEY.toString()),
                requiredDate = "2020-02-11".toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                description = "jobTitleeeeeeeeeeeeeee".toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                notes = "Notes".toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                critical = "0".toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                idSdm = "860".toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                file = MultipartBody.Part.createFormData(
                    "$contentType",
                    file.name,
                    body
                )
            ).enqueue(object : Callback<ResponseCreateUpb> {
                override fun onResponse(
                    call: Call<ResponseCreateUpb>,
                    response: Response<ResponseCreateUpb>
                ) {
                    response.body()?.let {
                        binding?.root?.snackbar(it.message ?: "Sudah")
                        Timber.d("haha ${it.message}")
                        binding?.progressCreateUpb?.progress = 100
                    }
                }

                override fun onFailure(call: Call<ResponseCreateUpb>, t: Throwable) {
                    Timber.d("errordong ${t.message}")
                    binding?.progressCreateUpb?.progress = 100
                    t.message?.let { binding?.root?.snackbar(it) }
                }
            })
        } catch (e: Exception) {
            Timber.e("errornya ${e.message}")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingFragmentTambahUsulan = null
    }

    companion object {
        const val REQUEST_CODE_PICK_DOCUMENT = 100
    }

    override fun onProgressUpdate(percentage: Int) {
        binding?.progressCreateUpb?.visibility = View.VISIBLE
        binding?.progressCreateUpb?.progress = percentage
    }

}