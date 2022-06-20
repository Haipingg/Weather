package com.fight.mysunnyweather.login.network

import com.fight.mysunnyweather.SunnyWeatherApplication
import com.fight.mysunnyweather.login.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {
    @GET("v2/place?token=${SunnyWeatherApplication.Token}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String):Call<PlaceResponse>
}