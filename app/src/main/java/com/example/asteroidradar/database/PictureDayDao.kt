package com.example.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.asteroidradar.PictureOfDay


@Dao
interface PictureDayDao {
    @Query("SELECT * FROM picofDay")
   fun get(): PictureOfDay

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertPic(pic: com.example.asteroidradar.PictureOfDay)


}