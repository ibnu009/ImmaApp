package com.pjb.immaapp.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pjb.immaapp.R
import com.pjb.immaapp.databinding.FragmentNotificationBinding
import com.pjb.immaapp.ui.notification.adapter.NotificationPagedList
import com.pjb.immaapp.utils.global.ViewModelFactory
import com.pjb.immaapp.utils.global.snackbar
import timber.log.Timber

class NotificationFragment : Fragment(), NotificationListener {

    private var _bindingNotificationFragment: FragmentNotificationBinding? = null
    private val binding get() = _bindingNotificationFragment
    private lateinit var notificationAdapter: NotificationPagedList

    private val viewModel by lazy {
        val factory = this.context?.applicationContext?.let { ViewModelFactory.getInstance(it) }
        factory?.let { ViewModelProvider(this, it).get(NotificationViewModel::class.java) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindingNotificationFragment =
            FragmentNotificationBinding.inflate(inflater, container, false)

        return _bindingNotificationFragment?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel?.notificationListener = this

        notificationAdapter = NotificationPagedList()
        notificationAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && notificationAdapter.itemCount < 1) {
                emptyView(true)
            } else {
                emptyView(false)
            }
        }

        with(binding?.rvNotification) {
            this?.adapter = notificationAdapter
            this?.layoutManager =
                LinearLayoutManager(this?.context, LinearLayoutManager.VERTICAL, false)
        }

        initToolbar("Notifikasi")
        initData()
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

    private fun initData() {
        viewModel?.getNotification()?.observe(viewLifecycleOwner, Observer {
            notificationAdapter.submitData(lifecycle, it)
        })
    }

    private fun emptyView(isEmpty: Boolean) {
        if (isEmpty) {
            binding?.customToolbarNotification?.root?.visibility = View.GONE
            binding?.layoutEmptyList?.visibility = View.VISIBLE
        } else {
            binding?.customToolbarNotification?.root?.visibility = View.VISIBLE
            binding?.layoutEmptyList?.visibility = View.GONE
        }
    }

    override fun onInitiating() {
        TODO("Not yet implemented")
    }

    override fun onLoaded() {
        TODO("Not yet implemented")
    }

    override fun onOpenNotification() {
        TODO("Not yet implemented")
    }

    override fun onErrorOccurred(message: String) {
        binding?.root?.snackbar(message)
    }

}