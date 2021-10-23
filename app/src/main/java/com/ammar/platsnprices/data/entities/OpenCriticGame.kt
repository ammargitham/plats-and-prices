package com.ammar.platsnprices.data.entities

import com.ammar.platsnprices.utils.slugify
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenCriticGame(
    val id: Long,
    val name: String,
    val percentRecommended: Float,
    val numReviews: Int,
    val numTopCriticReviews: Int,
    val numUserReviews: Int,
    val medianScore: Float,
    val averageScore: Float,
    val topCriticScore: Float,
    val tier: Tier,
) {
    @Transient
    private val url = "https://opencritic.com/game/${id}/${name.slugify()}"
    val reviewsUrl by lazy { "$url/reviews" }
}

//@Retention(AnnotationRetention.RUNTIME)
//@JsonQualifier
//annotation class Tier

internal class TierAdapter {
    @FromJson
    fun fromJson(string: String): Tier = when (string.lowercase()) {
        "mighty" -> Tier.MIGHTY
        "strong" -> Tier.STRONG
        "fair" -> Tier.FAIR
        "weak" -> Tier.WEAK
        else -> Tier.UNKNOWN
    }
}

enum class Tier {
    MIGHTY,
    STRONG,
    FAIR,
    WEAK,
    UNKNOWN,
}