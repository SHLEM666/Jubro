package com.shlem666.jubro.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shlem666.jubro.core.designsystem.component.JubroSettingsSwitchItem
import com.shlem666.jubro.feature.settings.UiState.Loading
import com.shlem666.jubro.feature.settings.UiState.Success

@Composable
fun SettingsDialog(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var firstAccess by rememberSaveable { mutableStateOf(true) }
    var tempJupyterUrl by rememberSaveable { mutableStateOf("") }
    var tempNotchPadding by rememberSaveable { mutableStateOf(false) }
    var tempHideStatusBarInLandscape by rememberSaveable { mutableStateOf(false) }

    when (uiState) {
        is Loading -> { }
        is Success -> {
            if (firstAccess) {
                firstAccess = false
                tempJupyterUrl = (uiState as Success)
                    .resources.jupyterUrl
                tempNotchPadding = (uiState as Success)
                    .resources.notchPadding
                tempHideStatusBarInLandscape = (uiState as Success)
                    .resources.hideStatusBarInLandscape
            }
        }
    }

    @Composable
    fun settingsItems() {
        Column {
            TextField(
                value = tempJupyterUrl,
                onValueChange = { tempJupyterUrl = it },
                modifier = modifier.fillMaxWidth(),
                label = {
                    Text( stringResource(R.string.jupyter_url) )
                },
                singleLine = true,
            )
            JubroSettingsSwitchItem(
                text = stringResource(R.string.notch_padding),
                isChecked = tempNotchPadding,
                onToggle = { tempNotchPadding = !tempNotchPadding },
            )
            JubroSettingsSwitchItem(
                text = stringResource(R.string.hide_status_bar_in_landscape),
                isChecked = tempHideStatusBarInLandscape,
                onToggle = {
                    tempHideStatusBarInLandscape = !tempHideStatusBarInLandscape
                },
            )
        }
    }

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { /* Do nothing */ },
        title = {
            Text( stringResource( R.string.feature_settings_title ) )
        },
        text = {
            settingsItems()
        },
        modifier = modifier.fillMaxSize(),
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.updateJupyterUrl(tempJupyterUrl)
                    viewModel.updateNotchPadding(tempNotchPadding)
                    viewModel.updateHideStatusBarInLandscape(
                        tempHideStatusBarInLandscape
                    )
                    onDismiss()
                }
            ) {
                Text( stringResource(R.string.btn_save) )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text( stringResource(R.string.btn_cancel) )
            }
        },
    )
}