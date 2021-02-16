package com.yogeshlokhande.weathercheck.data.api

import com.yogeshlokhande.weathercheck.data.model.forecast.ForecastResponse
import com.yogeshlokhande.weathercheck.data.model.todaysforecast.TodaysForecast
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCalls {

    @GET("data/2.5/weather?units=metric")
    fun fetchTodaysForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") appId: String
    ): Call<TodaysForecast>

    @GET("data/2.5/forecast?units=metric")
    fun fetchForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") appId: String
    ): Call<ForecastResponse>
}