package com.yogeshlokhande.weathercheck.ui.main.viewmodel

import androidx.lifecycle.LiveData
import com.yogeshlokhande.weathercheck.data.model.LatLong
import com.yogeshlokhande.weathercheck.data.repository.CityRepository
import com.yogeshlokhande.weathercheck.ui.base.BaseViewModel

open class CityViewModel(val repository: CityRepository) : BaseViewModel(repository) {

    private var allCities: LiveData<List<LatLong>>? = null

    init {
        allCities = repository.allCities
    }

    fun insert(latLong: LatLong): LiveData<Long> {
        return repository.insert(latLong)
    }

    fun delete(latLong: LatLong): LiveData<LatLong> {
        return repository.delete(latLong)
    }

    fun getAllCities(): LiveData<List<LatLong>>? {
        return allCities
    }

    fun deleteAll() {
        repository.deletAll()
    }

}