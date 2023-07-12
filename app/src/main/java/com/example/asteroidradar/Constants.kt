package com.example.asteroidradar

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

object Constants {
    const val API_QUERY_DATE_FORMAT = "yyyy-MM-dd" //TODO From YYYY to yyyy
    const val DEFAULT_END_DATE_DAYS = 7
    const val BASE_URL = "https://api.nasa.gov/"
    const val API_KEY = "OEhrGMe8Q5o7DATRan1cc3oJy51RxYy1ljLputGc"
    const val IMAGE_MEDIA_TYPE = "image"
    const val REFRESH_ASTEROIDS_WORK_NAME = "AsteroidRefreshDataWorker"
    const val DELETE_ASTEROIDS_WORK_NAME = "AsteroidDeleteDataWorker"
    const val NOTIFICATION_ID = "id"
    const val NOTIFICATION_NAME = "channel_name"

    const val ONLINE_IMAGE_FOR_TEST = "https://live-production.wcms.abc-cdn.net.au/3e20d94ae45c8fc312bc7c614e5ac106?impolicy=wcms_crop_resize&cropH=396&cropW=699&xPos=0&yPos=42&width=862&height=485"

}
