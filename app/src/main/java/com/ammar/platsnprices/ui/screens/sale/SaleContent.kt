package com.ammar.platsnprices.ui.screens.sale

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.ammar.platsnprices.data.entities.Discount
import com.ammar.platsnprices.data.entities.GameDiscount
import com.ammar.platsnprices.ui.composables.discount.DiscountCard
import com.ammar.platsnprices.ui.composables.discount.DiscountTile
import com.ammar.platsnprices.ui.theme.PlatsNPricesTheme
import java.time.LocalDateTime

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalCoilApi
@Composable
fun SaleContent(
    padding: PaddingValues = PaddingValues(0.dp),
    discounts: List<Discount>,
    loading: Boolean = false,
    listMode: ListMode = ListMode.LIST,
    filters: Filters = Filters(),
    sort: Sort = Sort.NAME,
    onSortClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onListModeChange: (ListMode) -> Unit = {},
    onDiscountClick: (Discount) -> Unit = {},
) {
    val listState = rememberLazyListState()

    LaunchedEffect(sort, filters.version, filters.type) {
        // Scroll to top on filters or sorting change
        listState.scrollToItem(0)
    }

    if (loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator()
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OptionsRow(
                selectedListMode = listMode,
                filters = filters,
                sort = sort,
                onListModeChange = onListModeChange,
                onSortClick = onSortClick,
                onFilterClick = onFilterClick,
            )
            when (listMode) {
                ListMode.LIST -> SaleContentList(listState, discounts, onDiscountClick)
                ListMode.GRID -> SaleContentGrid(listState, discounts, onDiscountClick)
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
private fun SaleContentList(
    state: LazyListState,
    discounts: List<Discount>,
    onDiscountClick: (Discount) -> Unit = {},
) {
    LazyColumn(
        state = state,
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 0.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(discounts) { DiscountCard(it) { onDiscountClick(it) } }
    }
}

@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun SaleContentGrid(
    state: LazyListState,
    discounts: List<Discount>,
    onDiscountClick: (Discount) -> Unit = {},
) {
    LazyVerticalGrid(
        state = state,
        cells = GridCells.Adaptive(minSize = 150.dp),
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 0.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(discounts) { DiscountTile(discount = it) { onDiscountClick(it) } }
    }
}


private val previewGameDiscounts = List(5) {
    GameDiscount(
        id = it.toLong(),
        ppId = it.toLong(),
        saleDbId = it.toLong(),
        name = "War Tech Fighters",
        imgUrl = "https://image.api.playstation.com/cdn/UP1195/CUSA14243_00/5mRM6yX9S1caOciy3481tXrK2eZBVMQr.png",
        difficulty = 3,
        hoursLow = 15F,
        hoursHigh = 18F,
        isPs4 = true,
        isPs5 = false,
        lastDiscounted = null,
        discountedUntil = LocalDateTime.now().plusDays(3),
        basePrice = 1999,
        salePrice = 1199,
        plusPrice = 799,
        formattedBasePrice = "$19.99",
        formattedSalePrice = "$11.99",
        formattedPlusPrice = "$7.99",
        platPricesUrl = "https://platprices.com/en-us/game/4973-war-tech-fighters"
    )
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalCoilApi
@Preview(showSystemUi = true)
@Composable
fun PreviewSaleContent() {
    PlatsNPricesTheme {
        SaleContent(
            discounts = previewGameDiscounts,
            listMode = ListMode.LIST,
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalCoilApi
@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSaleContentDark() {
    PlatsNPricesTheme {
        SaleContent(
            discounts = previewGameDiscounts,
            listMode = ListMode.LIST,
        )
    }
}

@ExperimentalFoundationApi
@ExperimentalCoilApi
@Preview(showBackground = true)
@Composable
fun PreviewSaleContentList() {
    PlatsNPricesTheme {
        SaleContentList(rememberLazyListState(), previewGameDiscounts)
    }
}

@ExperimentalFoundationApi
@ExperimentalCoilApi
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSaleContentListDark() {
    PlatsNPricesTheme {
        SaleContentList(rememberLazyListState(), previewGameDiscounts)
    }
}

@ExperimentalFoundationApi
@ExperimentalCoilApi
@Preview(showBackground = true)
@Composable
fun PreviewSaleContentGrid() {
    PlatsNPricesTheme {
        SaleContentGrid(rememberLazyListState(), previewGameDiscounts)
    }
}

@ExperimentalFoundationApi
@ExperimentalCoilApi
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSaleContentGridDark() {
    PlatsNPricesTheme {
        SaleContentGrid(rememberLazyListState(), previewGameDiscounts)
    }
}