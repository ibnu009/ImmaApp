package com.pjb.immaapp.ui.usulanpermintaanbarang.approval

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentApprovalRabBinding
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.global.ConstVal
import com.pjb.immaapp.utils.global.ViewModelFactory
import com.pjb.immaapp.utils.global.snackbar
import timber.log.Timber

class ApprovalRabFragment : Fragment(), ApprovalRabListener, ApprovalRabHandler {

    private lateinit var sharedPreferences: SharedPreferences
    private var _approvalRabFragmentBinding: FragmentApprovalRabBinding? = null
    private val binding get() = _approvalRabFragmentBinding
    private var idPermintaan: Int = 0

    private lateinit var token: String
    private lateinit var name: String
    private lateinit var approvalName: String

    private val viewModel by lazy {
        val factory = this.context?.applicationContext?.let { ViewModelFactory.getInstance(it) }
        factory?.let { ViewModelProvider(this, it).get(ApprovalRabViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _approvalRabFragmentBinding = FragmentApprovalRabBinding.inflate(inflater, container, false)
        return _approvalRabFragmentBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = binding?.customToolbar
        val txView = toolbar?.root?.findViewById(R.id.tx_title_page) as TextView
        val btnBack = toolbar.root.findViewById(R.id.btn_back_menu) as ImageView
        btnBack.setOnClickListener {
            it.findNavController().popBackStack()
        }

        val safeArgs = arguments?.let { ApprovalRabFragmentArgs.fromBundle(it) }
        idPermintaan = safeArgs?.passIdPermintaan!!

        viewModel?.approvalRabListener = this
        binding?.handler = this
        binding?.viewModel = viewModel

        txView.text = getString(R.string.approval_rab)
        initiateKey()

    }

    private fun saveRab(approvalName: String) {
        var idSdmApproval = 0

        viewModel?.getListKaryawan(apiKey = "12345", token = token)
            ?.observe(viewLifecycleOwner, Observer {
                for (i in it.indices) {
                    if (approvalName.equals(it[i].nama, ignoreCase = true)) {
                        Timber.d("Check approvalNameData : ${it[i].nama}")
                        Timber.d("Check approvalId : ${it[i].idSdm}")
                        idSdmApproval = it[i].idSdm
                    }
                }


                viewModel?.saveRab(
                    apiKey = "12345",
                    token = token,
                    idPermintaan = idPermintaan,
                    idSdmApproval = idSdmApproval
                )

                if (idSdmApproval != 0) {
                    sendMessage(approvalName)
                }

                Timber.d("Check idSdmApproval : $idSdmApproval")
                Timber.d("Check approvalName : $approvalName")
            })



    }

    private fun sendMessage(approvalName: String) {

        val note = binding?.edtCatatan?.text.toString()

        viewModel?.getTokenData("12345", token)?.observe(viewLifecycleOwner, Observer {
            var blocker = 1
            for (i in it.indices) {
                if (approvalName.equals(it[i].nama, ignoreCase = true)) {
                    if (blocker < 2) {
                        Timber.d("Message Have sent")
                        viewModel!!.sendMessage(
                            recipientToken = it[i].TokenFcm,
                            body = note,
                            title = name,
                            message = "message",
                            type = "rab_request",
                            context = requireContext(),
                        )
                        blocker++
                    }
                }
            }
        })
    }

    private fun initiateKey() {
        sharedPreferences =
            activity?.getSharedPreferences(SharedPreferencesKey.PREFS_NAME, Context.MODE_PRIVATE)!!
        token =
            sharedPreferences.getString(SharedPreferencesKey.KEY_TOKEN, "Not Found")
                ?: "Shared Preference Not Found"
        name =
            sharedPreferences.getString(SharedPreferencesKey.KEY_NAME, "Not Found")
                ?: "Shared Preference Not Found"
    }

    override fun onInitiating() {
        isLoading(true)
    }

    override fun onSuccess(message: String) {
        isLoading(false)
        val action = ApprovalRabFragmentDirections.actionApprovalRabFragmentToSuccessFragment(
            ConstVal.RAB_TYPE
        )
        view?.findNavController()?.navigate(action)
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

    override fun onSaveClick(view: View) {
        approvalName = binding?.spinnerNeedApproval?.selectedItem.toString()

        Timber.d("Check idPermintaan : $idPermintaan")
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Konfirmasi Create RAB")
            setMessage("Apakah Anda yakin membuat RAB dengan approval dari $approvalName?")
            setPositiveButton(
                "Ya"
            ) { p0, _ ->
                saveRab(approvalName)
                p0.dismiss()
            }
            setNegativeButton("Tidak") { p0, _ ->
                p0.dismiss()
            }
        }.create().show()
    }

    override fun onCancelClick(view: View) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Imma Alert")
            setMessage("Apakah anda yakin untuk membatalkan create RAB?")
            setPositiveButton(
                "Ya"
            ) { p0, _ ->
                view.findNavController().popBackStack()
                p0.dismiss()
            }
            setNegativeButton("Tidak") { p0, _ ->
                p0.dismiss()
            }
        }.create().show()
    }
}