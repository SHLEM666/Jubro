package com.shlem666.jubro.feature.settings.items

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.shlem666.jubro.feature.settings.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JupyterURLItem(
    value: String,
    suggestions: List<String>,
    onValueChange: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    val filteredOptions = remember(value, suggestions) {
        suggestions.filter {
            it.contains(value, ignoreCase = true) && it != value
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded && filteredOptions.isNotEmpty(),
        onExpandedChange = { expanded = it },
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .menuAnchor(MenuAnchorType.PrimaryEditable, enabled = true)
            ,
            value = value,
            onValueChange = {
                onValueChange(it)
                expanded = true
            },
            label = {
                Text(stringResource(R.string.jupyter_url))
            },
            singleLine = true,
        )

        ExposedDropdownMenu(
            expanded = expanded && filteredOptions.isNotEmpty(),
            onDismissRequest = { expanded = false }
        ) {
            filteredOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}