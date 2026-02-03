package com.shlem666.jubro.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.hilt.navigation.compose.hiltViewModel
import com.shlem666.jubro.core.designsystem.component.JubroIconButton
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.Refresh
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.ViewSidebar

@Composable
fun TopToolBarLayout(
    toggleSettingDialog: () -> Unit,
    viewModel: JubroViewModel = hiltViewModel(),
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row {
            JubroIconButton(
                modifier = Modifier.scale(scaleX = -1f, scaleY = 1f),
                onClick = { viewModel.evalJS("ToggleLeftSideBar.js") },
                icon = ViewSidebar
            )
            JubroIconButton(
                onClick = { viewModel.webView.reload() },
                icon = Refresh
            )
        }
        Row {
            JubroIconButton(
                onClick = { toggleSettingDialog() },
                icon = Icons.Filled.Menu
            )
            JubroIconButton(
                onClick = { viewModel.evalJS("ToggleRightSideBar.js") },
                icon = ViewSidebar
            )
        }
    }
}