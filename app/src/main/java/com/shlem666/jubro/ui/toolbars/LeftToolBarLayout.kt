package com.shlem666.jubro.ui.toolbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import com.shlem666.jubro.core.designsystem.component.JubroIconButton
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.Redo
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.Refresh
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.Undo
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.ViewSidebar
import com.shlem666.jubro.util.WebViewController

@Composable
fun LeftToolBarLayout(
    webViewController: WebViewController,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight()
    ) {
        Column {
            JubroIconButton(
                modifier = Modifier.scale(scaleX = -1f, scaleY = 1f),
                onClick = { webViewController.evalJS("ToggleLeftSideBar.js") },
                icon = ViewSidebar
            )
            JubroIconButton(
                onClick = { webViewController.webView.reload() },
                icon = Refresh
            )
        }
        Column {
            JubroIconButton(
                onClick = { webViewController.evalJS("Undo.js") },
                icon = Undo
            )
            JubroIconButton(
                onClick = { webViewController.evalJS("Redo.js") },
                icon = Redo
            )
        }
    }
}