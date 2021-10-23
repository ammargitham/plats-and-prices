package com.ammar.platsnprices.ui.screens.sale

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.ammar.platsnprices.R
import com.ammar.platsnprices.data.entities.DlcDiscount
import com.ammar.platsnprices.data.entities.Region
import com.ammar.platsnprices.ui.controllers.ModalBottomSheetController
import com.ammar.platsnprices.ui.controllers.ToolbarController
import com.ammar.platsnprices.ui.controllers.ToolbarState
import com.ammar.platsnprices.utils.openUrl
import com.ammar.platsnprices.utils.pnpDateTimeFormatter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@ExperimentalComposeUiApi
@ExperimentalUnitApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalCoilApi
@Composable
fun Sale(
    padding: PaddingValues,
    toolbarController: ToolbarController,
    modalBottomSheetController: ModalBottomSheetController,
    saleDbId: Long?,
    name: String?,
    imgUrl: String?,
    navigateToProduct: (ppId: Long, name: String, imgUrl: String) -> Unit,
) {
    val viewModel: SaleViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState(UiState(loading = true))

    var showFilters by remember { mutableStateOf(false) }
    var showSortOptions by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    DisposableEffect(Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            modalBottomSheetController.setCallbacks(onHide = {
                showFilters = false
                showSortOptions = false
            })
        }, 1000)

        onDispose { modalBottomSheetController.setCallbacks() }
    }

    LaunchedEffect(name, uiState.saleName, uiState.saleEndTime) {
        val saleName = uiState.saleName
        val toolbarTitle = if (saleName.isNotBlank()) saleName else name ?: ""
        val endTimeStr = uiState.saleEndTime?.format(pnpDateTimeFormatter) ?: ""
        toolbarController.updateToolbar(ToolbarState(
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

    DisposableEffect(Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            modalBottomSheetController.setContent {
                when {
                    showFilters -> FilterOptions(uiState.filters) {
                        coroutineScope.launch {
                            modalBottomSheetController.hide()
                            viewModel.updateFilters(it)
                            showFilters = false
                        }
                    }
                    showSortOptions -> SortOptions(uiState.sort) {
                        coroutineScope.launch {
                            modalBottomSheetController.hide()
                            viewModel.updateSort(it)
                            showSortOptions = false
                        }
                    }
                    else -> Text(text = "")
                }
            }
        }, 1000)

        onDispose {
            modalBottomSheetController.setContent(null)
        }
    }

    SaleContent(
        padding = padding,
        discounts = uiState.discounts,
        loading = uiState.loading,
        listMode = uiState.listMode,
        filters = uiState.filters,
        sort = uiState.sort,
        onSortClick = {
            coroutineScope.launch {
                showSortOptions = true
                modalBottomSheetController.show()
            }
        },
        onFilterClick = {
            coroutineScope.launch {
                showFilters = true
                modalBottomSheetController.show()
            }
        },
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
}
