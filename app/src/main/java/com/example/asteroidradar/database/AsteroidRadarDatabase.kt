package com.example.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.DatabaseAsteroid


@Database(entities = [PictureOfDay::class, DatabaseAsteroid::class], version = 1, exportSchema = false)
abstract class AsteroidRadarDatabase : RoomDatabase()  {

    abstract fun getPictureDayDao(): PictureDayDao
    abstract fun getAsteroidDao(): AsteroidDao


    companion object {
        var INSTANCE: AsteroidRadarDatabase? = null

        fun initDatabase(context: Context): AsteroidRadarDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room.databaseBuilder(context,AsteroidRadarDatabase::class.java, "asteroids_database")
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}