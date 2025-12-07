package com.rekomendasiresepmakanan.ui.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
//import androidx.lifecycle.mutableStateOf
import com.rekomendasiresepmakanan.data.local.model.HomeItem
import com.rekomendasiresepmakanan.repository.HomeRepository


class HomeViewModel : ViewModel() {

    private val repository = HomeRepository()

    val homeItems = mutableStateOf<List<HomeItem>>(emptyList())

    init {
        loadItems()
    }

    private fun loadItems() {
        homeItems.value = repository.getHomeItems()
    }

}
