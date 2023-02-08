package com.ammar.platsnprices.ui.screens.sale

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
