package com.shlem666.jubro.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.hilt.navigation.compose.hiltViewModel
import com.shlem666.jubro.core.designsystem.component.JubroIconButton
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.Redo
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.Refresh
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.Undo
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.ViewSidebar

@Composable
fun LeftToolBarLayout(
    viewModel: JubroViewModel = hiltViewModel(),
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight()
    ) {
        Column {
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
        Column {
            JubroIconButton(
                onClick = { viewModel.evalJS("Undo.js") },
                icon = Undo
            )
            JubroIconButton(
                onClick = { viewModel.evalJS("Redo.js") },
                icon = Redo
            )
        }
    }
}