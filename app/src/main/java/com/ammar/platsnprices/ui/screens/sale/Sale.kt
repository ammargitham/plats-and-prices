package com.ammar.platsnprices.ui.screens.sale

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.ammar.platsnprices.R
import com.ammar.platsnprices.data.entities.DlcDiscount
import com.ammar.platsnprices.data.entities.Region
import com.ammar.platsnprices.ui.controllers.ToolbarController
import com.ammar.platsnprices.ui.controllers.ToolbarState
import com.ammar.platsnprices.utils.openUrl
import com.ammar.platsnprices.utils.pnpDateTimeFormatter

@Composable
fun Sale(
    padding: PaddingValues,
    toolbarController: ToolbarController,
    name: String?,
    navigateToProduct: (ppId: Long, name: String, imgUrl: String) -> Unit,
) {
    val viewModel: SaleViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState(UiState(loading = true))

    var showFiltersDialog by remember { mutableStateOf(false) }
    var showSortOptionsDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(name, uiState.saleName, uiState.saleEndTime) {
        val saleName = uiState.saleName
        val toolbarTitle = saleName.ifBlank { name ?: "" }
        val endTimeStr = uiState.saleEndTime?.format(pnpDateTimeFormatter) ?: ""
        toolbarController.updateToolbar(
            ToolbarState(
                title = {
                    Column {
                        Text(
                        text = toolbarTitle,
                        style = MaterialTheme.typography.h6
                    )
                    if (endTimeStr.isNotBlank()) {
                        Text(
                            text = stringResource(R.string.ends_on, endTimeStr),
                            style = MaterialTheme.typography.subtitle2
                        )
                    }
                }
            }
        ))
    }

    SaleContent(
        padding = padding,
        discounts = uiState.discounts,
        loading = uiState.loading,
        listMode = uiState.listMode,
        filters = uiState.filters,
        sort = uiState.sort,
        onSortClick = { showSortOptionsDialog = true },
        onFilterClick = { showFiltersDialog = true },
        onListModeChange = { viewModel.updateListMode(it) },
        onDiscountClick = {
            // PlatPrices API does not provide DLC info for any region and game info if the game region is not US
            val openBrowser = it is DlcDiscount || uiState.saleRegion != Region.US
            if (openBrowser) {
                context.openUrl(it.platPricesUrl)
                return@SaleContent
            }
            navigateToProduct(it.ppId, it.name, it.imgUrl)
        }
    )

    if (showFiltersDialog) {
        FilterOptionsDialog(
            filters = uiState.filters,
            onDismiss = { showFiltersDialog = false },
            onSave = {
                viewModel.updateFilters(it)
                showFiltersDialog = false
            }
        )
    }

    if (showSortOptionsDialog) {
        SortOptionsDialog(
            sort = uiState.sort,
            onDismiss = { showSortOptionsDialog = false },
            onSave = {
                viewModel.updateSort(it)
                showSortOptionsDialog = false
            }
        )
    }
}
