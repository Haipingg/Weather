package com.fight.mysunnyweather.ui.weather

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.fight.mysunnyweather.R
import com.fight.mysunnyweather.SunnyWeatherApplication.Companion.context
import com.fight.mysunnyweather.login.model.Weather
import com.fight.mysunnyweather.login.model.getSky
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.life_index.*
import kotlinx.android.synthetic.main.now.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.timer

class WeatherActivity:AppCompatActivity() {

    val viewModel by lazy { ViewModelProviders.of(this).get(WeatherViewModel::class.java) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        navBtn.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        drawerLayout.addDrawerListener(object :DrawerLayout.DrawerListener{

            override fun onDrawerStateChanged(newState: Int) {

            }
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }
            override fun onDrawerOpened(drawerView: View) {

            }
            override fun onDrawerClosed(drawerView: View) {
               val manager = getSystemService(Context.INPUT_METHOD_SERVICE)
                as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
            }
        })
        if(viewModel.Locationlng.isEmpty()){
            viewModel.Locationlng = intent.getStringExtra("location_lng")?: ""
        }
        if(viewModel.Locationlat.isEmpty()){
            viewModel.Locationlat = intent.getStringExtra("location_lat")?: ""
        }
        if(viewModel.PlaceName.isEmpty()){
            viewModel.PlaceName = intent.getStringExtra("placeName")?: ""
        }
        viewModel.weatherLiveData.observe(this, Observer { result->
            val weather = result.getOrNull()
            if(weather != null){
                showWatherInfo(weather)
        }else{
                Toast.makeText(this,"无法成功获取天气信息！",Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
                swipeRefresh.isRefreshing = false
        })
        swipeRefresh.setColorSchemeResources(R.color.black)
        /**刷新事件*/
        refreshWeather()
        swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }
        viewModel.refreshWeather(viewModel.Locationlng,viewModel.Locationlat)
    }
    fun refreshWeather(){
        viewModel.refreshWeather(viewModel.Locationlng,viewModel.Locationlat)   //刷新天气数据
        swipeRefresh.isRefreshing = true
    }
    private fun showWatherInfo(weather: Weather){
        placeName.text = viewModel.PlaceName
        val realtime = weather.realtime
        val daily = weather.daily
        /**填充now.xml布局*/
        val currentTempText = "${realtime.temperature.toInt()} ℃"
        currentTemp.text = currentTempText
        currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数${realtime.airQuality.aqi.chn.toInt()}"
        currentAQI.text = currentPM25Text
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        /**填充forecast.xml布局*/
        forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for(i in 0 until days){
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false)
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val temperatureText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = temperatureText
            forecastLayout.addView(view)
        }
        /**填充life_index.xml布局*/
        val lifeIndex = daily.lifeIndex
        coldRiskText.text = lifeIndex.coldRisk[0].desc
        dressingText.text = lifeIndex.dressing[0].desc
        ultravioletText.text = lifeIndex.ultraviolet[0].desc
        carWashingText.text = lifeIndex.carWashing[0].desc
        weatherLayout.visibility = View.VISIBLE     //设置控件可见
    }
}
