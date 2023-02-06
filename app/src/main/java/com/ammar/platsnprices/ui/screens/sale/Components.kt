package com.ammar.platsnprices.ui.screens.sale

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ammar.platsnprices.R
import com.ammar.platsnprices.ui.composables.ButtonToggleGroup
import com.ammar.platsnprices.ui.composables.ButtonToggleOption
import com.ammar.platsnprices.ui.composables.ClickableChip
import com.ammar.platsnprices.ui.theme.PlatsNPricesTheme

@Composable
internal fun OptionsRow(
    selectedListMode: ListMode = ListMode.LIST,
    filters: Filters = Filters(),
    sort: Sort = Sort.NAME,
    onListModeChange: (ListMode) -> Unit = {},
    onFilterClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
) {
    val context = LocalContext.current

    val listModeOptions = remember {
        listOf(
            ButtonToggleOption(
                ListMode.LIST
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_round_view_list_24),
                    contentDescription = stringResource(R.string.list)
                )
            },
            ButtonToggleOption(
                ListMode.GRID
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_round_grid_view_24),
                    contentDescription = stringResource(R.string.grid)
                )
            },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
    ) {
        val scrollState = rememberScrollState()

        Row(
            modifier = Modifier
                .padding(8.dp)
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ButtonToggleGroup(
                options = listModeOptions,
                valueChecked = selectedListMode,
                cornerRadius = 18.dp,
                onCheckedChange = onListModeChange
            )
            SortChip(stringResource(getLabel(sort)), onSortClick)
            FilterChip(context, filters, onFilterClick)
        }
    }
}

private fun getLabel(sort: Sort): Int = when (sort) {
    Sort.NAME -> R.string.name
    Sort.SALE_PRICE -> R.string.price
    Sort.DISCOUNT_PCT -> R.string.discount
}

@Composable
@Preview(showBackground = true)
fun PreviewOptionsRow() {
    OptionsRow()
}

@Composable
@Preview(showBackground = true)
fun PreviewOptionsRowWithFilters() {
    OptionsRow(
        filters = Filters(
            type = Type.GAME,
            version = Version.PS4,
        )
    )
}

@Composable
private fun FilterChip(
    context: Context,
    filters: Filters,
    onClick: () -> Unit = {},
) {
    val label = getFilterChipText(context, filters)

    ClickableChip(
        isActive = filters.isActive,
        onClick = onClick,
    ) {
        Icon(
            painterResource(id = R.drawable.ic_round_filter_alt_24),
            contentDescription = label
        )
        Text(text = label)
    }
}

private fun getFilterChipText(
    context: Context,
    filters: Filters
): String {
    if (!filters.isActive) {
        return context.getString(R.string.filters)
    }
    val filterStrings = mutableListOf<String>()
    if (filters.type != Type.ALL) {
        val str = when (filters.type) {
            Type.GAME -> context.getString(R.string.games)
            Type.DLC -> "DLC"
            else -> ""
        }
        filterStrings.add(context.getString(R.string.type_result, str))
    }
    if (filters.type != Type.DLC && filters.version != Version.ALL) {
        val str = when (filters.version) {
            Version.PS4 -> "PS4"
            Version.PS5 -> "PS5"
            else -> ""
        }
        filterStrings.add(context.getString(R.string.version, str))
    }
    return filterStrings.joinToString(", ")
}

@Composable
private fun SortChip(
    label: String,
    onClick: () -> Unit = {},
) {
    ClickableChip(
        onClick = onClick,
    ) {
        Icon(
            painterResource(id = R.drawable.ic_round_sort_24),
            contentDescription = stringResource(R.string.sort_by)
        )
        Text(text = label)
    }
}

@Composable
internal fun FilterOptions(
    filters: Filters = Filters(),
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
    var versionEnabled = remember { filters.type != Type.DLC }
    var localFilters by remember { mutableStateOf(filters.copy()) }
    var saveEnabled by remember { mutableStateOf(localFilters != filters) }

    LaunchedEffect(localFilters) {
        saveEnabled = localFilters != filters
        versionEnabled = localFilters.type != Type.DLC
    }

    Surface {
        Column {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(text = stringResource(R.string.type))
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
                    var color = MaterialTheme.colors.onSurface
                    if (!versionEnabled) {
                        color = color.copy(alpha = ContentAlpha.disabled)
                    }
                    Text(
                        text = stringResource(R.string.ps_version),
                        color = color
                    )
                    ButtonToggleGroup(
                        options = versionOptions,
                        valueChecked = localFilters.version,
                        cornerRadius = 18.dp,
                        enabled = versionEnabled,
                        onCheckedChange = { localFilters = localFilters.copy(version = it) }
                    )
                }
            }
            Divider()
            Row(modifier = Modifier.padding(8.dp)) {
                TextButton(onClick = { localFilters = Filters() }) {
                    Text(text = stringResource(R.string.reset))
                }
                TextButton(
                    onClick = { onSave(localFilters) },
                    enabled = saveEnabled,
                ) {
                    Text(text = stringResource(R.string.save))
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewFilterOptions() {
    FilterOptions(
        filters = Filters(
            type = Type.DLC
        )
    )
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun PreviewFilterOptionsDark() {
    PlatsNPricesTheme {
        FilterOptions(
            filters = Filters(
                type = Type.DLC
            )
        )
    }
}

@Composable
internal fun SortOptions(
    sort: Sort = Sort.NAME,
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
    var saveEnabled by remember { mutableStateOf(localSort != sort) }

    LaunchedEffect(localSort) {
        saveEnabled = localSort != sort
    }

    Surface {
        Column {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(text = stringResource(R.string.sort_by))
                ButtonToggleGroup(
                    options = sortOptions,
                    valueChecked = localSort,
                    cornerRadius = 18.dp,
                    onCheckedChange = { localSort = it }
                )
            }
            Divider()
            Row(modifier = Modifier.padding(8.dp)) {
                TextButton(
                    onClick = { onSave(localSort) },
                    enabled = saveEnabled,
                ) {
                    Text(text = stringResource(R.string.save))
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewSortOptions() {
    SortOptions()
}