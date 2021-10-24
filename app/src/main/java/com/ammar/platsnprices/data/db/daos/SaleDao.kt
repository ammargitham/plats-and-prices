package com.ammar.platsnprices.data.db.daos

import androidx.room.*
import com.ammar.platsnprices.data.entities.Region
import com.ammar.platsnprices.data.entities.Sale
import com.ammar.platsnprices.data.entities.SaleWithDiscounts
import kotlinx.coroutines.flow.Flow

@Dao
interface SaleDao {

    @Query("SELECT * FROM sales ORDER BY end_time < (strftime('%s', 'now') * 1000), start_time")
    fun getAll(): Flow<List<Sale>>

    @Query("SELECT * FROM sales WHERE region = :region ORDER BY end_time < (strftime('%s', 'now') * 1000), start_time")
    fun getAllByRegion(region: Region): Flow<List<Sale>>

    @Query("SELECT id FROM sales WHERE end_time < (strftime('%s', 'now') * 1000)")
    suspend fun getExpiredSaleIds(): List<Long>

    @Query("SELECT * FROM sales WHERE id = :id")
    fun getById(id: Long): Flow<Sale>

    @Query("SELECT * FROM sales WHERE sale_id = :saleId")
    suspend fun getBySaleId(saleId: Long): Sale?

    @Transaction
    @Query("SELECT * FROM sales WHERE id = :id")
    fun getSaleWithDiscountsById(id: Long): Flow<SaleWithDiscounts>

    @Transaction
    @Query("SELECT * FROM sales WHERE sale_id = :saleId")
    fun getSaleWithDiscountsBySaleId(saleId: Long): Flow<SaleWithDiscounts>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(sale: Sale)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(vararg sale: Sale)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(sales: List<Sale>)

    @Update
    suspend fun update(sale: Sale)

    @Query("DELETE FROM sales where id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM sales where id in (:ids)")
    suspend fun deleteByIds(ids: List<Long>)

    @Query("DELETE FROM sales where sale_id = :saleId")
    suspend fun deleteBySaleId(saleId: Long)

    @Query("DELETE FROM sales")
    suspend fun deleteAll()
}