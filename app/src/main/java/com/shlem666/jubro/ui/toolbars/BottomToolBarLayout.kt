package com.shlem666.jubro.ui.toolbars

import android.view.KeyEvent.KEYCODE_DPAD_DOWN
import android.view.KeyEvent.KEYCODE_DPAD_LEFT
import android.view.KeyEvent.KEYCODE_DPAD_RIGHT
import android.view.KeyEvent.KEYCODE_DPAD_UP
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.shlem666.jubro.core.designsystem.component.JubroIconButton
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.KeyboardArrowDown
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.KeyboardArrowLeft
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.KeyboardArrowRight
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.KeyboardArrowUp
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.Redo
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.Undo
import com.shlem666.jubro.util.WebViewController

@Composable
fun BottomToolBarLayout(
    webViewController: WebViewController,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
    ) {
        Row {
            JubroIconButton(
                onClick = { webViewController.evalJS("Undo.js") },
                icon = Undo
            )
            JubroIconButton(
                onClick = { webViewController.evalJS("Redo.js") },
                icon = Redo
            )
        }
        Row {
            JubroIconButton(
                onClick = { webViewController.simulateKeyPress(KEYCODE_DPAD_LEFT) },
                icon = KeyboardArrowLeft
            )
            JubroIconButton(
                onClick = { webViewController.simulateKeyPress(KEYCODE_DPAD_RIGHT) },
                icon = KeyboardArrowRight
            )
            JubroIconButton(
                onClick = { webViewController.simulateKeyPress(KEYCODE_DPAD_UP) },
                icon = KeyboardArrowUp
            )
            JubroIconButton(
                onClick = { webViewController.simulateKeyPress(KEYCODE_DPAD_DOWN) },
                icon = KeyboardArrowDown
            )
        }
    }
}