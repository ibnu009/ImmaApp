package com.pjb.immaapp.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pjb.immaapp.data.entity.local.fts.SupplierFTS
import com.pjb.immaapp.data.entity.local.notification.NotificationEntity
import com.pjb.immaapp.data.entity.local.po.PurchaseOrders
import com.pjb.immaapp.data.entity.local.supplier.Suppliers
import com.pjb.immaapp.data.entity.po.fts.DataPoFTS
import com.pjb.immaapp.data.local.dao.notification.NotificationDao
import com.pjb.immaapp.data.local.dao.po.PurchaseOrderDao
import com.pjb.immaapp.data.local.dao.po.PurchaseOrderRemoteKeys
import com.pjb.immaapp.data.local.dao.supplier.SupplierDao
import com.pjb.immaapp.data.local.dao.supplier.SupplierRemoteKeys

@Database(
    entities = [
        PurchaseOrders.PurchaseOrderEntity::class,
        PurchaseOrders.PurchaseOrderKeys::class,
        DataPoFTS::class,
        Suppliers.SupplierEntity::class,
        Suppliers.SupplierKey::class,
        SupplierFTS::class,
        NotificationEntity::class],
    version = 7,
    exportSchema = false
)
abstract class ImmaDatabase : RoomDatabase() {

    abstract fun getDataPoDao(): PurchaseOrderDao
    abstract fun getRemoteKeyDataPo(): PurchaseOrderRemoteKeys

    abstract fun getSupplierDao(): SupplierDao
    abstract fun getRemoteKeySupplier(): SupplierRemoteKeys

    abstract fun getNotificationDao(): NotificationDao

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
                        )
                            .addCallback(object : RoomDatabase.Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                    db.execSQL("INSERT INTO data_po_fts(data_po_fts) VALUES ('rebuild')")
                                }
                            })
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE as ImmaDatabase
        }
    }
}