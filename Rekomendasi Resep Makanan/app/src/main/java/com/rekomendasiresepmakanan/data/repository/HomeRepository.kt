package com.rekomendasiresepmakanan.data.repository

import com.rekomendasiresepmakanan.data.local.DummyData
import com.rekomendasiresepmakanan.domain.model.HomeItem

class HomeRepository {

    fun getHomeItems(): List<HomeItem> {
        return DummyData.homeItems
    }
}

