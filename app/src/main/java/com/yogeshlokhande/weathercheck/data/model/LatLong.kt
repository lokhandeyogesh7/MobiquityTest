package com.yogeshlokhande.weathercheck.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "latLong_table")
data class LatLong(
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val locality :String,
    @PrimaryKey(autoGenerate = true) var id: Int=0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(address)
        parcel.writeString(locality)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LatLong> {
        override fun createFromParcel(parcel: Parcel): LatLong {
            return LatLong(parcel)
        }

        override fun newArray(size: Int): Array<LatLong?> {
            return arrayOfNulls(size)
        }
    }
}