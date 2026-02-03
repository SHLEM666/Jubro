package com.shlem666.jubro.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.unit.dp
import com.shlem666.jubro.feature.settings.SettingsDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JubroApp() {
    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }

    if (showSettingsDialog) {
        SettingsDialog(
            onDismiss = { showSettingsDialog = false },
        )
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                modifier = Modifier.height(80.dp),
                title = { },
                actions = { TopToolBarLayout(
                    toggleSettingDialog = { showSettingsDialog = true }
                ) },
            )
        },
        bottomBar = { BottomToolBarLayout() }

    ) { innerPadding ->
        Box(
            Modifier.padding(innerPadding)
        ) {
            JubroWebView()
        }
    }
}