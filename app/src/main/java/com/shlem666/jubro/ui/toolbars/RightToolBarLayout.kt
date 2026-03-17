package com.shlem666.jubro.ui.toolbars

import android.view.KeyEvent.KEYCODE_DPAD_DOWN
import android.view.KeyEvent.KEYCODE_DPAD_LEFT
import android.view.KeyEvent.KEYCODE_DPAD_RIGHT
import android.view.KeyEvent.KEYCODE_DPAD_UP
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.shlem666.jubro.core.designsystem.component.JubroIconButton
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.KeyboardArrowDown
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.KeyboardArrowLeft
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.KeyboardArrowRight
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.KeyboardArrowUp
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.ViewSidebar
import com.shlem666.jubro.ui.webview.WebViewController

@Composable
fun RightToolBarLayout(
    toggleSettingDialog: () -> Unit,
    webViewController: WebViewController
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight()
    ) {
        Column {
            JubroIconButton(
                onClick = { webViewController.evalJS("ToggleRightSideBar.js") },
                icon = ViewSidebar
            )
            JubroIconButton(
                onClick = { toggleSettingDialog() },
                icon = Icons.Filled.Menu
            )
        }
        Column {
            JubroIconButton(
                onClick = { webViewController.simulateKeyPress(KEYCODE_DPAD_UP) },
                icon = KeyboardArrowUp
            )
            JubroIconButton(
                onClick = { webViewController.simulateKeyPress(KEYCODE_DPAD_LEFT) },
                icon = KeyboardArrowLeft
            )
            JubroIconButton(
                onClick = { webViewController.simulateKeyPress(KEYCODE_DPAD_RIGHT) },
                icon = KeyboardArrowRight
            )
            JubroIconButton(
                onClick = { webViewController.simulateKeyPress(KEYCODE_DPAD_DOWN) },
                icon = KeyboardArrowDown
            )
        }
    }
}