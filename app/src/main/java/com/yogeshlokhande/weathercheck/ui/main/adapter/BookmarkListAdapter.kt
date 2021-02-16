package com.yogeshlokhande.weathercheck.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yogeshlokhande.weathercheck.data.model.LatLong
import com.yogeshlokhande.weathercheck.databinding.ItemBookmarkBinding

class BookmarkListAdapter(val itemClickListener: ItemClickListener, val cityList: List<LatLong>,val clickListener:DeleteCity) :
    RecyclerView.Adapter<BookmarkListAdapter.BookmarkViewholder>() {

    inner class BookmarkViewholder(var itemBookmarkBinding: ItemBookmarkBinding) :
        RecyclerView.ViewHolder(itemBookmarkBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewholder {
        val itemView =
            ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkViewholder(itemView)
    }

    override fun onBindViewHolder(holder: BookmarkViewholder, position: Int) {
        val cityDetails = cityList[position]
        holder.itemBookmarkBinding.apply {
            tvCityName.text = cityDetails.locality
            tvDetailAddress.text = cityDetails.address
            ivDelete.setOnClickListener {
                clickListener.deleteOnClick(cityDetails)
            }
        }
        holder.itemView.setOnClickListener {
            itemClickListener.onItemClicked(cityDetails)
        }
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    interface DeleteCity{
        fun deleteOnClick(latLong: LatLong)
    }

    interface ItemClickListener{
        fun onItemClicked(latLong: LatLong)
    }
}
