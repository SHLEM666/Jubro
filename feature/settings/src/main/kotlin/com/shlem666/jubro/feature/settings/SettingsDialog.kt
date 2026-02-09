package com.shlem666.jubro.feature.settings

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

    @Composable
    fun jupyterUrlField(): String {
        return when (uiState) {
            is Loading -> {
                stringResource(R.string.loading)
            }
            is Success -> {
                if (firstAccess) {
                    firstAccess = false
                    tempJupyterUrl = (uiState as Success)
                        .resources.jupyterUrl
                }
                tempJupyterUrl
            }
        }
    }

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { /* Do nothing */ },
        title = {
            Text( stringResource( R.string.feature_settings_title ) )
        },
        text = {
            TextField(
                value = jupyterUrlField(),
                onValueChange = { tempJupyterUrl = it },
                modifier = modifier.fillMaxWidth(),
                label = {
                    Text( stringResource(R.string.jupyter_url) )
                },
                singleLine = true,
            )
        },
        modifier = modifier.fillMaxSize(),
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.updateJupyterUrl(tempJupyterUrl)
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