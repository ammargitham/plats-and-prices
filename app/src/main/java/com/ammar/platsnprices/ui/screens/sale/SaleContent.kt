package com.ammar.platsnprices.ui.screens.sale

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ammar.platsnprices.data.entities.Discount
import com.ammar.platsnprices.data.entities.GameDiscount
import com.ammar.platsnprices.ui.composables.discount.DiscountTile
import com.ammar.platsnprices.ui.theme.PlatsNPricesTheme
import java.time.LocalDateTime

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
    val listState = rememberLazyGridState()

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
            SaleContentGrid(listMode, listState, discounts, onDiscountClick)
            // when (listMode) {
            //     ListMode.LIST -> SaleContentList(listState, discounts, onDiscountClick)
            //     ListMode.GRID ->
            // }
        }
    }
}

// @ExperimentalCoilApi
// @Composable
// private fun SaleContentList(
//     state: LazyListState,
//     discounts: List<Discount>,
//     onDiscountClick: (Discount) -> Unit = {},
// ) {
//     LazyColumn(
//         state = state,
//         contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp, top = 0.dp),
//         verticalArrangement = Arrangement.spacedBy(8.dp)
//     ) {
//         items(discounts) { DiscountCard(it) { onDiscountClick(it) } }
//     }
// }

@Composable
fun SaleContentGrid(
    listMode: ListMode = ListMode.LIST,
    state: LazyGridState,
    discounts: List<Discount>,
    onDiscountClick: (Discount) -> Unit = {},
) {
    LazyVerticalGrid(
        state = state,
        columns = if (listMode == ListMode.LIST) GridCells.Fixed(1) else GridCells.Adaptive(minSize = 150.dp),
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

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSaleContent() {
    PlatsNPricesTheme {
        SaleContent(
            discounts = previewGameDiscounts,
            listMode = ListMode.LIST,
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSaleContentList() {
    PlatsNPricesTheme {
        SaleContentGrid(ListMode.LIST, rememberLazyGridState(), previewGameDiscounts)
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSaleContentGrid() {
    PlatsNPricesTheme {
        SaleContentGrid(ListMode.GRID, rememberLazyGridState(), previewGameDiscounts)
    }
}
