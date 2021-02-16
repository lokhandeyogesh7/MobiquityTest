package com.yogeshlokhande.weathercheck.data.repository

import androidx.lifecycle.MutableLiveData
import com.yogeshlokhande.weathercheck.data.api.RestClient
import com.yogeshlokhande.weathercheck.data.model.forecast.ForecastResponse
import com.yogeshlokhande.weathercheck.data.model.todaysforecast.TodaysForecast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsRepository : BaseRepository() {

    fun fetchTodaysForecast(latitude: Double, longitude: Double,appId:String):MutableLiveData<TodaysForecast>  {
        val apiCalls =RestClient.getInstance().getApiService()
        val todaysForecast: MutableLiveData<TodaysForecast> =
            MutableLiveData<TodaysForecast>()
        val call: Call<TodaysForecast> = apiCalls.fetchTodaysForecast(latitude,longitude,appId)
        call.enqueue(object : Callback<TodaysForecast?> {
          override  fun onResponse(call: Call<TodaysForecast?>?, response: Response<TodaysForecast?>) {
                if (response.body() != null) {
                    todaysForecast.value = response.body()
                }
            }
            override fun onFailure(call: Call<TodaysForecast?>?, t: Throwable?) {}
        })
        return todaysForecast
    }

    fun fetchForecast(latitude: Double, longitude: Double, appid: String): MutableLiveData<ForecastResponse> {
        val apiCalls =RestClient.getInstance().getApiService()
        val forecast: MutableLiveData<ForecastResponse> =
            MutableLiveData<ForecastResponse>()
        val call: Call<ForecastResponse> = apiCalls.fetchForecast(latitude,longitude,appid)
        call.enqueue(object : Callback<ForecastResponse?> {
            override fun onResponse(
                call: Call<ForecastResponse?>?,
                response: Response<ForecastResponse?>
            ) {
                if (response.body() != null) {
                    forecast.value = response.body()
                }
            }

            override fun onFailure(call: Call<ForecastResponse?>?, t: Throwable?) {
            }
        })
        return forecast
    }
}