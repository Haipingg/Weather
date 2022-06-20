package com.fight.mysunnyweather.login.network

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import retrofit2.http.Query
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object SunnyWeatherNetWork {
    /**天气数据的封装 Start*/
    private val WeatherService = ServiceCreator.create(WeatherService::class.java)

    suspend fun SearchRealtimeWeather(lng: String,lat:String) = WeatherService.getRealtimeWeather(lng,lat).await()

    suspend fun SearchDailyWeather(lng: String,lat:String) = WeatherService.getDailyWeather(lng,lat).await()

    /**天气数据的封装 end*/

    private val placeService = ServiceCreator.create(PlaceService::class.java)

    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    @ExperimentalCoroutinesApi
    private suspend fun <T> Call<T>.await(): T{
        return suspendCancellableCoroutine { continuation ->
            enqueue(object :Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if(body != null) continuation.resume(body)
                    else{
                        continuation.resumeWithException(RuntimeException("response body is null"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)

                }
            })
        }
    }
}