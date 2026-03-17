package com.shlem666.jubro.ui.toolbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import com.shlem666.jubro.core.designsystem.component.JubroIconButton
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.Refresh
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.ViewSidebar
import com.shlem666.jubro.ui.webview.WebViewController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopToolBarLayout(
    toggleSettingDialog: () -> Unit,
    webViewController: WebViewController
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(TopAppBarDefaults.windowInsets)
    ) {
        Row {
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
        Row {
            JubroIconButton(
                onClick = { toggleSettingDialog() },
                icon = Icons.Filled.Menu
            )
            JubroIconButton(
                onClick = { webViewController.evalJS("ToggleRightSideBar.js") },
                icon = ViewSidebar
            )
        }
    }
}