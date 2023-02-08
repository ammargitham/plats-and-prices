package com.ammar.platsnprices.ui.screens.sale

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
fun SortOptionsDialog(
    modifier: Modifier = Modifier,
    sort: Sort = Sort.NAME,
    onDismiss: () -> Unit = {},
    onSave: (Sort) -> Unit = {},
) {
    val sortOptions = remember {
        listOf(
            ButtonToggleOption(
                Sort.NAME
            ) {
                Text(text = stringResource(R.string.name))
            },
            ButtonToggleOption(
                Sort.SALE_PRICE
            ) {
                Text(text = stringResource(R.string.price))
            },
            ButtonToggleOption(
                Sort.DISCOUNT_PCT
            ) {
                Text(text = stringResource(R.string.discount_pct))
            }
        )
    }
    var localSort by remember { mutableStateOf(sort) }
    val saveEnabled by remember(localSort) { derivedStateOf { localSort != sort } }

    AlertDialog(
        modifier = modifier,
        title = {
            Text(text = stringResource(R.string.sort_by))
        },
        text = {
            ButtonToggleGroup(
                options = sortOptions,
                valueChecked = localSort,
                cornerRadius = 18.dp,
                onCheckedChange = { localSort = it }
            )
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(
                    onClick = { onSave(localSort) },
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
fun PreviewSortOptionsDialog() {
    SortOptionsDialog()
}
