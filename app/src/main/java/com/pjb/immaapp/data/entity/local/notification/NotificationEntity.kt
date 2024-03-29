package com.pjb.immaapp.data.entity.local.notification

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_entity")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val sender: String,
    val message: String
)