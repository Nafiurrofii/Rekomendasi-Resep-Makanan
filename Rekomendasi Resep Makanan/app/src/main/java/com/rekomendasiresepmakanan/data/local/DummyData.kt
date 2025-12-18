package com.rekomendasiresepmakanan.data.local

import com.rekomendasiresepmakanan.R
import com.rekomendasiresepmakanan.domain.model.HomeItem

object DummyData {

    val homeItems = listOf(
        HomeItem(
            id = 1,
            title = "Menu 1",
            description = "Deskripsi menu 1",
            image = R.drawable.ic_launcher_foreground
        ),
        HomeItem(
            id = 2,
            title = "Menu 2",
            description = "Deskripsi menu 2",
            image = R.drawable.ic_launcher_foreground
        ),
        HomeItem(
            id = 3,
            title = "Menu 3",
            description = "Deskripsi menu 3",
            image = R.drawable.ic_launcher_foreground
        )
    )
}
