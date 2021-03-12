package com.pjb.immaapp.ui.usulanpermintaanbarang.tambah.material

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.pjb.immaapp.BuildConfig
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentTambahMaterialBinding
import com.pjb.immaapp.handler.UpbCreateMaterialHandler
import com.pjb.immaapp.handler.UpbCreateMaterialListener
import com.pjb.immaapp.ui.stokopname.StokOpnameFragmentDirections
import com.pjb.immaapp.utils.FIleHelper
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.global.ViewModelFactory
import com.pjb.immaapp.utils.global.snackbar
import timber.log.Timber

class TambahMaterialUpbFragment : Fragment(), UpbCreateMaterialHandler, UpbCreateMaterialListener {

    private var selectedImageUri: Uri? = null
    private var takePictureImageUri: Uri? = null
    private var imagePath: String? = null
    private lateinit var token: String
    private lateinit var idSdm: String
    private lateinit var apiKey: String
    private lateinit var itemNumString: String
    private var idPermintaan: Int? = null
    private lateinit var sharedPreferences: SharedPreferences

    private var _bindingTambahMaterialFragment: FragmentTambahMaterialBinding? = null
    private val binding get() = _bindingTambahMaterialFragment

    private val viewModel by lazy {
        val factory = this.context?.applicationContext?.let { ViewModelFactory.getInstance(it) }
        factory?.let { ViewModelProvider(this, it) }?.get(TambahMaterialViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingTambahMaterialFragment =
            FragmentTambahMaterialBinding.inflate(inflater, container, false)
        return _bindingTambahMaterialFragment?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.viewModel = viewModel
        viewModel?.upbCreateMaterialListener = this
        binding?.lifecycleOwner = this
        binding?.handler = this

        val safeArgs = arguments?.let { TambahMaterialUpbFragmentArgs.fromBundle(it) }
        idPermintaan = safeArgs?.passIdPermintaan

        val toolbar = binding?.customToolbarTambahMaterial
        val txView = toolbar?.findViewById(R.id.tx_title_page) as TextView
        val btnBack = toolbar.findViewById(R.id.btn_back_menu) as ImageView
        btnBack.setOnClickListener {
            it.findNavController().popBackStack()
        }

        txView.text = getString(R.string.tambah_material)
        initiateKeys()
        context?.let { initiatePermission(it) }

        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingTambahMaterialFragment = null
    }

    override fun onClickOpenPhotoFile(view: View) {
        Timber.d("onclickedOpenPhotoFile")
        pickFileImage.launch("image/*")
    }

    override fun onClickOpenCamera(view: View) {
        Timber.d("onclickedOpenCamera")
        takePictureRegistration.launch()
    }

    override fun onClickuploadMaterial(view: View) {
        Timber.d("onClickUploadMaterial with result = $imagePath")

        val spinner = binding?.spinnerLineType?.selectedItemId.toString()
        var type = 0
        when (spinner) {
            getString(R.string.services) -> {
                type = 0
            }
            getString(R.string.items) -> {
                type = 1
            }
        }

        viewModel?.validateMaterialUpload(
            context = this.requireContext(),
            lifecycleOwner = this,
            apiKey = apiKey,
            token = token,
            itemNum = itemNumString,
            idPermintaan = idPermintaan!!,
            lineType = type,
            path = imagePath ?: "-"
        )
    }

    override fun onClickSearchItemNum(view: View) {
        Timber.d("onclickedSearchItemNum")
        initiateDialogView(view)
    }

    private fun initiateKeys() {
        sharedPreferences =
            activity?.getSharedPreferences(SharedPreferencesKey.PREFS_NAME, Context.MODE_PRIVATE)!!
        apiKey = sharedPreferences.getString(SharedPreferencesKey.KEY_API, "Not Found")
            ?: "Shared Preference Not Found"
        token = sharedPreferences.getString(SharedPreferencesKey.KEY_TOKEN, "Not Found")
            ?: "Shared Preference Not Found"
        idSdm = sharedPreferences.getString(SharedPreferencesKey.KEY_ID_SDM, "Not Found")
            ?: "Shared Preference Not Found"
    }

    private fun initiateDialogView(v: View) {
        val mDialogView =
            LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_item_num, null)
        val mBuilder = AlertDialog.Builder(requireActivity())
            .setView(mDialogView)
            .show()

        val edtItemNum = mDialogView.findViewById(R.id.edt_item_num) as EditText
        val btnKonfirmasi = mDialogView.findViewById(R.id.btn_konfirmasi) as Button

        btnKonfirmasi.setOnClickListener {
            itemNumString = edtItemNum.text.toString()
            val itemNum = Integer.parseInt(itemNumString)
            viewModel?.getDataMaterial(apiKey, token, itemNum)?.observe(this, Observer {
                binding?.edtSpesifikasi?.text = it.description
                binding?.edtCurrentBalance?.text = it.curbal.toString()
                binding?.edtSatuan?.text = it.satuan
            })
            mBuilder.dismiss()
        }
    }

//    private val takePicture: Runnable = Runnable {
//        context?.applicationContext?.let {
//            FIleHelper().createImageFile(it)?.also { file ->
//                takePictureImageUri = FileProvider.getUriForFile(
//                    context?.applicationContext!!,
//                    BuildConfig.APPLICATION_ID + ".provider",
//                    file
//                )
//                takePictureRegistration.launch(takePictureImageUri)
//            }
//            imagePath = FIleHelper().getRealImagePathFromURI(it, takePictureImageUri)
//        }
//    }

    private val takePictureRegistration =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
//            if (success) {
                val uri = FIleHelper().getImageUri(requireActivity(), bitmap)
                imagePath = FIleHelper().getFilePathFromURI(requireActivity(), uri)
                binding?.imgMaterialContainer?.setImageBitmap(bitmap)
//            }
        }

    private val pickFileImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            selectedImageUri = uri
//            imagePath = FIleHelper().getFilePathFromURI(
//                this.context?.applicationContext,
//                selectedImageUri
//            )
            binding?.imgMaterialContainer?.setImageURI(selectedImageUri)
        }

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101
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

}