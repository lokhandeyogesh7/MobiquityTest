package com.yogeshlokhande.weathercheck.data.model.todaysforecast

data class Weather(
    var id: Int,
    var main: String,
    var description: String,
    var icon: String? = null
)