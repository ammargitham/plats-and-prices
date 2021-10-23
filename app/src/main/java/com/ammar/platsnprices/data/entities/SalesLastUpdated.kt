package com.ammar.platsnprices.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "sales_last_updated")
data class SalesLastUpdated(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "region") val region: Region,
    @ColumnInfo(name = "last_updated_on") val lastUpdatedOn: LocalDateTime?,
)