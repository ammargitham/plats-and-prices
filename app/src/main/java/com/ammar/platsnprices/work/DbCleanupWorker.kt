package com.ammar.platsnprices.work

import android.content.Context
import android.os.Build
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.ammar.platsnprices.data.db.daos.DlcDiscountDao
import com.ammar.platsnprices.data.db.daos.GameDiscountDao
import com.ammar.platsnprices.data.db.daos.RecentGameDiscountDao
import com.ammar.platsnprices.data.db.daos.SaleDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class DbCleanupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val salesDao: SaleDao,
    private val gameDiscountDao: GameDiscountDao,
    private val dlcDiscountDao: DlcDiscountDao,
    private val recentGameDiscountDao: RecentGameDiscountDao,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        cleanup()
        return Result.success()
    }

    private suspend fun cleanup() {
        cleanupExpiredSales()
        cleanupRecentDiscounts()
    }

    private suspend fun cleanupExpiredSales() {
        val expiredSaleDbIds = salesDao.getExpiredSaleIds()
        if (expiredSaleDbIds.isEmpty()) return
        // Delete games and dlc discounts using the above ids
        gameDiscountDao.deleteBySaleDbIds(expiredSaleDbIds)
        dlcDiscountDao.deleteBySaleDbIds(expiredSaleDbIds)
        // Delete the expired sales
        salesDao.deleteByIds(expiredSaleDbIds)
    }

    private suspend fun cleanupRecentDiscounts() {
        recentGameDiscountDao.deleteAll()
    }

    companion object {
        fun enqueue(context: Context) {
            val constraintsBuilder = Constraints.Builder()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                constraintsBuilder.setRequiresDeviceIdle(true)
            }
            val workRequest = PeriodicWorkRequestBuilder<DbCleanupWorker>(24, TimeUnit.HOURS)
                .setInitialDelay(24, TimeUnit.HOURS)
                .setConstraints(constraintsBuilder.build())
                .build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "db_cleanup",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }
    }
}