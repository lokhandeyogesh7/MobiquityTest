package com.yogeshlokhande.weathercheck.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yogeshlokhande.weathercheck.data.model.LatLong

@Dao
interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(latLong: LatLong): Long

    @Query("SELECT * FROM latLong_table")
    fun getAllCities(): LiveData<List<LatLong>>

    @Delete
    fun delete(latLong: LatLong): Int

    @Query("DELETE FROM latLong_table")
    fun deleteAll()
}