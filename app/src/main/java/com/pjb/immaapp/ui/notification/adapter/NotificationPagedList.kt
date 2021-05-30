package com.pjb.immaapp.ui.notification.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pjb.immaapp.data.entity.local.notification.NotificationEntity
import com.pjb.immaapp.data.entity.po.ItemPurchaseOrder
import com.pjb.immaapp.databinding.NotificationItemBinding

class NotificationPagedList :
    PagingDataAdapter<NotificationEntity, NotificationPagedList.NotificationViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        getItem(position)?.let { notification ->
            holder.bind(notification)
        }
    }

    inner class NotificationViewHolder(private val binding: NotificationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: NotificationEntity) {
            binding.txSender.text = notification.sender

        }
    }



    companion object {


        private val DIFF_CALLBACK: DiffUtil.ItemCallback<NotificationEntity> = object :
            DiffUtil.ItemCallback<NotificationEntity>() {
            override fun areItemsTheSame(oldItem: NotificationEntity, newItem: NotificationEntity): Boolean {
                return oldItem.id == newItem.id && oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: NotificationEntity,
                newItem: NotificationEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}