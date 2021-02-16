package com.yogeshlokhande.weathercheck.data.model.forecast

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ForecastResponse {
    @SerializedName("cod")
    @Expose
    var cod: String? = null

    @SerializedName("message")
    @Expose
    var message: Double? = null

    @SerializedName("cnt")
    @Expose
    var cnt: Double? = null

    @SerializedName("list")
    @Expose
    var list: List<List1>? = null

    @SerializedName("city")
    @Expose
    var city: City? = null
}