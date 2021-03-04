package com.pjb.immaapp.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentNotificationBinding

class NotificationFragment: Fragment() {

    private var _bindingNotificationFragment: FragmentNotificationBinding? = null
    private val binding get() = _bindingNotificationFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingNotificationFragment = FragmentNotificationBinding.inflate(inflater, container, false)

        return _bindingNotificationFragment?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar("Notifikasi")
    }

    private fun initToolbar(title: String) {
        val toolbar = binding?.customToolbarNotification?.root
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        val txTitle = toolbar?.findViewById(R.id.tx_title_page) as TextView
        txTitle.text = title

        val btnBack = toolbar.findViewById(R.id.btn_back_menu) as ImageView
        btnBack.setOnClickListener {
            it.findNavController().popBackStack()
        }

    }

}