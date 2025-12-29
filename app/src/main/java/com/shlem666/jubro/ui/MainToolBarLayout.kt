package com.shlem666.jubro.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MainToolBarLayout(
    viewModel: TabLayoutViewModel = hiltViewModel(),
) {
    Row {
        IconButton(
            onClick = { viewModel.newTab() },
        ) {
            Icon(
                imageVector = Icons.Filled.AddCircleOutline,
                contentDescription = null,
            )
        }
        IconButton(
            onClick = { viewModel.reloadCurrentPage() },
        ) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = null,
            )
        }
    }
}