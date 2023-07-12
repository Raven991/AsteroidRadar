package com.example.asteroidradar.main

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.example.asteroidradar.Asteroid
import com.example.asteroidradar.Constants.IMAGE_MEDIA_TYPE
import com.example.asteroidradar.PictureOfDay
import com.example.asteroidradar.api.Network
import com.example.asteroidradar.database.AsteroidRadarDatabase
import com.example.asteroidradar.database.AsteroidRadarDatabase.Companion.INSTANCE
import com.udacity.asteroidradar.api.getSeventhDay
import com.udacity.asteroidradar.api.getToday
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val mDatabase = AsteroidRadarDatabase.initDatabase(application)
    private val asteroidRepository = AsteroidRepository(mDatabase)

    private val _navigateToDetailFragment = MutableLiveData<Asteroid?>()
    val navigateToDetailFragment: LiveData<Asteroid?>
        get() = _navigateToDetailFragment

    private var _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    private val _pictureOfDay = MutableLiveData<PictureOfDay?>()
    val pictureOfDay: LiveData<PictureOfDay?>
        get() = _pictureOfDay

//    private val _displaySnackbarEvent = MutableLiveData<Boolean>()
//    val displaySnackbarEvent: LiveData<Boolean>
//        get() = _displaySnackbarEvent

    init {
        onViewWeekAsteroidsClicked()
        viewModelScope.launch {
            try {
                asteroidRepository.refreshAsteroids()
                refreshPictureOfDay()
            } catch (e: Exception) {
                println("Exception refreshing data: $e.message")
//                _displaySnackbarEvent.value = true
            }
//            val newpic = PictureOfDay(0,"","","")
//
//            insert(newpic)
        }
    }

    private suspend fun refreshPictureOfDay() {
       _pictureOfDay.value = getPictureOfDay()
    }

    //    private suspend fun insert(pictureOfDay: PictureOfDay) {
//        withContext(Dispatchers.IO) {
//            database.asteroidDao.insertPic(pictureOfDay)
//        }
//    }
    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetailFragment.value = asteroid
    }

    fun doneNavigating() {
        _navigateToDetailFragment.value = null
    }

//    fun doneDisplayingSnackbar() {
//        _displaySnackbarEvent.value = false
//    }

    fun onViewWeekAsteroidsClicked() {
        viewModelScope.launch {
            mDatabase.getAsteroidDao().getAsteroidsByCloseApproachDate(getToday(), getSeventhDay())
                .collect { asteroids ->
                    _asteroids.value = asteroids
                }
        }
    }

    fun onTodayAsteroidsClicked() {
        viewModelScope.launch {
            mDatabase.getAsteroidDao().getAsteroidsByCloseApproachDate(getToday(), getToday())
                .collect { asteroids ->
                    _asteroids.value = asteroids
                }
        }
    }

    fun onSavedAsteroidsClicked() {
        viewModelScope.launch {
            mDatabase.getAsteroidDao().getAllAsteroids().collect { asteroids ->
                _asteroids.value = asteroids
            }
        }
    }

/*
    private suspend fun getPictureOfDay(): PictureOfDay? {
        var  oldPic: PictureOfDay? = null
        withContext(Dispatchers.IO){

        }


        withContext(Dispatchers.IO) {
    //      val oldPic =databasePic.pictureDayDatabase().get()
            myoldPic = databasePic.pictureDayDatabase().get()
//            val old =  getDatabase().pictureDayDatabase().get()
            try {
                val picFromApi= Network.service.getPictureOfDayAsync() // get the Picture from api
                if (picFromApi.mediaType == IMAGE_MEDIA_TYPE){
                    databasePic.pictureDayDatabase().insertPic(picFromApi)
                }else{
                    myoldPic
                    return@withContext

                }

            }catch (e: Exception){
                myoldPic
               // databasePic.pictureDayDatabase().get()
            }
        }
        return myoldPic
    }*/

    private suspend fun getPictureOfDay(): PictureOfDay? {
        var  localPic: PictureOfDay?

        withContext(Dispatchers.IO){
            localPic = INSTANCE?.getPictureDayDao()?.get()
            Log.e(TAG, "Old Local Image:: $localPic: ", )
        }

        return try {
            withContext(Dispatchers.IO){
                val remotePic = Network.service.getPictureOfDayAsync()
                Log.e(TAG, "getPictureOfDay: $remotePic")

                if (remotePic.mediaType == IMAGE_MEDIA_TYPE){
                    mDatabase.getPictureDayDao().insertPic(remotePic)
                    Log.e(TAG, "New Local Image Inserted:: $localPic: ", )
                    return@withContext
                }
            }
            localPic
        } catch (e: Exception){
            Log.e(TAG, "getPictureOfDay: ${e.message ?: e.toString()}", )
            localPic
        }

    }

}