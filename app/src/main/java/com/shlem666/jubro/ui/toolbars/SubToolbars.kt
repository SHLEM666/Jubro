package com.shlem666.jubro.ui.toolbars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.hilt.navigation.compose.hiltViewModel
import com.shlem666.jubro.core.designsystem.component.JubroIconButton
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.KeyboardArrowDown
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.KeyboardArrowLeft
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.KeyboardArrowRight
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.KeyboardArrowUp
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.Redo
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.Refresh
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.Undo
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.ViewSidebar
import com.shlem666.jubro.ui.webview.JubroWebViewViewModel

@Composable
fun LeftTop(
    viewModel: JubroWebViewViewModel = hiltViewModel()
) {
    JubroIconButton(
        modifier = Modifier.scale(scaleX = -1f, scaleY = 1f),
        onClick = { viewModel.toggleLeftSideBar() },
        icon = ViewSidebar
    )
    JubroIconButton(
        onClick = { viewModel.reload() },
        icon = Refresh
    )
}

@Composable
fun RightTop(
    toggleSettingDialog: () -> Unit,
    viewModel: JubroWebViewViewModel = hiltViewModel(),
    reverse: Boolean = false
) {
    val menuButton = @Composable {
        JubroIconButton(
            onClick = { toggleSettingDialog() },
            icon = Icons.Filled.Menu
        )
    }
    val sideBarButton = @Composable {
        JubroIconButton(
            onClick = { viewModel.toggleRightSideBar() },
            icon = ViewSidebar
        )
    }
    if (reverse) {
        sideBarButton()
        menuButton()
    } else {
        menuButton()
        sideBarButton()
    }
}

@Composable
fun LeftBottom(
    viewModel: JubroWebViewViewModel = hiltViewModel()
) {
    JubroIconButton(
        onClick = { viewModel.undo() },
        icon = Undo
    )
    JubroIconButton(
        onClick = { viewModel.redo() },
        icon = Redo
    )
}

@Composable
fun RightBottom(
    viewModel: JubroWebViewViewModel = hiltViewModel()
) {
    JubroIconButton(
        onClick = { viewModel.arrowUp() },
        icon = KeyboardArrowUp
    )
    JubroIconButton(
        onClick = { viewModel.arrowLeft() },
        icon = KeyboardArrowLeft
    )
    JubroIconButton(
        onClick = { viewModel.arrowRight() },
        icon = KeyboardArrowRight
    )
    JubroIconButton(
        onClick = { viewModel.arrowDown() },
        icon = KeyboardArrowDown
    )
}