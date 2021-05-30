package com.pjb.immaapp.data.local.dao.notification

import androidx.paging.DataSource
import androidx.paging.PagingData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pjb.immaapp.data.entity.local.notification.NotificationEntity

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotification(notification: NotificationEntity)

    @Query("SELECT * FROM notification_entity ORDER BY id ASC")
    fun getAllNotification(): DataSource.Factory<Int, NotificationEntity>

    @Query("DELETE FROM notification_entity WHERE id =:id")
    fun deleteNotification(id: Int)
}