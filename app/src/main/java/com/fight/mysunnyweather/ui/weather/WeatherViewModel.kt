package com.fight.mysunnyweather.ui.weather


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fight.mysunnyweather.login.Repository
import com.fight.mysunnyweather.login.model.Location

class WeatherViewModel:ViewModel() {
    private val locationLiveData = MutableLiveData<Location>()

    var Locationlng = ""

    var Locationlat = ""

    var PlaceName = ""

    val weatherLiveData = Transformations.switchMap(locationLiveData){
        location->
        Repository.refreshWeather(location.lng,location.lat)
    }
    fun refreshWeather(lng:String,lat:String){
        locationLiveData.value = Location(lng,lat)
    }


}