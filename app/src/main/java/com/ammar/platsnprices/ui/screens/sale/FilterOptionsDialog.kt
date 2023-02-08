package com.ammar.platsnprices.ui.screens.sale

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ammar.platsnprices.R
import com.ammar.platsnprices.ui.composables.ButtonToggleGroup
import com.ammar.platsnprices.ui.composables.ButtonToggleOption

@Composable
fun FilterOptionsDialog(
    modifier: Modifier = Modifier,
    filters: Filters = Filters(),
    onDismiss: () -> Unit = {},
    onSave: (Filters) -> Unit = {},
) {
    val typeOptions = remember {
        listOf(
            ButtonToggleOption(
                Type.ALL
            ) {
                Text(text = stringResource(R.string.all))
            },
            ButtonToggleOption(
                Type.GAME
            ) {
                Text(text = stringResource(R.string.games))
            },
            ButtonToggleOption(
                Type.DLC
            ) {
                Text(text = "DLC")
            }
        )
    }
    val versionOptions = remember {
        listOf(
            ButtonToggleOption(
                Version.ALL
            ) {
                Text(text = stringResource(R.string.all))
            },
            ButtonToggleOption(
                Version.PS4
            ) {
                Text(text = "PS4")
            },
            ButtonToggleOption(
                Version.PS5
            ) {
                Text(text = "PS5")
            }
        )
    }

    var localFilters by remember { mutableStateOf(filters.copy()) }
    val saveEnabled by remember(localFilters) { derivedStateOf { localFilters != filters } }
    val versionDisabled by remember(localFilters) { derivedStateOf { localFilters.type == Type.DLC } }

    AlertDialog(
        modifier = modifier,
        title = {
            Text(text = stringResource(R.string.filters))
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                var color = contentColorFor(MaterialTheme.colors.surface)
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = stringResource(R.string.type),
                        color = color,
                    )
                    ButtonToggleGroup(
                        options = typeOptions,
                        valueChecked = localFilters.type,
                        cornerRadius = 18.dp,
                        onCheckedChange = { localFilters = localFilters.copy(type = it) }
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    if (versionDisabled) {
                        color = color.copy(alpha = ContentAlpha.disabled)
                    }
                    Text(
                        text = stringResource(R.string.ps_version),
                        color = color,
                    )
                    ButtonToggleGroup(
                        options = versionOptions,
                        valueChecked = localFilters.version,
                        cornerRadius = 18.dp,
                        enabled = !versionDisabled,
                        onCheckedChange = { localFilters = localFilters.copy(version = it) }
                    )
                }
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = { localFilters = filters }) {
                    Text(text = stringResource(R.string.reset))
                }
                TextButton(
                    onClick = { onSave(localFilters) },
                    enabled = saveEnabled,
                ) {
                    Text(text = stringResource(R.string.save))
                }
            }
        },
        onDismissRequest = onDismiss,
    )
}

@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewFilterOptionsDialog() {
    FilterOptionsDialog()
}
