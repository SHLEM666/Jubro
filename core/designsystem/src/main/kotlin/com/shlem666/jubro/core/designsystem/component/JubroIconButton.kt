package com.shlem666.jubro.core.designsystem.component

import androidx.compose.foundation.interaction.MutableInteractionSource
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
    interactionSource: MutableInteractionSource? = null,
) {
    IconButton(
        modifier = modifier,
        onClick = { onClick() },
        interactionSource = interactionSource,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
        )
    }
}