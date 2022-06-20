package com.fight.mysunnyweather.login.network

import android.telecom.Call
import com.fight.mysunnyweather.SunnyWeatherApplication
import com.fight.mysunnyweather.login.model.DailyResponse
import com.fight.mysunnyweather.login.model.RealtimeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    @GET("v2.5/${SunnyWeatherApplication.Token}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng") lng:String,@Path("lat") lat:String):
            retrofit2.Call<RealtimeResponse>

    @GET("v2.5/${SunnyWeatherApplication.Token}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng:String,@Path("lat") lat:String):
            retrofit2.Call<DailyResponse>
}