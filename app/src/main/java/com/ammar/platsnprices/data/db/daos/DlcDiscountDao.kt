package com.ammar.platsnprices.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ammar.platsnprices.data.entities.DlcDiscount

@Dao
interface DlcDiscountDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(dlcDiscounts: List<DlcDiscount>)

    @Query("DELETE FROM game_discounts WHERE sale_db_id in (:saleDbIds)")
    suspend fun deleteBySaleDbIds(saleDbIds: List<Long>)
}
