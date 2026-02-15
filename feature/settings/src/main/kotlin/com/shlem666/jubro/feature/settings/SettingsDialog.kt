package com.shlem666.jubro.feature.settings

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shlem666.jubro.core.designsystem.component.JubroOrientButton
import com.shlem666.jubro.core.designsystem.component.JubroSettingsSwitchItem
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.ScreenLockLandscape
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.ScreenLockPortrait
import com.shlem666.jubro.core.designsystem.icon.JubroIcons.ScreenRotation
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
    var tempScreenOrient by rememberSaveable { mutableIntStateOf(
        ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    ) }

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
                tempScreenOrient = (uiState as Success)
                    .resources.screenOrient
            }
        }
    }

    @Composable
    fun DeviceOrientationItem() {
        val pairs = listOf(
            Pair(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED, ScreenRotation),
            Pair(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, ScreenLockPortrait),
            Pair(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, ScreenLockLandscape),
            Pair(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE, ScreenLockLandscape),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            pairs.forEach { pair ->
                JubroOrientButton(
                    modifier = Modifier
                        .then(if (
                            pair.first == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                        ) {
                            Modifier.scale(scaleX = 1f, scaleY = -1f)
                        } else { Modifier } )
                    ,
                    activated = pair.first == tempScreenOrient,
                    onClick = { tempScreenOrient = pair.first },
                    icon = pair.second,
                )
            }
        }
    }

    @Composable
    fun settingsItems() {
        Column(
            modifier = Modifier
                .verticalScroll( state = rememberScrollState() ),
        ) {
            DeviceOrientationItem()
            TextField(
                value = tempJupyterUrl,
                onValueChange = { tempJupyterUrl = it },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                ,
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
                    viewModel.updateScreenOrient(tempScreenOrient)
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