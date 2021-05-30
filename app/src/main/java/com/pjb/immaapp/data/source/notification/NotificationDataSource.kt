package com.pjb.immaapp.data.source.notification

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pjb.immaapp.data.entity.local.notification.NotificationEntity
import com.pjb.immaapp.data.local.dao.notification.NotificationDao
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class NotificationDataSource(
    private val notificationDao: NotificationDao
): PagingSource<Int, NotificationEntity>() {
    override fun getRefreshKey(state: PagingState<Int, NotificationEntity>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotificationEntity> {
        TODO("Not yet implemented")
    }
}