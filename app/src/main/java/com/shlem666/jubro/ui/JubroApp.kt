package com.shlem666.jubro.ui

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
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
import androidx.compose.runtime.DisposableEffect
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import com.shlem666.jubro.ui.toolbars.BottomToolBarLayout
import com.shlem666.jubro.ui.toolbars.LeftToolBarLayout
import com.shlem666.jubro.ui.toolbars.RightToolBarLayout
import com.shlem666.jubro.ui.toolbars.TopToolBarLayout
import com.shlem666.jubro.feature.settings.SettingsDialog
import com.shlem666.jubro.feature.settings.AppSettings
import com.shlem666.jubro.feature.settings.SettingsUiState.Success
import com.shlem666.jubro.feature.settings.SettingsViewModel
import com.shlem666.jubro.ui.webview.JubroWebView

@Composable
fun JubroApp(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
) {
    val settingsUiState by settingsViewModel.settingsUiState.collectAsStateWithLifecycle()
    val isCompact = currentWindowAdaptiveInfo().windowSizeClass
        .windowWidthSizeClass == WindowWidthSizeClass.COMPACT
    var appSettings by rememberSaveable(stateSaver = AppSettings.Saver) {
        mutableStateOf( AppSettings() )
    }

    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }
    if (showSettingsDialog) {
        SettingsDialog( onDismiss = { showSettingsDialog = false } )
    }

    if (settingsUiState is Success) {
        appSettings = (settingsUiState as Success).appSettings

        HideStatusBar(!isCompact && appSettings.hideStatusBar)
        LockScreenOrientation(appSettings.screenOrient)

        Scaffold(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .imePadding()
                .then(if (appSettings.notchPadding) {
                    Modifier.displayCutoutPadding()
                } else { Modifier } )
            ,
            topBar = { if (isCompact) TopToolBarLayout {
                showSettingsDialog = true
            } },
            bottomBar = { if (isCompact) BottomToolBarLayout() },
        ) { innerPadding ->
            Row ( Modifier.padding(innerPadding) ) {
                if (!isCompact) LeftToolBarLayout()
                Box( Modifier.weight(1f) ) {
                    JubroWebView(appSettings.jupyterUrl)
                }
                if (!isCompact) RightToolBarLayout {
                    showSettingsDialog = true
                }
            }
        }
    }
}

@Composable
fun LockScreenOrientation(orientation: Int) {
    val activity = LocalActivity.current
    DisposableEffect(orientation) {
        val activity = activity ?: return@DisposableEffect onDispose { }
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = originalOrientation
        }
    }
}

@Composable
fun HideStatusBar(hide: Boolean) {
    val activity = LocalActivity.current
    val window = (activity as Activity).window
    val windowInsetsController =
        WindowCompat.getInsetsController(window, window.decorView)
    val statusBars = WindowInsetsCompat.Type.statusBars()
    if (hide) {
        windowInsetsController.hide(statusBars)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat
                .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    } else {
        windowInsetsController.show(statusBars)
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
    }
}