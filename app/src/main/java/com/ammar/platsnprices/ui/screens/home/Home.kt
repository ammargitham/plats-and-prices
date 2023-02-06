package com.ammar.platsnprices.ui.screens.home

import android.os.Handler
import android.os.Looper
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ammar.platsnprices.R
import com.ammar.platsnprices.data.entities.RecentGameDiscount
import com.ammar.platsnprices.data.entities.Region
import com.ammar.platsnprices.data.entities.Resource
import com.ammar.platsnprices.data.entities.Sale
import com.ammar.platsnprices.data.entities.successOr
import com.ammar.platsnprices.ui.controllers.ModalBottomSheetController
import com.ammar.platsnprices.ui.controllers.ToolbarController
import com.ammar.platsnprices.ui.theme.PlatsNPricesTheme
import com.ammar.platsnprices.utils.getFlag
import com.ammar.platsnprices.utils.openUrl
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@ExperimentalComposeUiApi
@ExperimentalUnitApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalCoilApi
@Composable
fun Home(
    padding: PaddingValues,
    toolbarController: ToolbarController,
    modalBottomSheetController: ModalBottomSheetController,
    navigateToSale: (saleDbId: Long, name: String, imgUrl: String) -> Unit,
    navigateToProduct: (ppId: Long, name: String, imgUrl: String) -> Unit,
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    var filter by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        toolbarController.updateToolbar()
        Handler(Looper.getMainLooper()).postDelayed({
            modalBottomSheetController.setContent {
                RegionPickerContent(
                    selectedRegion = uiState.region,
                    filter = filter,
                    onFilterChange = { filter = it },
                    onRegionSelect = {
                        coroutineScope.launch {
                            modalBottomSheetController.hide()
                            viewModel.updateRegion(it)
                        }
                    }
                )
            }
        }, 1000)
    }

    DisposableEffect(Unit) {
        Handler(Looper.getMainLooper()).postDelayed({
            modalBottomSheetController.setCallbacks(
                onHide = { filter = "" }
            )
        }, 1000)
        onDispose { modalBottomSheetController.setCallbacks() }
    }

    HomeContent(
        padding = padding,
        salesLoading = uiState.sales is Resource.Loading,
        sales = uiState.sales.successOr(emptyList()),
        region = uiState.region,
        recentDiscountsLoading = uiState.recentDiscounts is Resource.Loading,
        recentDiscounts = uiState.recentDiscounts.successOr(emptyList()),
        onSaleClick = { navigateToSale(it.id, it.name, it.imgUrl) },
        onShowRegionPicker = {
            coroutineScope.launch {
                modalBottomSheetController.show()
            }
        },
        onRecentDiscountClick = {
            // PlatPrices API does not provide DLC info for any region and game info if the game region is not US
            if (uiState.region != Region.US) {
                context.openUrl(it.platPricesUrl)
                return@HomeContent
            }
            navigateToProduct(it.ppId, it.name, it.imgUrl)
        },
    )
}

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Composable
fun HomeContent(
    padding: PaddingValues = PaddingValues(0.dp),
    salesLoading: Boolean = false,
    sales: List<Sale> = emptyList(),
    region: Region? = null,
    recentDiscountsLoading: Boolean = false,
    recentDiscounts: List<RecentGameDiscount> = emptyList(),
    onSaleClick: (Sale) -> Unit = {},
    onShowRegionPicker: () -> Unit = {},
    onRecentDiscountClick: (RecentGameDiscount) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp),
        ) {
            RegionRow(
                region = region,
                onChangeClick = onShowRegionPicker,
            )
            Row {
                SalesContent(
                    loading = salesLoading,
                    sales = sales,
                    onSaleClick = onSaleClick,
                )
            }
            Spacer(modifier = Modifier.requiredHeight(8.dp))
            Row {
                RecentDiscountsContent(
                    loading = recentDiscountsLoading,
                    discounts = recentDiscounts,
                    onDiscountClick = onRecentDiscountClick,
                )
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
private fun RegionRow(
    region: Region?,
    onChangeClick: () -> Unit
) {
    Row(
        modifier = Modifier.padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = "${stringResource(R.string.current_region)}:")
            region?.let {
                Image(
                    modifier = Modifier.size(width = 24.dp, height = 20.dp),
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current).data(data = getFlag(it.code)).apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                            placeholder(R.drawable.image_placeholder)
                        }).build()
                    ),
                    contentDescription = "${stringResource(it.labelResId)} ${stringResource(R.string.flag)}",
                )
            }
            Text(
                text = stringResource(region?.labelResId ?: R.string.loading),
                maxLines = 1,
                overflow = TextOverflow.Clip,
                softWrap = false,
            )
        }
        OutlinedButton(
            onClick = onChangeClick
        ) {
            Text(text = stringResource(R.string.change))
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalCoilApi
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DefaultPreview() {
    PlatsNPricesTheme {
        HomeContent(
            sales = List(5) {
                Sale(
                    id = it.toLong(),
                    saleId = it.toLong(),
                    name = "Example Sale",
                    startTime = LocalDateTime.now(),
                    endTime = LocalDateTime.now().plusDays(3),
                    numGames = 1,
                    region = Region.US,
                    imgUrl = "",
                )
            }
        )
    }
}