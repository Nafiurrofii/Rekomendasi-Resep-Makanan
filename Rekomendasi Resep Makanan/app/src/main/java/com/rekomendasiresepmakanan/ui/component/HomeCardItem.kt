package com.rekomendasiresepmakanan.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rekomendasiresepmakanan.domain.model.HomeItem

@Composable
fun HomeCardItem(
    item: HomeItem,
    onClick: (HomeItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(item) }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.image),
                contentDescription = null,
                modifier = Modifier.size(60.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(item.title, style = MaterialTheme.typography.titleMedium)
                Text(item.description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
