package com.yogeshlokhande.weathercheck.ui.base

import androidx.lifecycle.ViewModel
import com.yogeshlokhande.weathercheck.data.repository.BaseRepository

abstract class BaseViewModel(private val repository: BaseRepository) : ViewModel() {
}