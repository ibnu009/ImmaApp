package com.pjb.immaapp.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pjb.immaapp.data.entity.local.po.PurchaseOrders
import com.pjb.immaapp.data.local.dao.PurchaseOrderDao
import com.pjb.immaapp.data.local.dao.PurchaseOrderRemoteKeys

@Database(
    entities = [PurchaseOrders.PurchaseOrderEntity::class, PurchaseOrders.PurchaseOrderKeys::class],
    version = 1,
    exportSchema = false
)
abstract class ImmaDatabase : RoomDatabase() {

    abstract fun getDataPoDao(): PurchaseOrderDao
    abstract fun getRemoteKeyDataPo(): PurchaseOrderRemoteKeys

    companion object {
        @Volatile
        private var INSTANCE: ImmaDatabase? = null

        @JvmStatic
        fun getDataBase(context: Context): ImmaDatabase {
            if (INSTANCE == null) {
                synchronized(ImmaDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ImmaDatabase::class.java,
                            "db_imma"
                        ).fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE as ImmaDatabase
        }
    }
}