package com.pjb.immaapp.ui.usulanpermintaanbarang.material.approval

import android.content.Context
import android.content.Intent
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
import com.pjb.immaapp.data.entity.notification.NotificationMessage
import com.pjb.immaapp.databinding.FragmentApprovalRabBinding
import com.pjb.immaapp.ui.login.LoginActivity
import com.pjb.immaapp.ui.usulanpermintaanbarang.material.DetailMaterialFragmentArgs
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.global.ViewModelFactory
import com.pjb.immaapp.utils.global.snackbar
import com.pjb.immaapp.utils.global.tokenExpired
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

        txView.text = getString(R.string.approval_rab)
        initiateKey()

        approvalName = binding?.spinnerNeedApproval?.selectedItem.toString()
    }

    private fun saveRab(approvalName: String) {
        var idSdmApproval = 0

        viewModel?.getListKaryawan(apiKey = "12345", token = token)
            ?.observe(viewLifecycleOwner, Observer {
                for (i in it.indices) {
                    if (approvalName.equals(it[i].nama, ignoreCase = true)) {
                        idSdmApproval = it[i].idSdm
                    }
                }
            })

        viewModel?.saveRab(
            apiKey = "12345",
            token = token,
            idPermintaan = idPermintaan,
            idSdmApproval = idSdmApproval
        )
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

    override fun onSaveClick(view: View) {
        sendMessage(approvalName)
        saveRab(approvalName)
    }

    override fun onCancelClick(view: View) {
        AlertDialog.Builder(requireContext().applicationContext).apply {
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
        }.create()
    }
}