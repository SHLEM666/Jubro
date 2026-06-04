/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shlem666.jubro.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.shlem666.jubro.feature.settings.SettingsUiState.Loading
import com.shlem666.jubro.feature.settings.SettingsUiState.Success
import com.shlem666.jubro.feature.settings.items.HintTextSwitchItem
import com.shlem666.jubro.feature.settings.items.JupyterURLItem
import com.shlem666.jubro.feature.settings.items.OrientationControlItem
import com.shlem666.jubro.feature.settings.items.SwitchItem

@Composable
fun SettingsDialog(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
) {
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()
    var appSettings by rememberSaveable(stateSaver = AppSettings.Saver) {
        mutableStateOf((settingsUiState as Success).appSettings)
    }

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { onDismiss() },
        title = { Text( stringResource(R.string.feature_settings_title) ) },
        text = {
            when (settingsUiState) {
                is Loading -> {
                    Text( stringResource(R.string.loading) )
                }
                is Success -> {
                    Items(
                        tempSettings = appSettings,
                        updateSettings = { tempSettings ->
                            appSettings = tempSettings
                        },
                    )
                }
            }
        },
        modifier = modifier.fillMaxSize(),
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.applySettings(appSettings)
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

@Composable
fun Items(
    tempSettings: AppSettings,
    updateSettings: (AppSettings) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll( state = rememberScrollState() ),
    ) {
        OrientationControlItem(
            screenOrient = tempSettings.screenOrient,
            onScreenOrientChange = {
                updateSettings( tempSettings.copy(screenOrient = it) )
            }
        )
        JupyterURLItem(
            value = tempSettings.jupyterUrl,
            onValueChange = {
                updateSettings( tempSettings.copy(jupyterUrl = it) )
            }
        )
        SwitchItem(
            text = stringResource(R.string.use_dark_theme),
            isChecked = tempSettings.darkTheme,
            onToggle = {
                updateSettings( tempSettings.copy(
                    darkTheme = !tempSettings.darkTheme
                ) )
            },
        )
        SwitchItem(
            text = stringResource(R.string.notch_padding),
            isChecked = tempSettings.notchPadding,
            onToggle = {
                updateSettings( tempSettings.copy(
                    notchPadding = !tempSettings.notchPadding
                ) )
            },
        )
        SwitchItem(
            text = stringResource(R.string.hide_status_bar_in_landscape),
            isChecked = tempSettings.hideStatusBar,
            onToggle = {
                updateSettings( tempSettings.copy(
                    hideStatusBar = !tempSettings.hideStatusBar
                ) )
            },
        )
        HintTextSwitchItem(
            primaryText = stringResource(R.string.use_js_api),
            secondaryText = stringResource(R.string.know_more),
            isChecked = tempSettings.useJsApi,
            onToggle = {
                updateSettings( tempSettings.copy(
                    useJsApi = !tempSettings.useJsApi
                ) )
            }
        )
    }
}
