package com.udacity.asteroidradar.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.utils.Constants
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query



//retrofit object
val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build()))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

//API access methods
interface ApiService {
    @GET("neo/rest/v1/feed")
    fun getAsteroidAsync(
        @Query("api_key")
            apiKey:String = Constants.API_KEY,
        @Query("start_date")
            startDate:String = getFormattedDate(),
        @Query("end_date")
            endDate:String = getFormattedDate(Constants.DEFAULT_END_DATE_DAYS)
    ): Deferred<String>

    @GET("planetary/apod")
    fun getPicOfDayAsync(
        @Query("api_key")
            apiKey:String = Constants.API_KEY,

        ): Deferred<PictureOfDay>
}

//object to access methods outside
object ApiObj {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }


}