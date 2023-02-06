package com.ammar.platsnprices.data.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ammar.platsnprices.data.entities.Region
import com.ammar.platsnprices.data.entities.SalesLastUpdated
import kotlinx.coroutines.flow.Flow

@Dao
interface SalesLastUpdatedDao {
    @Query("SELECT * FROM sales_last_updated")
    fun getAll(): Flow<List<SalesLastUpdated>>

    @Query("SELECT * FROM sales_last_updated WHERE region = :region")
    suspend fun getByRegion(region: Region): SalesLastUpdated?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(salesLastUpdated: SalesLastUpdated)

    @Update
    suspend fun update(salesLastUpdated: SalesLastUpdated)
}