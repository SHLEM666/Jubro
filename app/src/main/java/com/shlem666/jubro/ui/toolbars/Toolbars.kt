package com.shlem666.jubro.ui.toolbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopToolBarLayout(
    toggleSettingDialog: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(TopAppBarDefaults.windowInsets)
    ) {
        Row { LeftTop() }
        Row { RightTop(toggleSettingDialog) }
    }
}

@Composable
fun BottomToolBarLayout() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
    ) {
        Row { LeftBottom() }
        Row { RightBottom() }
    }
}

@Composable
fun LeftToolBarLayout() {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight()
    ) {
        Column { LeftTop() }
        Column { LeftBottom(reverse = true) }
    }
}

@Composable
fun RightToolBarLayout(
    toggleSettingDialog: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight(),
    ) {
        Column {
            RightTop(
                toggleSettingDialog = toggleSettingDialog,
                reverse = true
            )
        }
        Column { RightBottom() }
    }
}