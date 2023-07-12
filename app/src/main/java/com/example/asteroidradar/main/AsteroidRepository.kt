package com.example.asteroidradar.main


import com.example.asteroidradar.Asteroid
import com.example.asteroidradar.Constants
import com.example.asteroidradar.PictureOfDay
import com.example.asteroidradar.api.Network
import com.example.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject

class AsteroidRepository(private val mDatabase: AsteroidRadarDatabase) {

    suspend fun refreshAsteroids(
        startDate: String = getToday(),
        endDate: String = getSeventhDay()
    ) {
        var asteroidList: ArrayList<Asteroid>
        withContext(Dispatchers.IO) {
            val asteroidResponseBody: ResponseBody = Network.service.getAsteroidsAsync(
                startDate, endDate,
                Constants.API_KEY
            ).await()

            asteroidList = parseAsteroidsJsonResult(JSONObject(asteroidResponseBody.string()))
            mDatabase.getAsteroidDao().insertAll(*asteroidList.asDomainModel())


        }
    }

    suspend fun deletePreviousDayAsteroids() {
        withContext(Dispatchers.IO) {
            mDatabase.getAsteroidDao().deletePreviousDayAsteroids(getToday())
        }
    }

}