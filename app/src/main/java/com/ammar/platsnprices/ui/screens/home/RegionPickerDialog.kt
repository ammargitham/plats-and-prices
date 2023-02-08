package com.ammar.platsnprices.ui.screens.home

import android.content.res.Configuration
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ammar.platsnprices.data.entities.Region

@Composable
fun RegionPickerDialog(
    modifier: Modifier = Modifier,
    selectedRegion: Region? = null,
    onRegionSelect: (Region) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    val context = LocalContext.current

    var filter by remember { mutableStateOf("") }
    val items by remember(filter) {
        derivedStateOf {
            val regex = Regex("(?i).*$filter.*")
            Region.values()
                .filter {
                    if (filter.isNotBlank()) {
                        return@filter context.getString(it.labelResId).matches(regex)
                    }
                    true
                }
                .sortedBy { context.getString(it.labelResId) }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Card(
            modifier = modifier,
        ) {
            Column(
                modifier = Modifier
                    .requiredHeightIn(max = 500.dp)
                    .padding(16.dp)
                    .scrollable(
                        state = rememberScrollState(),
                        orientation = Orientation.Vertical,
                    ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                SearchField(
                    modifier = Modifier.fillMaxWidth(),
                    text = filter,
                    onChange = { filter = it },
                )
                RegionList(
                    items = items,
                    selectedRegion = selectedRegion,
                    onRegionSelect = onRegionSelect,
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewRegionPickerDialog() {
    RegionPickerDialog()
}
