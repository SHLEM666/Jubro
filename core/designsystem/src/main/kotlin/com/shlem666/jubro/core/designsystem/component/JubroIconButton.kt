package com.shlem666.jubro.core.designsystem.component

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun JubroIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
    description: String? = null,
) {
    IconButton(
        modifier = modifier,
        onClick = { onClick() },
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
        )
    }
}