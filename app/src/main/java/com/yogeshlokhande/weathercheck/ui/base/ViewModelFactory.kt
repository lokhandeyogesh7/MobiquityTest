package com.yogeshlokhande.weathercheck.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yogeshlokhande.weathercheck.data.repository.BaseRepository
import com.yogeshlokhande.weathercheck.data.repository.CityRepository
import com.yogeshlokhande.weathercheck.data.repository.DetailsRepository
import com.yogeshlokhande.weathercheck.data.repository.WebViewRepository
import com.yogeshlokhande.weathercheck.ui.main.viewmodel.CityViewModel
import com.yogeshlokhande.weathercheck.ui.main.viewmodel.DetailsViewModel
import com.yogeshlokhande.weathercheck.ui.main.viewmodel.WebViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: BaseRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DetailsViewModel::class.java) -> DetailsViewModel(repository as DetailsRepository) as T
            modelClass.isAssignableFrom(WebViewModel::class.java) -> WebViewModel(repository as WebViewRepository) as T
            modelClass.isAssignableFrom(CityViewModel::class.java) -> CityViewModel(repository as CityRepository) as T
            else -> throw IllegalArgumentException("ViewModel class not found")
        }
    }
}