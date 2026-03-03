package com.shlem666.jubro.ui

import android.app.Activity
import android.content.pm.ActivityInfo
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
import com.shlem666.jubro.feature.settings.SettingsDialog
import com.shlem666.jubro.ui.toolbars.BottomToolBarLayout
import com.shlem666.jubro.ui.toolbars.LeftToolBarLayout
import com.shlem666.jubro.ui.toolbars.RightToolBarLayout
import com.shlem666.jubro.ui.toolbars.TopToolBarLayout
import com.shlem666.jubro.ui.UiState.Success

@Composable
fun JubroApp(
    viewModel: JubroViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isCompact = currentWindowAdaptiveInfo().windowSizeClass
        .windowWidthSizeClass == WindowWidthSizeClass.COMPACT
    var settings by rememberSaveable(stateSaver = DataStoreResources.Saver) {
        mutableStateOf(
            DataStoreResources(
                jupyterUrl = "",
                notchPadding = false,
                hideStatusBar = false,
                screenOrient = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED,
            )
        )
    }

    if (uiState is Success) {
        settings = (uiState as Success).resources
        if (!isCompact && settings.hideStatusBar) {
            HideStatusBar(true)
        } else {
            HideStatusBar(false)
        }
        LockScreenOrientation(settings.screenOrient)
    }

    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }
    if (showSettingsDialog) {
        SettingsDialog( onDismiss = { showSettingsDialog = false } )
    }

    Scaffold(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .imePadding()
            .then(if (settings.notchPadding) {
                Modifier.displayCutoutPadding()
            } else { Modifier } )
        ,
        topBar = {
            if (isCompact) { TopToolBarLayout(
                toggleSettingDialog = { showSettingsDialog = true }
            ) }
        },
        bottomBar = { if (isCompact) { BottomToolBarLayout() } }
    ) { innerPadding ->
        Row (
            Modifier.padding(innerPadding)
        ) {
            if (!isCompact) { LeftToolBarLayout() }
            Box( modifier = Modifier.weight(1f) ) {
                JubroWebView(settings.jupyterUrl)
            }
            if (!isCompact) { RightToolBarLayout(
                toggleSettingDialog = { showSettingsDialog = true }
            ) }
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