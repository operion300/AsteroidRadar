package com.udacity.asteroidradar.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.APODEntity

//apod api model
@JsonClass(generateAdapter = true)
data class PictureOfDay(
        @Json(name = "media_type")
        val mediaType: String,
        val title: String,
        val url: String
)

//converts api object to apod database
fun PictureOfDay.asDatabaseObj(): APODEntity {
    return APODEntity(
                id = 1,
                title = title,
                url = url
        )
}
