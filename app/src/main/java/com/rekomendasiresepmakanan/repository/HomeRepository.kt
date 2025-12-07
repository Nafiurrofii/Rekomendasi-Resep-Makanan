package com.rekomendasiresepmakanan.repository

import com.rekomendasiresepmakanan.data.local.DummyData
import com.rekomendasiresepmakanan.data.local.model.HomeItem

class HomeRepository {

    fun getHomeItems(): List<HomeItem> {
        return DummyData.homeItems
    }
}
