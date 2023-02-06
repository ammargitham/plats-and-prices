package com.ammar.platsnprices.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ammar.platsnprices.data.db.converters.Converters
import com.ammar.platsnprices.data.db.daos.DlcDiscountDao
import com.ammar.platsnprices.data.db.daos.GameDiscountDao
import com.ammar.platsnprices.data.db.daos.RecentGameDiscountDao
import com.ammar.platsnprices.data.db.daos.SaleDao
import com.ammar.platsnprices.data.db.daos.SalesLastUpdatedDao
import com.ammar.platsnprices.data.entities.DlcDiscount
import com.ammar.platsnprices.data.entities.GameDiscount
import com.ammar.platsnprices.data.entities.RecentGameDiscount
import com.ammar.platsnprices.data.entities.Sale
import com.ammar.platsnprices.data.entities.SalesLastUpdated

@Database(
    version = 5,
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
        AutoMigration(from = 4, to = 5),
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
