package com.example.asteroidradar

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
@Entity(tableName = "picofDay")
data class PictureOfDay(
    @PrimaryKey(autoGenerate = false)
    val uid: Long = 0,
    @Json(name = "media_type")
    val mediaType: String,
    val title: String,
    val url: String
)