package com.pjb.immaapp.ui.usulanpermintaanbarang.material.approval

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.pjb.immaapp.R
import com.pjb.immaapp.data.entity.notification.NotificationMessage
import com.pjb.immaapp.databinding.FragmentApprovalRabBinding
import com.pjb.immaapp.utils.SharedPreferencesKey
import com.pjb.immaapp.utils.global.ViewModelFactory
import timber.log.Timber

class ApprovalRabFragment: Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private var _approvalRabFragmentBinding: FragmentApprovalRabBinding? = null
    private val binding get() = _approvalRabFragmentBinding

    private lateinit var token: String
    private lateinit var name: String

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

        txView.text = getString(R.string.approval_rab)
        initiateKey()

        binding?.btnSubmitApproval?.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage(){

        val approvalName = binding?.spinnerNeedApproval?.selectedItem.toString()
        val note = binding?.edtCatatan?.text.toString()

        val notificationMessage = NotificationMessage(
            senderName = name,
            note = note,
            message = "message"
        )

        viewModel?.getTokenData("12345", token)?.observe(viewLifecycleOwner, Observer {
            var blocker = 1
            for (i in it.indices) {
                if (approvalName.equals(it[i].nama, ignoreCase = true)) {
                    if (blocker < 2) {
                        Timber.d("Message Have sent")
                        viewModel!!.sendMessage(it[i].TokenFcm, body = note, title = name, message = "message")
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
            sharedPreferences.getString(SharedPreferencesKey.KEY_TOKEN, "Not Found") ?: "Shared Preference Not Found"
        name =
            sharedPreferences.getString(SharedPreferencesKey.KEY_NAME, "Not Found") ?: "Shared Preference Not Found"
    }

}