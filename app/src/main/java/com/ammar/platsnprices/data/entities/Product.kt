package com.ammar.platsnprices.data.entities

import android.util.Log
import com.ammar.platsnprices.utils.TAG
import com.ammar.platsnprices.utils.getDiscountPct
import com.ammar.platsnprices.utils.pnpDateFormatter
import com.ammar.platsnprices.utils.pnpDateTimeFormatter
import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeParseException
import java.util.Locale

@JsonClass(generateAdapter = true)
data class Product(
    @Json(name = "PPID") val ppId: Long,
    @Json(name = "PSNID") val psnId: String,
    @Json(name = "GameID") val gameId: String,
    @Json(name = "PlatPricesURL") val platPricesUrl: String,
    @Json(name = "PSStoreURL") val psStoreUrl: String,
    @Json(name = "ProductName") val productName: String,
    @Json(name = "GameName") val gameName: String,
    @Json(name = "Publisher") val publisher: String,
    @Json(name = "Developer") val developer: String,
    @Json(name = "ReleaseDate") val releaseDateStr: String,
    @Json(name = "Desc") val description: String,
    @Json(name = "IsPS4") val isPs4Int: Int,
    @Json(name = "IsPS5") val isPs5Int: Int,
    @Json(name = "IsDLC") val isDlcInt: Int,
    @Json(name = "IsDemoOrSoundtrack") val isDemoOrSoundtrackInt: Int,
    @Json(name = "IsVR") val isVrInt: Int,
    @Json(name = "IsMove") val isMoveInt: Int,
    @Json(name = "VitaCB") val isVitaCrossBuyInt: Int,
    @Json(name = "PS4Size") val ps4SizeBytes: Long,
    @Json(name = "PS5Size") val ps5SizeBytes: Long,
    @Json(name = "OnlinePlay") val onlinePlayTypeInt: Int,
    @Json(name = "OfflinePlayers") val offlinePlayers: Int,
    @Json(name = "OnlinePlayers") val onlinePlayers: Int,
    @Json(name = "PSPlusNeeded") val isPsPlusNeededInt: Int,
    @Json(name = "VoiceLang") @ProductStringList val voiceLanguages: List<String>,
    @Json(name = "SubtitleLang") @ProductStringList val subtitleLanguages: List<String>,
    @Json(name = "Rating") val rating: String,
    @Json(name = "RatingDesc") @ProductStringList val ratingDescription: List<String>,
    @Json(name = "OpenCriticID") val openCriticId: Long,
    @Json(name = "MetacriticURL") val metacriticUrl: String,
    @Json(name = "Img") val imgUrl: String,
    @Json(name = "CoverArt") val coverArtUrl: String,
    @Json(name = "LogoImg") val logoImgUrl: String,
    @Json(name = "Screenshot1") val screenShotUrl1: String,
    @Json(name = "Screenshot2") val screenShotUrl2: String,
    @Json(name = "Screenshot3") val screenShotUrl3: String,
    @Json(name = "Screenshot4") val screenShotUrl4: String,
    @Json(name = "Screenshot5") val screenShotUrl5: String,
    @Json(name = "Screenshot6") val screenShotUrl6: String,
    @Json(name = "Screenshot7") val screenShotUrl7: String,
    @Json(name = "Screenshot8") val screenShotUrl8: String,
    @Json(name = "Screenshot9") val screenShotUrl9: String,
    @Json(name = "PreviewVideo") val previewVideoUrl: String,
    @Json(name = "GenreAction") val genreActionInt: Int,
    @Json(name = "GenreAdventure") val genreAdventureInt: Int,
    @Json(name = "GenreArcade") val genreArcadeInt: Int,
    @Json(name = "GenreFighting") val genreFightingInt: Int,
    @Json(name = "GenreFPS") val genreFpsInt: Int,
    @Json(name = "GenreHorror") val genreHorrorInt: Int,
    @Json(name = "GenreIntStory") val genreStoryInt: Int,
    @Json(name = "GenreMMO") val genreMmoInt: Int,
    @Json(name = "GenreMusic") val genreMusicInt: Int,
    @Json(name = "GenrePlatformer") val genrePlatformerInt: Int,
    @Json(name = "GenrePuzzle") val genrePuzzleInt: Int,
    @Json(name = "GenreRacing") val genreRacingInt: Int,
    @Json(name = "GenreRPG") val genreRpgInt: Int,
    @Json(name = "GenreSimulation") val genreSimulationInt: Int,
    @Json(name = "GenreSports") val genreSportsInt: Int,
    @Json(name = "GenreStrategy") val genreStrategyInt: Int,
    @Json(name = "GenreTPS") val genreTpsInt: Int,
    @Json(name = "Bronze") val trophyBronzeCount: Int,
    @Json(name = "Silver") val trophySilverCount: Int,
    @Json(name = "Gold") val trophyGoldCount: Int,
    @Json(name = "Platinum") val trophyPlatinumCount: Int,
    @Json(name = "Difficulty") val difficulty: Int,
    @Json(name = "HoursLow") val hoursLow: Float,
    @Json(name = "HoursHigh") val hoursHigh: Float,
    @Json(name = "TrophyListURL") val trophyGuideUrl: String,
    // @Json(name = "GuidePSNP") val guidePsnProfiles: String,
    @Json(name = "GuidePS3T") val guidePlaystationTrophiesUrl: String,
    @Json(name = "GuidePS3I") val guidePs3ImportsUrl: String,
    @Json(name = "GuidePyx") val guidePowerPyxUrl: String,
    @Json(name = "GuideKnoef") val guideKnoef: String,
    @Json(name = "GuideYoutube") val guideYoutube: String,
    @Json(name = "GuideTrophiesDE") val guideTrophiesDe: String,
    @Json(name = "GuideDex") val guideDex: String,
    @Json(name = "GuideCust") val guideCust: String,
    @Json(name = "GuideCustLabel") val guideCustLabel: String,
    @Json(name = "Region") val region: String,
    @Json(name = "BasePrice") val basePrice: Int,
    @Json(name = "PlusPrice") val plusPrice: Int,
    @Json(name = "SalePrice") val salePrice: Int,
    @Json(name = "formattedBasePrice") val formattedBasePrice: String,
    @Json(name = "formattedSalePrice") val formattedSalePrice: String,
    @Json(name = "formattedPlusPrice") val formattedPlusPrice: String,
    @Json(name = "DiscPerc") val discountPct: Int,
    @Json(name = "LastDiscounted") val lastDiscountedStr: String, // "2021-02-10 09:11:04"
    @Json(name = "DiscountedUntil") val discountedUntilStr: String,
    val error: Int,
    val errorDesc: String,
    val apiLimit: Int,
    val apiUsage: Int
) : NetworkBase(
    error,
    errorDesc,
    apiLimit,
    apiUsage,
) {
    val isPs4 by lazy { isPs4Int == 1 }
    val isPs5 by lazy { isPs5Int == 1 }
    val isPsPlusNeeded by lazy { isPsPlusNeededInt == 1 }
    val platforms = listOf(
        if (isPs4) "PS4" else "",
        if (isPs5) "PS5" else "",
    ).filter { it.isNotBlank() }

    val screenShotUrls by lazy {
        listOf(
            screenShotUrl1,
            screenShotUrl2,
            screenShotUrl3,
            screenShotUrl4,
            screenShotUrl5,
            screenShotUrl6,
            screenShotUrl7,
            screenShotUrl8,
            screenShotUrl9,
        ).filter { it.isNotBlank() }
    }
    val genres: List<Genre> by lazy {
        mapOf(
            Genre.ACTION to genreActionInt,
            Genre.ADVENTURE to genreAdventureInt,
            Genre.ARCADE to genreArcadeInt,
            Genre.FIGHTING to genreFightingInt,
            Genre.FPS to genreFpsInt,
            Genre.HORROR to genreHorrorInt,
            Genre.STORY to genreStoryInt,
            Genre.MMO to genreMmoInt,
            Genre.MUSIC to genreMusicInt,
            Genre.PLATFORMER to genrePlatformerInt,
            Genre.PUZZLE to genrePuzzleInt,
            Genre.RACING to genreRacingInt,
            Genre.RPG to genreRpgInt,
            Genre.SIMULATION to genreSimulationInt,
            Genre.SPORTS to genreSportsInt,
            Genre.STRATEGY to genreStrategyInt,
            Genre.TPS to genreTpsInt,
        )
            .asSequence()
            .filter { it.value == 1 }
            .map { it.key }
            .toList()
    }
    val trophies: Map<Trophy, Int> by lazy {
        mapOf(
            Trophy.BRONZE to trophyBronzeCount,
            Trophy.SILVER to trophySilverCount,
            Trophy.GOLD to trophyGoldCount,
            Trophy.PLATINUM to trophyPlatinumCount,
        )
    }
    val saleDiscountPct: Int by lazy { getDiscountPct(basePrice, salePrice) }
    val plusDiscountPct: Int by lazy { getDiscountPct(basePrice, plusPrice) }
    val discountEndTime: LocalDateTime? by lazy {
        if (discountedUntilStr.isBlank()) return@lazy null
        try {
            LocalDateTime.parse(discountedUntilStr, pnpDateTimeFormatter)
        } catch (e: DateTimeParseException) {
            Log.e(TAG, "Error parsing date time", e)
            null
        }
    }
    val voiceLanguageLabels: List<String> by lazy {
        voiceLanguages
            .asSequence()
            .map { Locale.forLanguageTag(it.replace("_", "-")) }
            .map { it.displayName }
            .filter { it.isNotBlank() }
            .sorted()
            .toList()
    }
    val subtitleLanguageLabels: List<String> by lazy {
        subtitleLanguages
            .asSequence()
            .map { Locale.forLanguageTag(it.replace("_", "-")) }
            .map { it.displayName }
            .filter { it.isNotBlank() }
            .sorted()
            .toList()
    }
    val releaseDate: LocalDate? by lazy {
        if (releaseDateStr.isBlank()) return@lazy null
        try {
            LocalDate.parse(releaseDateStr, pnpDateFormatter)
        } catch (e: DateTimeParseException) {
            Log.e(TAG, "Error parsing date time", e)
            null
        }
    }
    val guides: Map<GuideSource, String> by lazy {
        var map = mapOf(
            GuideSource.PS_TROPHIES to guidePlaystationTrophiesUrl,
            GuideSource.PS3_IMPORTS to guidePs3ImportsUrl,
            GuideSource.PYX to guidePowerPyxUrl,
            GuideSource.KNOEF to guideKnoef,
            GuideSource.YOUTUBE to guideYoutube,
            GuideSource.TROPHIES_DE to guideTrophiesDe,
            GuideSource.DEX to guideDex,
        ).filterValues { it.isNotBlank() }
        if (guideCustLabel.isNotBlank() && guideCust.isNotBlank()) {
            map = map.toMutableMap()
            map[GuideSource.CUSTOM] = "$guideCustLabel~~~~$guideCust"
        }
        map
    }
}

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class ProductStringList

internal class ProductStringListAdapter {
    @ToJson
    fun toJson(@ProductStringList voiceLanguages: List<String>): String {
        val moshi = Moshi.Builder().build()
        return moshi.adapter(List::class.java).toJson(voiceLanguages)
    }

    @Suppress("UNCHECKED_CAST")
    @FromJson
    @ProductStringList
    fun fromJson(arrayStr: String): List<String> {
        if (arrayStr.isBlank()) return emptyList()
        val moshi = Moshi.Builder().build()
        return try {
            moshi.adapter(List::class.java).fromJson(arrayStr) as List<String>
        } catch (e: JsonDataException) {
            val map = moshi.adapter(Map::class.java).fromJson(arrayStr) as Map<String, String>
            map.values.toList()
        }
    }
}
