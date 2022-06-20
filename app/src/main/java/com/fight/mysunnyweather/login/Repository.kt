package com.fight.mysunnyweather.login


import androidx.lifecycle.liveData
import com.fight.mysunnyweather.login.dao.PlaceDao
import com.fight.mysunnyweather.login.model.Place
import com.fight.mysunnyweather.login.model.Weather
import com.fight.mysunnyweather.login.network.SunnyWeatherNetWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import java.lang.RuntimeException

/**仓库层*/
object Repository {
    fun searchPlaces(query: String) = liveData(Dispatchers.IO){
        val result = try {
            val placeResponse = SunnyWeatherNetWork.searchPlaces(query)
            if(placeResponse.status == "ok"){
                val places = placeResponse.places
                Result.success(places)
            }else{
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        }catch (e:Exception){
            Result.failure<List<Place>>(e)
            
        }
        emit(result)
    }
    fun refreshWeather(lng: String,lat:String) = liveData(Dispatchers.IO) {
        val result = try {
            coroutineScope {
                val deferredRealtime = async {
                    SunnyWeatherNetWork.SearchRealtimeWeather(lng,lat)
                }
                val deferredDaily = async {
                    SunnyWeatherNetWork.SearchDailyWeather(lng,lat)
                }
                val realtimeResponse = deferredRealtime.await()
                val dailyResponse = deferredDaily.await()
                if(realtimeResponse.status == "ok" || dailyResponse.status == "ok"){
                    val weather = Weather(realtimeResponse.result.realtime,dailyResponse.result.daily)
                    Result.success(weather)
                }else{
                    Result.failure(
                            RuntimeException(
                                    "realtime response status is ${realtimeResponse.status}" +
                                            "daily response status is ${dailyResponse.status}"
                            )
                    )
                }
            }
        }catch (e:Exception){
            Result.failure<Weather>(e)
        }
        emit(result)
    }
    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getPlace() = PlaceDao.getPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()
}