package com.ammar.platsnprices.data.entities

import androidx.room.*
import com.ammar.platsnprices.utils.getDiscountPct
import com.ammar.platsnprices.utils.parsePlatPricesDateTime
import com.squareup.moshi.*
import com.squareup.moshi.JsonReader.Token.BEGIN_OBJECT
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class NetworkSales(
    val sales: List<NetworkSale> = emptyList(),
    val error: Int = 0,
    val errorDesc: String = "",
    val apiLimit: Int = 0,
    val apiUsage: Int = 0,
) : NetworkBase(
    error,
    errorDesc,
    apiLimit,
    apiUsage,
)

@JsonClass(generateAdapter = true)
data class NetworkSale(
    @Json(name = "ID") val id: Long?, // ID is not returned only with the fetchSaleWithDiscounts call
    @Json(name = "SaleName") val name: String,
    @Json(name = "SaleTime") val startTimeStr: String,
    @Json(name = "SaleEnd") val endTimeStr: String,
    @Json(name = "NumGames") val numGames: Int?, // NumGames is not returned only with the fetchSaleWithDiscounts call
    @Json(name = "ImgURL") val imgUrl: String,
    @Json(name = "Region") val region: String?,
    @Json(name = "game_discounts") val gameDiscounts: List<NetworkGameDiscount>?,
    @Json(name = "dlc_discounts") val dlcDiscounts: List<NetworkDlcDiscount>?,
) {
    fun toSale(
        dbId: Long = 0,
        region: Region = Region.US,
    ): Sale = Sale(
        id = dbId,
        saleId = id ?: 0,
        name = name,
        startTime = startTimeStr.parsePlatPricesDateTime(),
        endTime = endTimeStr.parsePlatPricesDateTime(),
        region = region,
        numGames = numGames ?: 0,
        imgUrl = imgUrl,
    )
}

@Entity(tableName = "sales")
data class Sale(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "sale_id") val saleId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "start_time") val startTime: LocalDateTime?,
    @ColumnInfo(name = "end_time") val endTime: LocalDateTime?,
    @ColumnInfo(name = "region") val region: Region,
    @ColumnInfo(name = "num_games") val numGames: Int,
    @ColumnInfo(name = "img_url") val imgUrl: String,
)

@JsonClass(generateAdapter = true)
data class NetworkRecentDiscounts(
    @NetworkRecentDiscountsStringList val discounts: List<NetworkGameDiscount>,
    val error: Int = 0,
    val errorDesc: String = "",
    val apiLimit: Int = 0,
    val apiUsage: Int = 0,
) : NetworkBase(
    error,
    errorDesc,
    apiLimit,
    apiUsage,
)

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class NetworkRecentDiscountsStringList

internal class NetworkRecentDiscountsStringListAdapter {
    @ToJson
    fun toJson(@NetworkRecentDiscountsStringList discounts: List<NetworkGameDiscount>): String {
        val moshi = Moshi.Builder().build()
        return moshi.adapter(List::class.java).toJson(discounts)
    }

    @FromJson
    @NetworkRecentDiscountsStringList
    fun fromJson(
        jsonReader: JsonReader,
        delegateList: JsonAdapter<List<NetworkGameDiscount>>,
        delegateMap: JsonAdapter<Map<String, NetworkGameDiscount?>>
    ): List<NetworkGameDiscount> {
        return if (jsonReader.peek() == BEGIN_OBJECT) {
            delegateMap.fromJson(jsonReader)?.values?.filterNotNull() ?: emptyList()
        } else {
            delegateList.fromJson(jsonReader) ?: emptyList()
        }
    }
}

@Entity(tableName = "recent_game_discounts")
data class RecentGameDiscount(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "pp_id") val ppId: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "region") val region: Region,
    @ColumnInfo(name = "img_url") val imgUrl: String,
    @ColumnInfo(name = "difficulty") val difficulty: Int,
    @ColumnInfo(name = "hours_low") val hoursLow: Float,
    @ColumnInfo(name = "hours_high") val hoursHigh: Float,
    @ColumnInfo(name = "is_ps4") val isPs4: Boolean,
    @ColumnInfo(name = "is_ps5") val isPs5: Boolean,
    @ColumnInfo(name = "last_discounted") val lastDiscounted: LocalDateTime?,
    @ColumnInfo(name = "discounted_until") val discountedUntil: LocalDateTime?,
    @ColumnInfo(name = "base_price") val basePrice: Int,
    @ColumnInfo(name = "sale_price") val salePrice: Int,
    @ColumnInfo(name = "plus_price") val plusPrice: Int,
    @ColumnInfo(name = "formatted_base_price") val formattedBasePrice: String,
    @ColumnInfo(name = "formatted_sale_price") val formattedSalePrice: String,
    @ColumnInfo(name = "formatted_plus_price") val formattedPlusPrice: String,
    @ColumnInfo(name = "plat_prices_url") val platPricesUrl: String,
) {
    val saleDiscountPct: Int by lazy { getDiscountPct(basePrice, salePrice) }
    val plusDiscountPct: Int by lazy { getDiscountPct(basePrice, plusPrice) }
}

@JsonClass(generateAdapter = true)
data class NetworkGameDiscount(
    @Json(name = "PPID") val ppId: Long,
    @Json(name = "Name") val name: String,
    @Json(name = "Img") val imgUrl: String,
    @Json(name = "Difficulty") val difficulty: Int,
    @Json(name = "HoursLow") val hoursLow: Float,
    @Json(name = "HoursHigh") val hoursHigh: Float,
    @Json(name = "IsPS4") val isPs4Int: Int,
    @Json(name = "IsPS5") val isPs5Int: Int,
    @Json(name = "LastDiscounted") val lastDiscountedStr: String,
    @Json(name = "DiscountedUntil") val discountedUntilStr: String,
    @Json(name = "BasePrice") val basePrice: Int,
    @Json(name = "SalePrice") val salePrice: Int,
    @Json(name = "PlusPrice") val plusPrice: Int,
    @Json(name = "formattedBasePrice") val formattedBasePrice: String,
    @Json(name = "formattedSalePrice") val formattedSalePrice: String,
    @Json(name = "formattedPlusPrice") val formattedPlusPrice: String,
    @Json(name = "PlatPricesURL") val platPricesUrl: String,
) {
    fun toGameDiscount(dbId: Long = 0, saleDbId: Long) = GameDiscount(
        id = dbId,
        ppId = ppId,
        saleDbId = saleDbId,
        name = name,
        imgUrl = imgUrl,
        difficulty = difficulty,
        hoursLow = hoursLow,
        hoursHigh = hoursHigh,
        isPs4 = isPs4Int == 1,
        isPs5 = isPs5Int == 1,
        lastDiscounted = lastDiscountedStr.parsePlatPricesDateTime(),
        discountedUntil = discountedUntilStr.parsePlatPricesDateTime(),
        basePrice = basePrice,
        salePrice = salePrice,
        plusPrice = plusPrice,
        formattedBasePrice = formattedBasePrice,
        formattedSalePrice = formattedSalePrice,
        formattedPlusPrice = formattedPlusPrice,
        platPricesUrl = platPricesUrl,
    )

    fun toRecentGameDiscount(dbId: Long = 0, region: Region) = RecentGameDiscount(
        id = dbId,
        ppId = ppId,
        name = name,
        region = region,
        imgUrl = imgUrl,
        difficulty = difficulty,
        hoursLow = hoursLow,
        hoursHigh = hoursHigh,
        isPs4 = isPs4Int == 1,
        isPs5 = isPs5Int == 1,
        lastDiscounted = lastDiscountedStr.parsePlatPricesDateTime(),
        discountedUntil = discountedUntilStr.parsePlatPricesDateTime(),
        basePrice = basePrice,
        salePrice = salePrice,
        plusPrice = plusPrice,
        formattedBasePrice = formattedBasePrice,
        formattedSalePrice = formattedSalePrice,
        formattedPlusPrice = formattedPlusPrice,
        platPricesUrl = platPricesUrl,
    )
}

@JsonClass(generateAdapter = true)
data class NetworkDlcDiscount(
    @Json(name = "PPID") val ppId: Long,
    @Json(name = "Name") val name: String,
    @Json(name = "Img") val imgUrl: String,
    @Json(name = "BasePrice") val basePrice: Int,
    @Json(name = "SalePrice") val salePrice: Int,
    @Json(name = "PlusPrice") val plusPrice: Int,
    @Json(name = "formattedBasePrice") val formattedBasePrice: String,
    @Json(name = "formattedSalePrice") val formattedSalePrice: String,
    @Json(name = "formattedPlusPrice") val formattedPlusPrice: String,
    @Json(name = "PlatPricesURL") val platPricesUrl: String,
    @Json(name = "ParentGame") val parentGame: String,
) {
    fun toDlcDiscount(dbId: Long = 0, saleDbId: Long) = DlcDiscount(
        id = dbId,
        ppId = ppId,
        saleDbId = saleDbId,
        name = name,
        imgUrl = imgUrl,
        basePrice = basePrice,
        salePrice = salePrice,
        plusPrice = plusPrice,
        formattedBasePrice = formattedBasePrice,
        formattedSalePrice = formattedSalePrice,
        formattedPlusPrice = formattedPlusPrice,
        platPricesUrl = platPricesUrl,
        parentGame = parentGame,
    )
}

abstract class Discount(
    open val ppId: Long,
    open val saleDbId: Long,
    open val name: String,
    open val imgUrl: String,
    open val basePrice: Int,
    open val salePrice: Int,
    open val plusPrice: Int,
    open val formattedBasePrice: String,
    open val formattedSalePrice: String,
    open val formattedPlusPrice: String,
    open val platPricesUrl: String,
) {
    val saleDiscountPct: Int by lazy { getDiscountPct(basePrice, salePrice) }
    val plusDiscountPct: Int by lazy { getDiscountPct(basePrice, plusPrice) }
}

@Entity(tableName = "game_discounts")
data class GameDiscount(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "pp_id") override val ppId: Long,
    @ColumnInfo(name = "sale_db_id") override val saleDbId: Long,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "img_url") override val imgUrl: String,
    @ColumnInfo(name = "difficulty") val difficulty: Int,
    @ColumnInfo(name = "hours_low") val hoursLow: Float,
    @ColumnInfo(name = "hours_high") val hoursHigh: Float,
    @ColumnInfo(name = "is_ps4") val isPs4: Boolean,
    @ColumnInfo(name = "is_ps5") val isPs5: Boolean,
    @ColumnInfo(name = "last_discounted") val lastDiscounted: LocalDateTime?,
    @ColumnInfo(name = "discounted_until") val discountedUntil: LocalDateTime?,
    @ColumnInfo(name = "base_price") override val basePrice: Int,
    @ColumnInfo(name = "sale_price") override val salePrice: Int,
    @ColumnInfo(name = "plus_price") override val plusPrice: Int,
    @ColumnInfo(name = "formatted_base_price") override val formattedBasePrice: String,
    @ColumnInfo(name = "formatted_sale_price") override val formattedSalePrice: String,
    @ColumnInfo(name = "formatted_plus_price") override val formattedPlusPrice: String,
    @ColumnInfo(name = "plat_prices_url") override val platPricesUrl: String,
) : Discount(
    ppId = ppId,
    saleDbId = saleDbId,
    name = name,
    imgUrl = imgUrl,
    basePrice = basePrice,
    salePrice = salePrice,
    plusPrice = plusPrice,
    formattedBasePrice = formattedBasePrice,
    formattedSalePrice = formattedSalePrice,
    formattedPlusPrice = formattedPlusPrice,
    platPricesUrl = platPricesUrl,
)

@Entity(tableName = "dlc_discounts")
data class DlcDiscount(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "pp_id") override val ppId: Long,
    @ColumnInfo(name = "sale_db_id") override val saleDbId: Long,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "img_url") override val imgUrl: String,
    @ColumnInfo(name = "base_price") override val basePrice: Int,
    @ColumnInfo(name = "sale_price") override val salePrice: Int,
    @ColumnInfo(name = "plus_price") override val plusPrice: Int,
    @ColumnInfo(name = "formatted_base_price") override val formattedBasePrice: String,
    @ColumnInfo(name = "formatted_sale_price") override val formattedSalePrice: String,
    @ColumnInfo(name = "formatted_plus_price") override val formattedPlusPrice: String,
    @ColumnInfo(name = "plat_prices_url") override val platPricesUrl: String,
    @ColumnInfo(name = "parent_game") val parentGame: String,
) : Discount(
    ppId = ppId,
    saleDbId = saleDbId,
    name = name,
    imgUrl = imgUrl,
    basePrice = basePrice,
    salePrice = salePrice,
    plusPrice = plusPrice,
    formattedBasePrice = formattedBasePrice,
    formattedSalePrice = formattedSalePrice,
    formattedPlusPrice = formattedPlusPrice,
    platPricesUrl = platPricesUrl,
)

data class SaleWithDiscounts(
    @Embedded val sale: Sale,
    @Relation(
        parentColumn = "id",
        entityColumn = "sale_db_id"
    )
    val gameDiscounts: List<GameDiscount>?,
    @Relation(
        parentColumn = "id",
        entityColumn = "sale_db_id"
    )
    val dlcDiscounts: List<DlcDiscount>?
)
