//package com.example.asteroidradar.work
//
//import android.content.Context
//import androidx.work.CoroutineWorker
//import androidx.work.WorkerParameters
//import com.example.asteroidradar.database.AsteroidRadarDatabase
//
//import com.example.asteroidradar.main.AsteroidRepository
//import retrofit2.HttpException
//
//class DeleteDataWork(appContext: Context, params: WorkerParameters) :
//
//    CoroutineWorker(appContext, params) {
//    override suspend fun doWork(): Result {
//        val database = AsteroidRadarDatabase.initDatabase(applicationContext)
//        val repository = AsteroidRepository(database)
//
//        return try {
//            repository.deletePreviousDayAsteroids()
//            Result.success()
//        } catch (exception: HttpException) {
//            Result.retry()
//        }
//    }
//}