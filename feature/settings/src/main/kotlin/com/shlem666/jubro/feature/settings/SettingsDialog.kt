package com.shlem666.jubro.feature.settings

import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shlem666.jubro.core.designsystem.component.JubroIconButton
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.ScreenLockLandscape
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.ScreenLockPortrait
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.ScreenRotation
import com.shlem666.jubro.feature.settings.SettingsUiState.Loading
import com.shlem666.jubro.feature.settings.SettingsUiState.Success

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
                    SettingsItems(
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
fun SettingsItems(
    tempSettings: AppSettings,
    updateSettings: (AppSettings) -> Unit
) {
    Column(
        modifier = Modifier
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
        JubroSettingsSwitchItem(
            text = stringResource(R.string.notch_padding),
            isChecked = tempSettings.notchPadding,
            onToggle = {
                updateSettings( tempSettings.copy(
                    notchPadding = !tempSettings.notchPadding
                ) )
            },
        )
        JubroSettingsSwitchItem(
            text = stringResource(R.string.hide_status_bar_in_landscape),
            isChecked = tempSettings.hideStatusBar,
            onToggle = {
                updateSettings( tempSettings.copy(
                    hideStatusBar = !tempSettings.hideStatusBar
                ) )
            },
        )
        JubroSettingsSwitchItem(
            text = stringResource(R.string.use_js_api),
            isChecked = tempSettings.useJsApi,
            onToggle = {
                updateSettings( tempSettings.copy(
                    useJsApi = !tempSettings.useJsApi
                ) )
            },
        )
    }
}

@Composable
fun OrientationControlItem(
    screenOrient: Int,
    onScreenOrientChange: (Int) -> Unit,
) {
    val triples = listOf(
        Triple(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED, ScreenRotation, Modifier),
        Triple(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, ScreenLockPortrait, Modifier),
        Triple(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, ScreenLockLandscape, Modifier),
        Triple(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE, ScreenLockLandscape,
            Modifier.scale(scaleX = 1f, scaleY = -1f)),
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        triples.forEach { triple ->
            JubroOrientButton(
                modifier = triple.third,
                activated = triple.first == screenOrient,
                onClick = {
                    onScreenOrientChange(triple.first)
                },
                icon = triple.second,
            )
        }
    }
}

@Composable
fun JubroOrientButton(
    modifier: Modifier = Modifier,
    activated: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
) {
    JubroIconButton(
        modifier = modifier
            .clip(CircleShape)
            .then(
                if (activated) {
                    Modifier.background(
                        MaterialTheme.colorScheme.inversePrimary
                    )
                } else { Modifier }
            )
        ,
        onClick = { onClick() },
        icon = icon
    )
}

@Composable
fun JupyterURLItem(
    value: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        label = {
            Text(stringResource(R.string.jupyter_url))
        },
        singleLine = true,
    )
}

@Composable
fun JubroSettingsSwitchItem(
    text: String,
    isChecked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() } // Make the entire row clickable
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
        Switch(
            checked = isChecked,
            onCheckedChange = null // The Row click handles the toggle
        )
    }
}
