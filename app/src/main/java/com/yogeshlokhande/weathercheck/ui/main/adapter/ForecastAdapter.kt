package com.yogeshlokhande.weathercheck.ui.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yogeshlokhande.weathercheck.data.model.forecast.List1
import com.yogeshlokhande.weathercheck.databinding.ItemForecastRowBinding
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ForecastAdapter(val forecastList: List<List1>?) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewholder>() {

    inner class ForecastViewholder(var itemForecastRowBinding: ItemForecastRowBinding) :
        RecyclerView.ViewHolder(itemForecastRowBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewholder {
        val itemView =
            ItemForecastRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastViewholder(itemView)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: ForecastViewholder, position: Int) {
        val forecastDetails = forecastList?.get(position)
        var date1 = ""
        var time1 = ""
        try {
            val f: DateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
            val d: Date = f.parse(forecastDetails?.dtTxt)
            var date: DateFormat = SimpleDateFormat("dd/MM/yyyy")
            var time: DateFormat = SimpleDateFormat("hh:mm:ss")
            date1 = date.format(d)
            time1 = time.format(d)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        holder.itemForecastRowBinding.apply {
            tvDate.text = date1
            tvTime.text = time1
            tvTemp.text = "${forecastDetails?.main?.temp.toString()}\u2103"
            tvWeather.text = forecastDetails?.weather!![0].main
            tvHumidity.text = "Humidity: ${forecastDetails.main?.humidity} %"
            tvWindSpeed.text = "Wind-Speed: ${forecastDetails.wind?.speed} meter/sec"
            Glide.with(holder.itemView.context)
                .load("http://openweathermap.org/img/w/${forecastDetails.weather?.get(0)?.icon}.png")
                .into(ivWeatherIcon)
        }
    }

    override fun getItemCount(): Int {
        return forecastList?.size!!
    }
}
