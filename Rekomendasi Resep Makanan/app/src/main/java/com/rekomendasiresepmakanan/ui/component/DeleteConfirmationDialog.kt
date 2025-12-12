package com.rekomendasiresepmakanan.ui.component

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Hapus Resep?") },
        text = { Text("Yakin ingin menghapus resep ini? Aksi ini tidak bisa dibatalkan.") },
        confirmButton = {
            Button(
                onClick = onConfirm, 
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Ya, Hapus")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
