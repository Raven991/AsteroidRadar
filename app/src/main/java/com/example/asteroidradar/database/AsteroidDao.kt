package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.*
import com.example.asteroidradar.Asteroid
import com.example.asteroidradar.PictureOfDay
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM ASTEROID_TABLE WHERE closeApproachDate >= :startDate AND closeApproachDate <= :endDate ORDER BY closeApproachDate ASC")
    fun getAsteroidsByCloseApproachDate(startDate: String, endDate: String): Flow<List<Asteroid>>

    @Query("SELECT * FROM ASTEROID_TABLE ORDER BY closeApproachDate ASC")
    fun getAllAsteroids(): Flow<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("DELETE FROM ASTEROID_TABLE WHERE closeApproachDate < :today")
    fun deletePreviousDayAsteroids(today: String): Int
}
