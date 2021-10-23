package com.ammar.platsnprices.data.entities

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.ammar.platsnprices.R
import com.squareup.moshi.Json

enum class Genre {
    ACTION,
    ADVENTURE,
    ARCADE,
    FIGHTING,
    FPS,
    HORROR,
    STORY,
    MMO,
    MUSIC,
    PLATFORMER,
    PUZZLE,
    RACING,
    RPG,
    SIMULATION,
    SPORTS,
    STRATEGY,
    TPS,
}

enum class Trophy {
    BRONZE,
    SILVER,
    GOLD,
    PLATINUM,
}

enum class EsrbRating(
    val labels: List<String>,
    @DrawableRes val resId: Int
) {
    RP(listOf("Rating pending"), R.drawable.ic_esrb_rating_pending),
    E(listOf("Everyone"), R.drawable.ic_esrb_everyone),
    E10(listOf("Everyone 10+"), R.drawable.ic_esrb_everyone_10_plus),
    T(listOf("Teen"), R.drawable.ic_esrb_teen),
    M(listOf("Mature", "Mature 17+"), R.drawable.ic_esrb_mature),
    AO(listOf("Adults Only", "Adults Only 18+"), R.drawable.ic_esrb_adults_only);

    companion object {
        fun match(string: String): EsrbRating? {
            val ratingStr = if (string.startsWith("ESRB")) {
                val split = string.split(" ")
                if (split.size > 1) {
                    split[1]
                } else {
                    string
                }
            } else {
                string
            }
            return values().find {
                it.labels.any { label -> label == ratingStr }
            }
        }
    }
}

enum class GuideSource {
    PS_TROPHIES,
    PS3_IMPORTS,
    PYX,
    KNOEF,
    YOUTUBE,
    TROPHIES_DE,
    DEX,
    CUSTOM,
}

enum class Region(
    val code: String,
    @StringRes val labelResId: Int
) {
    AR("ar", R.string.argentina),
    BR("br", R.string.brazil),
    CA("ca", R.string.canada),
    CL("cl", R.string.chile),
    MX("mx", R.string.mexico),
    US("us", R.string.us),
    HK("hk", R.string.hk),
    ID("id", R.string.indonesia),
    JP("jp", R.string.japan),
    KR("kr", R.string.south_korea),
    MY("my", R.string.malaysia),
    SG("sg", R.string.singapore),
    TW("tw", R.string.taiwan),
    TH("th", R.string.thailand),
    AU("au", R.string.australia),
    AT("at", R.string.austria),
    BE("be", R.string.belgium),
    BG("bg", R.string.bulgaria),
    HR("hr", R.string.croatia),
    CZ("cz", R.string.czechia),
    DK("dk", R.string.denmark),
    FI("fi", R.string.finland),
    FR("fr", R.string.france),
    DE("de", R.string.germany),
    GR("gr", R.string.greece),
    HU("hu", R.string.hungary),
    IS("is", R.string.iceland),
    IN("in", R.string.india),
    IE("ie", R.string.ireland),
    IL("il", R.string.israel),
    IT("it", R.string.italy),
    NL("nl", R.string.netherlands),
    NZ("nz", R.string.nz),
    NO("no", R.string.norway),
    PL("pl", R.string.poland),
    PT("pt", R.string.portugal),
    RO("ro", R.string.romania),
    RU("ru", R.string.russia),
    SA("sa", R.string.saudi_arabia),
    SK("sk", R.string.slovakia),
    ZA("za", R.string.south_africa),
    ES("es", R.string.spain),
    SE("se", R.string.sweden),
    CH("ch", R.string.switzerland),
    TR("tr", R.string.turkey),
    UA("ua", R.string.ukraine),
    AE("ae", R.string.uae),
    GB("gb", R.string.uk);

    companion object {
        fun findByCode(code: String) = values().find { it.code == code }
    }
}