package com.yogeshlokhande.weathercheck.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yogeshlokhande.weathercheck.data.model.LatLong

@Database(entities = [LatLong::class], version = 5)
abstract class CityDatabase : RoomDatabase() {

    companion object {
        var instance: CityDatabase? = null

        fun getInstance(context: Context): CityDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    CityDatabase::class.java,
                    "city_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }

    abstract fun cityDao(): CityDao
}