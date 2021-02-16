package com.yogeshlokhande.weathercheck.data.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yogeshlokhande.weathercheck.data.model.LatLong
import com.yogeshlokhande.weathercheck.data.room.CityDao
import com.yogeshlokhande.weathercheck.data.room.CityDatabase
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.concurrent.Executors

class CityRepository() : BaseRepository() {

    lateinit var cityDao: CityDao
    var allCities: LiveData<List<LatLong>>? = null

    constructor(application: Application) : this() {
        val database = CityDatabase.getInstance(application)
        cityDao = database.cityDao()
        allCities = cityDao.getAllCities()
    }

    fun insert(latLong: LatLong): LiveData<Long> {
        val insertId = MutableLiveData<Long>()
        CoroutineScope(IO).launch {
            val insertId1 = cityDao.insert(latLong)
            insertId.postValue(insertId1)
        }
        return insertId
    }

    fun delete(latLong: LatLong): LiveData<LatLong> {
        val deleteId = MutableLiveData<LatLong>()
        CoroutineScope(IO).launch {
            CoroutineScope(IO).launch {
                if (cityDao.delete(latLong) > 0)
                    deleteId.postValue(latLong)
            }
        }
        return deleteId
    }

    fun deletAll() {
        CoroutineScope(IO).launch {
            cityDao.deleteAll()
        }
    }

}