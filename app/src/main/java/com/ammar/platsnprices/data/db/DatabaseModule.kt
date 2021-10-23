package com.ammar.platsnprices.data.db

import android.content.Context
import androidx.room.Room
import com.ammar.platsnprices.data.db.daos.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun provideSaleDao(database: AppDatabase): SaleDao = database.saleDao()

    @Provides
    fun provideSalesLastUpdatedDao(database: AppDatabase): SalesLastUpdatedDao = database.salesLastUpdatedDao()

    @Provides
    fun provideGameDiscountDao(database: AppDatabase): GameDiscountDao = database.gameDiscountDao()

    @Provides
    fun provideDlcDiscountDao(database: AppDatabase): DlcDiscountDao = database.dlcDiscountDao()

    @Provides
    fun provideRecentGameDiscountDao(database: AppDatabase): RecentGameDiscountDao = database.recentGameDiscountDao()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app.db"
    ).build()
}
