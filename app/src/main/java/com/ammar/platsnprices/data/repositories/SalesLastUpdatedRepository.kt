package com.ammar.platsnprices.data.repositories

import com.ammar.platsnprices.data.db.daos.SalesLastUpdatedDao
import com.ammar.platsnprices.data.entities.Region
import com.ammar.platsnprices.data.entities.SalesLastUpdated
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SalesLastUpdatedRepository @Inject constructor(private val salesLastUpdatedDao: SalesLastUpdatedDao) {
    suspend fun getByRegion(region: Region): SalesLastUpdated? = salesLastUpdatedDao.getByRegion(region)

    suspend fun insert(salesLastUpdated: SalesLastUpdated) = salesLastUpdatedDao.insert(salesLastUpdated)

    suspend fun insertOrUpdate(region: Region, dateTime: LocalDateTime) {
        val salesLastUpdated = getByRegion(region)
        if (salesLastUpdated != null) {
            salesLastUpdatedDao.update(salesLastUpdated.copy(lastUpdatedOn = dateTime))
            return
        }
        salesLastUpdatedDao.insert(SalesLastUpdated(0, region, dateTime))
    }
}