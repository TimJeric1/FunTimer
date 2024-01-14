package com.tjcoding.funtimer.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> CustomItemsVerticalGrid(
    modifier: Modifier = Modifier,
    items: List<T>,
    uiItem: @Composable (T) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        contentPadding = PaddingValues(12.dp),
        columns = GridCells.Fixed(2),
        content = {
            items(items) { item ->
                uiItem(item)
            }
        },

    )
}
