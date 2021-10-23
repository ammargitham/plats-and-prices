package com.ammar.platsnprices.utils

import android.content.Context
import com.ammar.platsnprices.R
import com.ammar.platsnprices.data.entities.Genre
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun getDiscountPct(basePrice: Int, discountedPrice: Int) = 100 * (basePrice - discountedPrice) / basePrice

data class DateTimeDiff(val value: Long, val type: ChronoUnit) {
    fun toFormattedString(context: Context): String {
        val unitStrRes = when (type) {
            ChronoUnit.SECONDS -> R.plurals.num_of_seconds
            ChronoUnit.MINUTES -> R.plurals.num_of_minutes
            ChronoUnit.HOURS -> R.plurals.num_of_hours
            ChronoUnit.DAYS -> R.plurals.num_of_days
            else -> 0
        }
        if (unitStrRes == 0) return ""
        return context.resources.getQuantityString(unitStrRes, value.toInt(), value)
    }
}

fun getDateTimeDiff(from: LocalDateTime): DateTimeDiff {
    val now = LocalDateTime.now()
    val between = Duration.between(now, from)
    var value = between.toDays()
    var type = ChronoUnit.DAYS
    if (value != 0L) {
        return DateTimeDiff(value, type)
    }
    value = between.toHours()
    type = ChronoUnit.HOURS
    if (value != 0L) {
        return DateTimeDiff(value, type)
    }
    value = between.toMinutes()
    type = ChronoUnit.MINUTES
    return DateTimeDiff(value, type)
}

val pnpDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
val pnpDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun genreStringRes(genre: Genre): Int = when (genre) {
    Genre.ACTION -> R.string.action
    Genre.ADVENTURE -> R.string.adventure
    Genre.ARCADE -> R.string.arcade
    Genre.FIGHTING -> R.string.fighting
    Genre.FPS -> R.string.fps
    Genre.HORROR -> R.string.horror
    Genre.STORY -> R.string.story
    Genre.MMO -> R.string.mmo
    Genre.MUSIC -> R.string.music
    Genre.PLATFORMER -> R.string.platformer
    Genre.PUZZLE -> R.string.puzzle
    Genre.RACING -> R.string.racing
    Genre.RPG -> R.string.rpg
    Genre.SIMULATION -> R.string.simulation
    Genre.SPORTS -> R.string.sports
    Genre.STRATEGY -> R.string.strategy
    Genre.TPS -> R.string.tps
}

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
inline fun <reified T : Enum<*>> valueOfOrNull(name: String?): T? = T::class.java.enumConstants.firstOrNull { it.name == name }

fun getFlag(code: String) = "https://flagcdn.com/w40/$code.png"