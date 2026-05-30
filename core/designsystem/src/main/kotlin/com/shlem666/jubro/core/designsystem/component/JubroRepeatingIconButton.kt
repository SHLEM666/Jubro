/*
 * Thanks to Google AI for some part of this code.
 * Prompt:
 * android compose IconButton repeat action many times on long press
 */

package com.shlem666.jubro.core.designsystem.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.delay

@Composable
fun JubroRepeatingIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: ImageVector,
    description: String? = null,
    delayToRepeat: Long = 300L,
    repeatInterval: Long = 50L,
) {
    // This temporary variable needs to simulate a regular behavior:
    // The action must be performed after the button is released
    var tempOnClick = onClick

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(isPressed) {
        if (isPressed) {

            // Clears after the delayToRepeat time has expired
            delay(delayToRepeat)
            tempOnClick = { }

            // Repeats while the button is pressed
            while (true) {
                onClick()
                delay(repeatInterval)
            }
        }
    }

    JubroIconButton(
        modifier = modifier,
        onClick = { tempOnClick() },
        icon = icon,
        description = description,
        interactionSource = interactionSource,
    )
}