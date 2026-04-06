package com.shlem666.jubro.feature.settings.items

import android.content.ClipData
import android.text.SpannedString
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import com.shlem666.jubro.feature.settings.R
import com.shlem666.jubro.feature.settings.SettingsViewModel

@Composable
fun HintTextSwitchItem(
    primaryText: String,
    secondaryText: String,
    isChecked: Boolean,
    onToggle: () -> Unit
) {
    var showHintDialog by rememberSaveable { mutableStateOf(false) }
    if (showHintDialog) {
        HintDialog( onDismiss = { showHintDialog = false } )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column (
            modifier = Modifier
                .weight(1f)
                .clickable { showHintDialog = true }
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = primaryText,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = secondaryText,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .clickable { onToggle() }
                .padding(vertical = 16.dp)
            ,
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            VerticalDivider(
                thickness = Dp.Hairline,
                color = MaterialTheme.colorScheme.onSurface
            )
            Box( modifier = Modifier.padding(start = 24.dp) ) {
                Switch(
                    checked = isChecked,
                    // The Row click handles the toggle
                    onCheckedChange = null
                )
            }
        }
    }
}

@Composable
fun HintDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()
    val code = viewModel.getCode("KeyboardShortcuts.json")
    val spanStyle = SpanStyle(
        color = MaterialTheme.colorScheme.primary,
        fontFamily = FontFamily.Monospace,
    )
    val spannedStrings = listOf(
        R.string.about_js_api_p1,
        R.string.about_js_api_p2,
        R.string.about_js_api_p3,
    ).map {
        SpannedString( context.getText(it) )
    }

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { onDismiss() },
        title = { Text( stringResource(R.string.about_js_api) ) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll( state = rememberScrollState() )
            ) {
                spannedStrings.forEach {
                    StyledTextBlock(it, spanStyle)
                }
                CodeBlock(code)
            }
        },
        modifier = modifier.fillMaxSize(),
        confirmButton = {
            TextButton(
                onClick = {
                    scope.launch {
                        clipboard.setClipEntry(
                            ClipData.newPlainText(
                                "code", code
                            ).toClipEntry()
                        )
                    }
                }
            ) {
                Text( stringResource(R.string.btn_copy) )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text( stringResource(R.string.btn_close) )
            }
        },
    )
}

@Composable
fun StyledTextBlock(
    spannedString: SpannedString,
    spanStyle: SpanStyle
) {
    Text(
        buildAnnotatedString {
            append( spannedString.toString() )
            spannedString.getSpans(
                0,
                spannedString.length,
                Any::class.java
            ).forEach { span ->
                addStyle(
                    spanStyle,
                    spannedString.getSpanStart(span),
                    spannedString.getSpanEnd(span)
                )
            }
        }
    )
}

@Composable
fun CodeBlock(code: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
        ,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        SelectionContainer(
            modifier = Modifier
                .horizontalScroll( state = rememberScrollState() )
        ) {
            Text(
                text = code,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}