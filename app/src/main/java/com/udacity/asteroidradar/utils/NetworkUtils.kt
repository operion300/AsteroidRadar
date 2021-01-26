package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.database.AsteroidEntity
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.utils.Constants
import org.json.JSONObject
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

//method that returns a object list from Jason
fun parseAsteroidsJsonResult(jsonResult: JSONObject): ArrayList<Asteroid> {
    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")

    val asteroidList = ArrayList<Asteroid>()

    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
    for (formattedDate in nextSevenDaysFormattedDates) {
        val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)

        for (i in 0 until dateAsteroidJsonArray.length()) {
            val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
            val id = asteroidJson.getLong("id")
            val codename = asteroidJson.getString("name")
            val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
            val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
                .getJSONObject("kilometers").getDouble("estimated_diameter_max")

            val closeApproachData = asteroidJson
                .getJSONArray("close_approach_data").getJSONObject(0)
            val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                .getDouble("kilometers_per_second")
            val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
                .getDouble("astronomical")
            val isPotentiallyHazardous = asteroidJson
                .getBoolean("is_potentially_hazardous_asteroid")

            val asteroid = Asteroid(id, codename, formattedDate, absoluteMagnitude,
                    estimatedDiameter, relativeVelocity, distanceFromEarth, isPotentiallyHazardous)
            asteroidList.add(asteroid)
        }
    }

    return asteroidList
}

//method to get objects from next seven days
fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val dateList = ArrayList<String>()
    var currentTime:LocalDate = LocalDate.now()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        dateList.add(currentTime.toString())
        currentTime = currentTime.plusDays(1)
    }
    return dateList
}

//method to return formatted days
fun getFormattedDate(days: Long = 0):String{
    var currentDate:LocalDate = LocalDate.now()
    currentDate = currentDate.plusDays(days)
    return currentDate.toString()
}

//check if current day belongs to current week
fun getFormattedDateInCurrentWeek():String{
    var current: LocalDate = LocalDate.now()
    val firstDayOfNextWeek: LocalDate = current.with(TemporalAdjusters.next(DayOfWeek.MONDAY))
    while (current.isBefore(firstDayOfNextWeek)) {
        current = current.plusDays(1)
    }
    return current.toString()
}

//method to convert Json list to database array
fun String.asDatabaseAstObj():Array<AsteroidEntity>{
    return parseAsteroidsJsonResult(JSONObject(this)).map {
        AsteroidEntity(
                id = it.id,
                codename = it.codename,
                closeApproachDate = it.closeApproachDate,
                absoluteMagnitude = it.absoluteMagnitude,
                estimatedDiameter = it.estimatedDiameter,
                relativeVelocity = it.relativeVelocity,
                distanceFromEarth = it.distanceFromEarth,
                isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}