package com.shlem666.jubro.ui

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.shlem666.jubro.core.designsystem.component.JubroIconButton
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.KeyboardArrowDown
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.KeyboardArrowLeft
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.KeyboardArrowRight
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.KeyboardArrowUp
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.ViewSidebar

@Composable
fun RightToolBarLayout(
    toggleSettingDialog: () -> Unit,
    viewModel: JubroViewModel = hiltViewModel(),
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight()
    ) {
        Column {
            JubroIconButton(
                onClick = { viewModel.evalJS("ToggleRightSideBar.js") },
                icon = ViewSidebar
            )
            JubroIconButton(
                onClick = { toggleSettingDialog() },
                icon = Icons.Filled.Menu
            )
        }
        Column {
            JubroIconButton(
                onClick = { viewModel.simulateKeyPress(KEYCODE_DPAD_UP) },
                icon = KeyboardArrowUp
            )
            JubroIconButton(
                onClick = { viewModel.simulateKeyPress(KEYCODE_DPAD_LEFT) },
                icon = KeyboardArrowLeft
            )
            JubroIconButton(
                onClick = { viewModel.simulateKeyPress(KEYCODE_DPAD_RIGHT) },
                icon = KeyboardArrowRight
            )
            JubroIconButton(
                onClick = { viewModel.simulateKeyPress(KEYCODE_DPAD_DOWN) },
                icon = KeyboardArrowDown
            )
        }
    }
}