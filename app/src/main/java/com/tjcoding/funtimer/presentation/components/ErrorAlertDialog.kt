package com.tjcoding.funtimer.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ErrorAlertDialog(
    text: String,
    onDismissClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissClick,
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = onDismissClick
            ) {
                Text(text = "Ok")
            }
        },
        title = {
            Text(
                text = "An Error Occured",
                style = MaterialTheme.typography.headlineMedium
            )
        },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        shape = MaterialTheme.shapes.extraLarge
    )
}