package com.yogeshlokhande.weathercheck.data.model.forecast

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class City {
    @SerializedName("id")
    @Expose
    var id: Double? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("coord")
    @Expose
    var coord: Coord? = null

    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("population")
    @Expose
    var population: Double? = null

    @SerializedName("timezone")
    @Expose
    var timezone: Double? = null

    @SerializedName("sunrise")
    @Expose
    var sunrise: Double? = null

    @SerializedName("sunset")
    @Expose
    var sunset: Double? = null
}