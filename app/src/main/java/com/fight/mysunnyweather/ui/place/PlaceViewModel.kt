package com.fight.mysunnyweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fight.mysunnyweather.login.Repository
import com.fight.mysunnyweather.login.Repository.searchPlaces
import com.fight.mysunnyweather.login.model.Place
import retrofit2.http.Query

class PlaceViewModel : ViewModel() {
    private val sreachLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    val placeLiveData = Transformations.switchMap(sreachLiveData){
        query->searchPlaces(query)
    }
    fun sreachPlaces(query: String){
        sreachLiveData.value = query
    }
    fun savePlace(place: Place) = Repository.savePlace(place)

    fun getPlace() = Repository.getPlace()

    fun isPlaceSaved() = Repository.isPlaceSaved()

}