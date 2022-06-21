package com.fight.mysunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherApplication :Application(){
    companion object{
        const val Token = "iYElJD2VOG47MYy5"
        @SuppressLint("StaticFiledLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}