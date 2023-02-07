package com.ammar.platsnprices.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ammar.platsnprices.R
import com.ammar.platsnprices.data.entities.RecentGameDiscount
import com.ammar.platsnprices.data.entities.Region
import com.ammar.platsnprices.data.entities.Sale
import com.ammar.platsnprices.ui.composables.discount.DiscountPrice
import com.ammar.platsnprices.ui.theme.PlatsNPricesTheme
import com.ammar.platsnprices.utils.DateTimeDiff
import com.ammar.platsnprices.utils.dpToPx
import com.ammar.platsnprices.utils.getDateTimeDiff
import com.ammar.platsnprices.utils.getFlag
import com.ammar.platsnprices.utils.pxToDp
import com.ammar.platsnprices.utils.trimAll
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import java.time.LocalDateTime
import kotlin.math.roundToInt

@Composable
fun SalesContent(
    loading: Boolean = false,
    sales: List<Sale> = emptyList(),
    onSaleClick: (sale: Sale) -> Unit = {}
) {
    val itemSpacing = 8.dp
    val itemSpacingPx = with(LocalDensity.current) { itemSpacing.toPx() }
    val maxItemSize = 180.dpToPx()
    val peekWidth = 50.dpToPx()
    var fullyVisibleItemCount by remember { mutableStateOf(0) }
    var itemSize by remember { mutableStateOf(300f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged {
                fullyVisibleItemCount = ((it.width - peekWidth) / (maxItemSize + itemSpacingPx)).roundToInt()
                if (fullyVisibleItemCount == 0) {
                    fullyVisibleItemCount = 1
                }
                itemSize = ((it.width - peekWidth) / fullyVisibleItemCount) - itemSpacingPx
            }
    ) {
        Row {
            Text(
                text = stringResource(R.string.current_sales),
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.h6,
            )
        }
        if (loading) {
            Row(
                modifier = Modifier
                    .padding(8.dp, 0.dp)
                    .horizontalScroll(
                        enabled = false,
                        state = rememberScrollState()
                    ),
                horizontalArrangement = Arrangement.spacedBy(itemSpacing)
            ) {
                repeat(fullyVisibleItemCount + 1) {
                    Sale(
                        size = itemSize.pxToDp(),
                        loading = true
                    )
                }
            }
        } else {
            if (sales.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_ongoing_sales),
                    modifier = Modifier.padding(8.dp, 0.dp),
                    style = MaterialTheme.typography.subtitle2,
                )
            } else {
                LazyRow(
                    contentPadding = PaddingValues(8.dp, 0.dp),
                    horizontalArrangement = Arrangement.spacedBy(itemSpacing)
                ) {
                    items(
                        items = sales,
                        itemContent = {
                            Sale(
                                size = itemSize.pxToDp(),
                                sale = it,
                                onSaleClick = onSaleClick
                            )
                        },
                        key = { it.id }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSales() {
    SalesContent(
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

@Preview(showBackground = true)
@Composable
fun PreviewSalesNoSales() {
    SalesContent()
}

@Composable
fun Sale(
    size: Dp = 200.dp,
    loading: Boolean = false,
    sale: Sale? = null,
    onSaleClick: (sale: Sale) -> Unit = {}
) {
    val dateTimeDiff: DateTimeDiff by remember {
        val endTime: LocalDateTime = sale?.endTime ?: LocalDateTime.now()
        mutableStateOf(getDateTimeDiff(endTime))
    }

    AnimatedVisibility(visible = true) {
        Column {
            Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .size(size)
                    .clickable(enabled = sale != null) { onSaleClick(sale ?: return@clickable) }
                    .placeholder(
                        visible = loading,
                        highlight = PlaceholderHighlight.fade(),
                    ),
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = sale?.imgUrl ?: R.drawable.image_placeholder)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                            placeholder(R.drawable.image_placeholder)
                        }).build()
                ),
                contentDescription = null
            )
            Spacer(modifier = Modifier.requiredHeight(8.dp))
            EndsInText(
                loading = loading,
                dateTimeDiff = dateTimeDiff
            )
        }
    }
}

@Composable
private fun EndsInText(
    loading: Boolean = false,
    dateTimeDiff: DateTimeDiff
) {
    var color = Color.Unspecified
    val text: String
    if (dateTimeDiff.value >= 0) {
        text = stringResource(R.string.ends_in, dateTimeDiff.toFormattedString(LocalContext.current))
    } else {
        text = stringResource(R.string.expired)
        color = MaterialTheme.colors.error
    }
    Text(
        modifier = Modifier.placeholder(
            visible = loading,
            highlight = PlaceholderHighlight.fade(),
        ),
        text = text,
        style = MaterialTheme.typography.subtitle2,
        color = color
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewSale() {
    Sale(
        sale = Sale(
            id = 0L,
            saleId = 0L,
            name = "Example Sale",
            startTime = LocalDateTime.now(),
            endTime = LocalDateTime.now().plusDays(3),
            numGames = 1,
            region = Region.US,
            imgUrl = "",
        )
    )
}


@Composable
fun RecentDiscountsContent(
    loading: Boolean = false,
    discounts: List<RecentGameDiscount> = emptyList(),
    onDiscountClick: (RecentGameDiscount) -> Unit = {},
) {
    val itemSpacing = 8.dp
    val itemSpacingPx = with(LocalDensity.current) { itemSpacing.toPx() }
    val maxItemSize = 180.dpToPx()
    val peekWidth = 50.dpToPx()
    var fullyVisibleItemCount by remember { mutableStateOf(0) }
    var itemSize by remember { mutableStateOf(300f) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged {
                fullyVisibleItemCount = ((it.width - peekWidth) / (maxItemSize + itemSpacingPx)).roundToInt()
                if (fullyVisibleItemCount == 0) {
                    fullyVisibleItemCount = 1
                }
                itemSize = ((it.width - peekWidth) / fullyVisibleItemCount) - itemSpacingPx
            }
    ) {
        Row {
            Text(
                text = stringResource(R.string.recent_discounts),
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.h6,
            )
        }
        if (loading) {
            Row(
                modifier = Modifier
                    .padding(8.dp, 0.dp)
                    .horizontalScroll(
                        enabled = false,
                        state = rememberScrollState()
                    ),
                horizontalArrangement = Arrangement.spacedBy(itemSpacing)
            ) {
                repeat(fullyVisibleItemCount + 1) {
                    RecentDiscountContent(
                        size = itemSize.pxToDp(),
                        loading = true
                    )
                }
            }
        } else {
            if (discounts.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_recent_discounts),
                    modifier = Modifier.padding(8.dp, 0.dp),
                    style = MaterialTheme.typography.subtitle2,
                )
            } else {
                LazyRow(
                    contentPadding = PaddingValues(8.dp, 0.dp),
                    horizontalArrangement = Arrangement.spacedBy(itemSpacing)
                ) {
                    items(
                        items = discounts,
                        itemContent = {
                            RecentDiscountContent(
                                size = itemSize.pxToDp(),
                                discount = it,
                                onClick = onDiscountClick
                            )
                        },
                        key = { it.id }
                    )
                }
            }
        }
    }
}

@Composable
fun RecentDiscountContent(
    size: Dp = 200.dp,
    loading: Boolean = false,
    discount: RecentGameDiscount? = null,
    onClick: (RecentGameDiscount) -> Unit = {}
) {
    AnimatedVisibility(visible = true) {
        Column {
            Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .size(size)
                    .clickable(enabled = discount != null) { onClick(discount ?: return@clickable) }
                    .placeholder(
                        visible = loading,
                        highlight = PlaceholderHighlight.fade(),
                    ),
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = discount?.imgUrl ?: R.drawable.image_placeholder)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                            placeholder(R.drawable.image_placeholder)
                        }).build()
                ),
                contentDescription = null
            )
            Spacer(modifier = Modifier.requiredHeight(8.dp))
            Text(
                modifier = Modifier.placeholder(
                    visible = loading,
                    highlight = PlaceholderHighlight.fade(),
                ),
                text = discount?.name ?: stringResource(R.string.loading),
                style = MaterialTheme.typography.subtitle2,
            )
            if (discount != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(
                            text = discount.formattedBasePrice,
                            style = MaterialTheme.typography.subtitle2,
                            textDecoration = TextDecoration.LineThrough,
                        )
                    }
                    DiscountPrice(
                        discount.formattedSalePrice,
                        discount.saleDiscountPct,
                    )
                }
                if (discount.salePrice != discount.plusPrice) {
                    DiscountPrice(
                        discount.formattedPlusPrice,
                        discount.plusDiscountPct,
                        true,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRecentDiscountsContent() {
    PlatsNPricesTheme {
        RecentDiscountsContent(
            discounts = List(5) {
                RecentGameDiscount(
                    id = it.toLong(),
                    ppId = it.toLong(),
                    region = Region.US,
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
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRecentDiscountContent() {
    PlatsNPricesTheme {
        RecentDiscountContent(
            discount = RecentGameDiscount(
                id = 1,
                ppId = 1,
                region = Region.US,
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
        )
    }
}

@Composable
fun RegionPickerContent(
    selectedRegion: Region? = null,
    filter: String = "",
    onRegionSelect: (Region) -> Unit = {},
    onFilterChange: (String) -> Unit = {},
) {
    val context = LocalContext.current
    var items by remember { mutableStateOf(emptyList<Region>()) }

    LaunchedEffect(filter) {
        val regex = Regex("(?i).*$filter.*")
        items = Region.values()
            .filter {
                if (filter.isNotBlank()) {
                    return@filter context.getString(it.labelResId).matches(regex)
                }
                true
            }
            .sortedBy { context.getString(it.labelResId) }
    }

    Column(
        modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SearchField(
            modifier = Modifier.fillMaxWidth(),
            text = filter,
            onChange = onFilterChange,
        )
        RegionList(
            items = items,
            selectedRegion = selectedRegion,
            onRegionSelect = onRegionSelect
        )
    }
}

@Composable
private fun SearchField(
    modifier: Modifier = Modifier,
    text: String = "",
    onChange: (String) -> Unit = {},
) {
    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = { onChange(it.trimAll()) },
        shape = RoundedCornerShape(percent = 50),
        singleLine = true,
        placeholder = {
            Text(text = "Search")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search"
            )
        },
        trailingIcon = {
            if (text.isNotEmpty()) {
                IconButton(onClick = { onChange("") }) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Clear"
                    )
                }
            }
        }
    )
}

@Composable
private fun RegionList(
    items: List<Region>,
    selectedRegion: Region?,
    onRegionSelect: (Region) -> Unit
) {
    var minHeight by remember { mutableStateOf(0) }

    LazyColumn(
        modifier = Modifier
            .heightIn(min = minHeight.pxToDp())
            .onGloballyPositioned {
                minHeight = it.size.height
            },
        state = rememberLazyListState(),
    ) {
        items(
            items = items,
            key = { it.code }
        ) {
            Row(
                modifier = Modifier
                    .clickable { onRegionSelect(it) }
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        modifier = Modifier.size(32.dp),
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current).data(data = getFlag(it.code)).apply(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                                placeholder(R.drawable.image_placeholder)
                            }).build()
                        ),
                        contentDescription = "${stringResource(it.labelResId)} ${stringResource(R.string.flag)}",
                    )
                    Text(
                        text = stringResource(it.labelResId),
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        softWrap = false,
                    )
                }
                if (it == selectedRegion) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_round_check_24),
                        contentDescription = "selected"
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegionPickerContentPreview() {
    PlatsNPricesTheme {
        RegionPickerContent(
            selectedRegion = Region.AR,
        )
    }
}