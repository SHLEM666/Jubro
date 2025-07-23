package com.shlem666.jubro.feature.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsDialog(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
) {
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { /* Do nothing */ },
        title = { Text("attention") },
        text = { Text("text") },
        modifier = modifier.fillMaxSize(),
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("no")
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("yes")
            }
        }
    )
}