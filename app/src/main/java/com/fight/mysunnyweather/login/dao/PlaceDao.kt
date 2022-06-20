package com.fight.mysunnyweather.login.dao

import android.content.Context
import androidx.core.content.edit
import com.fight.mysunnyweather.SunnyWeatherApplication
import com.fight.mysunnyweather.login.model.Place
import com.google.gson.Gson

object PlaceDao {
    fun savePlace(place : Place){
        SharedPreferences().edit{
            putString("place", Gson().toJson(place))
        }
    }
    fun getPlace(): Place {
        val placeJson = SharedPreferences().getString("place","")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun isPlaceSaved() = SharedPreferences().contains("place")

    private fun SharedPreferences() = SunnyWeatherApplication.context.
    getSharedPreferences("sunny_weather", Context.MODE_PRIVATE)
}