package com.ammar.platsnprices.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.ammar.platsnprices.data.entities.DlcDiscount

@Dao
interface DlcDiscountDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(dlcDiscounts: List<DlcDiscount>)
}
