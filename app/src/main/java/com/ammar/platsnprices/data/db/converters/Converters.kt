package com.ammar.platsnprices.data.db.converters

import androidx.room.TypeConverter
import com.ammar.platsnprices.data.entities.Region
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

object Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? = value?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.systemDefault()) }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? = date?.atZone(ZoneOffset.systemDefault())?.toInstant()?.toEpochMilli()

    @TypeConverter
    fun fromRegionCode(value: String?): Region? = value?.let { Region.findByCode(it) }

    @TypeConverter
    fun regionToCode(region: Region?): String? = region?.code

    @TypeConverter
    fun secondsToDuration(value: Long?): Duration? = value?.let { Duration.of(it, ChronoUnit.SECONDS) }

    @TypeConverter
    fun durationToSeconds(duration: Duration?): Long? = duration?.seconds
}