package com.shlem666.jubro.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.window.core.layout.WindowWidthSizeClass
import com.shlem666.jubro.feature.settings.SettingsDialog

@Composable
fun JubroApp() {

    val sizeClass = currentWindowAdaptiveInfo().windowSizeClass.windowWidthSizeClass
    val portrait = sizeClass == WindowWidthSizeClass.COMPACT

    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }
    if (showSettingsDialog) {
        SettingsDialog(
            onDismiss = { showSettingsDialog = false },
        )
    }

    Scaffold(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .imePadding()
            //.displayCutoutPadding()
        ,
        topBar = {
            if (portrait) {
                TopToolBarLayout(
                    toggleSettingDialog = { showSettingsDialog = true }
                )
            }
        },
        bottomBar = {
            if (portrait) { BottomToolBarLayout() }
        }
    ) { innerPadding ->
        Row (
            Modifier.padding(innerPadding)
        ) {
            if (!portrait) { LeftToolBarLayout() }
            Box( modifier = Modifier.weight(1f) ) { JubroWebView() }
            if (!portrait) {
                RightToolBarLayout(
                    toggleSettingDialog = { showSettingsDialog = true }
                )
            }
        }
    }
}