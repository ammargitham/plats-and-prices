package com.ammar.platsnprices.data.network

import com.ammar.platsnprices.data.entities.NetworkRecentDiscounts
import com.ammar.platsnprices.data.entities.NetworkSale
import com.ammar.platsnprices.data.entities.NetworkSales
import com.ammar.platsnprices.data.entities.Product
import retrofit2.http.GET
import retrofit2.http.Query

interface PlatPricesService {
    @GET(PLAT_PRICES_API_PATH)
    suspend fun fetchSales(@Query("sales") sales: Int = 1, @Query("region") region: String): NetworkSales

    @GET(PLAT_PRICES_API_PATH)
    suspend fun fetchSaleWithDiscounts(@Query("sale") saleId: Long): NetworkSale

    @GET(PLAT_PRICES_API_PATH)
    suspend fun fetchRecentDiscounts(@Query("discount") discount: Int = 1, @Query("region") region: String): NetworkRecentDiscounts

    @GET(PLAT_PRICES_API_PATH)
    suspend fun fetchProductByName(@Query("name") name: String): Product

    @GET(PLAT_PRICES_API_PATH)
    suspend fun fetchProductByPpId(@Query("ppid") ppId: Long): Product

    @GET(PLAT_PRICES_API_PATH)
    suspend fun fetchProductByPsnId(@Query("psnid") psnId: String): Product
}