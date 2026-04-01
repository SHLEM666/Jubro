package com.shlem666.jubro.feature.settings.items

import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.shlem666.jubro.core.designsystem.component.JubroIconButton
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.ScreenLockLandscape
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.ScreenLockPortrait
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.ScreenRotation

@Composable
fun OrientationControlItem(
    screenOrient: Int,
    onScreenOrientChange: (Int) -> Unit,
) {
    val triples = listOf(
        Triple(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED, ScreenRotation, Modifier),
        Triple(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, ScreenLockPortrait, Modifier),
        Triple(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, ScreenLockLandscape, Modifier),
        Triple(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE, ScreenLockLandscape,
            Modifier.scale(scaleX = 1f, scaleY = -1f)),
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        triples.forEach { triple ->
            OrientationControlButton(
                modifier = triple.third,
                activated = triple.first == screenOrient,
                onClick = {
                    onScreenOrientChange(triple.first)
                },
                icon = triple.second,
            )
        }
    }
}

@Composable
fun OrientationControlButton(
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