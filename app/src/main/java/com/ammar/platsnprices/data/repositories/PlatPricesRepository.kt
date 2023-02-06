package com.ammar.platsnprices.data.repositories

import com.ammar.platsnprices.data.db.daos.DlcDiscountDao
import com.ammar.platsnprices.data.db.daos.GameDiscountDao
import com.ammar.platsnprices.data.db.daos.RecentGameDiscountDao
import com.ammar.platsnprices.data.db.daos.SaleDao
import com.ammar.platsnprices.data.entities.NetworkSale
import com.ammar.platsnprices.data.entities.Product
import com.ammar.platsnprices.data.entities.RecentGameDiscount
import com.ammar.platsnprices.data.entities.Region
import com.ammar.platsnprices.data.entities.Resource
import com.ammar.platsnprices.data.entities.Sale
import com.ammar.platsnprices.data.entities.SaleWithDiscounts
import com.ammar.platsnprices.data.entities.networkBoundResource
import com.ammar.platsnprices.data.network.PlatPricesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlatPricesRepository @Inject constructor(
    private val platPricesService: PlatPricesService,
    private val saleDao: SaleDao,
    private val salesLastUpdatedRepository: SalesLastUpdatedRepository,
    private val gameDiscountDao: GameDiscountDao,
    private val dlcDiscountDao: DlcDiscountDao,
    private val recentGameDiscountDao: RecentGameDiscountDao,
) {
    suspend fun getSales(region: Region): Flow<Resource<List<Sale>>> = withContext(Dispatchers.IO) {
        networkBoundResource(
            dbQuery = { saleDao.getAllByRegion(region) },
            shouldFetch = {
                val salesLastUpdated = salesLastUpdatedRepository.getByRegion(region)
                salesLastUpdated == null || Duration.between(LocalDateTime.now(), salesLastUpdated.lastUpdatedOn).abs().toHours() > 12
            },
            fetch = {
                platPricesService.fetchSales(region = region.code)
            },
            saveFetchResult = {
                salesLastUpdatedRepository.insertOrUpdate(region, LocalDateTime.now())
                val inserts = mutableListOf<NetworkSale>()
                // Filter out Network sales already in db
                for (networkSale in it.sales) {
                    val id = networkSale.id ?: continue
                    val sale = saleDao.getBySaleId(id)
                    if (sale == null) {
                        inserts.add(networkSale)
                    }
                }
                saleDao.insertAll(inserts.map { networkSale -> networkSale.toSale(region = region) })
            }
        )
    }

    suspend fun getSaleWithDiscounts(saleDbId: Long): Flow<Resource<SaleWithDiscounts>> = withContext(Dispatchers.IO) {
        networkBoundResource(
            dbQuery = { saleDao.getSaleWithDiscountsById(saleDbId) },
            shouldFetch = {
                it.gameDiscounts.isNullOrEmpty() && it.dlcDiscounts.isNullOrEmpty()
            },
            fetch = {
                platPricesService.fetchSaleWithDiscounts(it.sale.saleId)
            },
            saveFetchResult = {
                val gameDiscounts = it.gameDiscounts ?: emptyList()
                gameDiscountDao.insertAll(gameDiscounts.map { d -> d.toGameDiscount(saleDbId = saleDbId) })
                val dlcDiscounts = it.dlcDiscounts ?: emptyList()
                dlcDiscountDao.insertAll(dlcDiscounts.map { d -> d.toDlcDiscount(saleDbId = saleDbId) })
            }
        )
    }

    suspend fun getRecentDiscounts(region: Region): Flow<Resource<List<RecentGameDiscount>>> = withContext(Dispatchers.IO) {
        networkBoundResource(
            dbQuery = { recentGameDiscountDao.getAllByRegion(region) },
            shouldFetch = { it.isEmpty() },
            fetch = {
                platPricesService.fetchRecentDiscounts(region = region.code)
            },
            saveFetchResult = {
                recentGameDiscountDao.insertAll(it.discounts.map { networkGameDiscount ->
                    networkGameDiscount.toRecentGameDiscount(region = region)
                })
            }
        )
    }

    suspend fun getProductByName(name: String): Product = withContext(Dispatchers.IO) { platPricesService.fetchProductByName(name) }

    suspend fun getProductByPpId(ppId: Long): Product = withContext(Dispatchers.IO) { platPricesService.fetchProductByPpId(ppId) }

    suspend fun getProductByPsnId(psnId: String): Product = withContext(Dispatchers.IO) { platPricesService.fetchProductByPsnId(psnId) }
}