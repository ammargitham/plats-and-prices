package com.ammar.platsnprices.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.ammar.platsnprices.data.entities.GameDiscount

@Dao
interface GameDiscountDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(gameDiscounts: List<GameDiscount>)
}