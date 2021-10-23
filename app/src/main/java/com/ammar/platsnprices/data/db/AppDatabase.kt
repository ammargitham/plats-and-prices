package com.ammar.platsnprices.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ammar.platsnprices.data.db.converters.Converters
import com.ammar.platsnprices.data.db.daos.*
import com.ammar.platsnprices.data.entities.*

@Database(
    version = 4,
    entities = [
        Sale::class,
        SalesLastUpdated::class,
        GameDiscount::class,
        DlcDiscount::class,
        RecentGameDiscount::class,
    ],
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun saleDao(): SaleDao
    abstract fun salesLastUpdatedDao(): SalesLastUpdatedDao
    abstract fun gameDiscountDao(): GameDiscountDao
    abstract fun dlcDiscountDao(): DlcDiscountDao
    abstract fun recentGameDiscountDao(): RecentGameDiscountDao
}
