package com.ammar.platsnprices.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ammar.platsnprices.data.entities.GameDiscount

@Dao
interface GameDiscountDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(gameDiscounts: List<GameDiscount>)

    @Query("DELETE FROM game_discounts WHERE sale_db_id in (:saleDbIds)")
    suspend fun deleteBySaleDbIds(saleDbIds: List<Long>)
}