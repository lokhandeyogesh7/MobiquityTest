package com.yogeshlokhande.weathercheck.data.model.forecast

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class List1 {
    @SerializedName("dt")
    @Expose
    var dt: Double? = null

    @SerializedName("main")
    @Expose
    var main: Main? = null

    @SerializedName("weather")
    @Expose
    var weather: List<Weather>? = null

    @SerializedName("clouds")
    @Expose
    var clouds: Clouds? = null

    @SerializedName("wind")
    @Expose
    var wind: Wind? = null

    @SerializedName("visibility")
    @Expose
    var visibility: Double? = null

    @SerializedName("pop")
    @Expose
    var pop: Double? = null

    @SerializedName("sys")
    @Expose
    var sys: Sys? = null

    @SerializedName("dt_txt")
    @Expose
    var dtTxt: String? = null

    @SerializedName("rain")
    @Expose
    var rain: Rain? = null
}