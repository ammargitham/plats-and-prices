package com.ammar.platsnprices.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ammar.platsnprices.data.entities.RecentGameDiscount
import com.ammar.platsnprices.data.entities.Region
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentGameDiscountDao {
    @Query("SELECT * FROM recent_game_discounts WHERE region = :region ORDER BY name")
    fun getAllByRegion(region: Region): Flow<List<RecentGameDiscount>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(recentGameDiscounts: List<RecentGameDiscount>)

    @Query("DELETE FROM sales")
    suspend fun deleteAll()
}