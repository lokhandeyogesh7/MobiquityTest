package com.yogeshlokhande.weathercheck.data.model.todaysforecast

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Sys (
    var country: String,
    var sunrise: Int,
    var sunset: Int
)