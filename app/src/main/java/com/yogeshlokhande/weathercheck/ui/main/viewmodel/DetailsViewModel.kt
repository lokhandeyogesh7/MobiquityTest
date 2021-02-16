package com.yogeshlokhande.weathercheck.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.yogeshlokhande.weathercheck.data.model.forecast.ForecastResponse
import com.yogeshlokhande.weathercheck.data.model.todaysforecast.TodaysForecast
import com.yogeshlokhande.weathercheck.data.repository.DetailsRepository
import com.yogeshlokhande.weathercheck.ui.base.BaseViewModel

class DetailsViewModel(private val repository: DetailsRepository) : BaseViewModel(repository) {

    private var todaysResponse: MutableLiveData<TodaysForecast> = MutableLiveData()
    private var forecastResponse: MutableLiveData<ForecastResponse> = MutableLiveData()

    fun fetchTodaysForecast(
        latitude: Double,
        longitude: Double,
        appid: String
    ): MutableLiveData<TodaysForecast> {
        todaysResponse = repository.fetchTodaysForecast(latitude, longitude,appid)
        return todaysResponse
    }

    fun fetchForecast(latitude: Double, longitude: Double, appid: String): MutableLiveData<ForecastResponse> {
        forecastResponse = repository.fetchForecast(latitude, longitude,appid)
        return forecastResponse
    }
}