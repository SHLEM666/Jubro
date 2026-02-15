package com.shlem666.jubro.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun JubroOrientButton(
    modifier: Modifier = Modifier,
    activated: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
) {
    JubroIconButton(
        modifier = modifier
            .clip(CircleShape)
            .then(
                if (activated) {
                    Modifier.background(
                        MaterialTheme.colorScheme.inversePrimary
                    )
                } else { Modifier }
            )
        ,
        onClick = { onClick() },
        icon = icon
    )
}