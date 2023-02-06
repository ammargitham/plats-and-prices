package com.ammar.platsnprices.ui.screens.product

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ammar.platsnprices.R
import com.ammar.platsnprices.data.entities.OpenCriticGame
import com.ammar.platsnprices.data.entities.Product
import com.ammar.platsnprices.ui.theme.PlatsNPricesTheme
import com.ammar.platsnprices.utils.dpToPx
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlin.math.min

@ExperimentalAnimationApi
@ExperimentalUnitApi
@ExperimentalCoilApi
@Composable
fun ProductContent(
    loading: Boolean = false,
    name: String? = null,
    imgUrl: String? = null,
    product: Product? = null,
    openCriticLoading: Boolean = false,
    openCriticGame: OpenCriticGame? = null,
    onScreenshotClick: ((Int) -> Unit)? = null,
    onVideoClick: (() -> Unit)? = null,
) {
    val coverArtUrl: String = remember(product?.coverArtUrl, product?.imgUrl, imgUrl) {
        when {
            product?.coverArtUrl?.isNotBlank() == true -> product.coverArtUrl
            product?.imgUrl?.isNotBlank() == true -> product.imgUrl
            else -> imgUrl ?: ""
        }
    }

    // val tag = "ProductContent"
    val coverHeight = 200
    val coverHeightDp = remember { coverHeight.dp }
    val coverHeightPx = coverHeight.dpToPx()
    val navIconWidthPx = 56.dpToPx()
    val imageCornerRadiusMin = 8.dpToPx()
    val imageCornerRadiusMax = 12.dpToPx()
    val imageMinSize = 32
    val imageMaxSize = 80

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val scrollState = rememberScrollState()
            val scrollDiff = coverHeightPx - scrollState.value
            val headerScrollDiff = scrollDiff.coerceIn(0f, coverHeightPx)
            val headerScrollProgress = 1 - (headerScrollDiff / coverHeightPx)
            val headerOffsetX = headerScrollProgress * navIconWidthPx
            val imageSize = (imageMinSize + ((imageMaxSize - imageMinSize) * (1 - headerScrollProgress))).dp
            val imageCornerRadius = imageCornerRadiusMax + ((imageCornerRadiusMin - imageCornerRadiusMax) * (1 - headerScrollProgress))
            val maxHeight = this.maxHeight

            Column(
                modifier = Modifier
                    .matchParentSize()
                    .verticalScroll(
                        state = scrollState
                    )
            ) {
                Cover(
                    coverArtUrl = coverArtUrl,
                    coverHeight = coverHeightDp,
                    scrollState = scrollState
                )
                MainContent(
                    modifier = Modifier
                        .heightIn(min = maxHeight)
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = imageSize + 16.dp,
                            bottom = 8.dp
                        ),
                    loading = loading,
                    product = product,
                    openCriticLoading = openCriticLoading,
                    openCriticGame = openCriticGame,
                    onScreenshotClick = onScreenshotClick,
                    onVideoClick = onVideoClick,
                )
            }
            HeaderRow(
                headerScrollProgress = headerScrollProgress,
                headerOffsetX = headerOffsetX,
                headerOffsetY = headerScrollDiff,
                imageSize = imageSize,
                imageCornerRadius = imageCornerRadius,
                imgUrl = product?.imgUrl ?: imgUrl ?: "",
                title = product?.productName ?: name ?: "",
                developer = product?.developer ?: "",
                publisher = product?.publisher ?: "",
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
private fun MainContent(
    modifier: Modifier,
    loading: Boolean,
    product: Product?,
    openCriticLoading: Boolean,
    openCriticGame: OpenCriticGame?,
    onScreenshotClick: ((Int) -> Unit)?,
    onVideoClick: (() -> Unit)?,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (product?.genres?.isNotEmpty() == true) {
            GenresRow(
                genres = product.genres,
                isPs4 = product.isPs4,
                isPs5 = product.isPs5,
                isVr = product.isVrInt,
            )
            Divider()
        }
        if (openCriticGame != null) {
            OpenCriticRow(openCriticGame)
            Divider()
        }
        if (!loading) {
            StoreButtons(
                formattedBasePrice = product?.formattedBasePrice ?: "",
                formattedSalePrice = product?.formattedSalePrice ?: "",
                formattedPlusPrice = if (product?.salePrice == product?.plusPrice) null else product?.formattedPlusPrice,
                psStoreUrl = product?.psStoreUrl,
                platPricesUrl = product?.platPricesUrl,
            )
            Divider()
        }
        ScreenshotsRow(
            videoUrl = product?.previewVideoUrl,
            onVideoClick = onVideoClick,
            screenshots = product?.screenShotUrls,
            onScreenshotClick = onScreenshotClick
        )
        if (!loading && product?.description?.isNotBlank() == true) {
            DescriptionRow(product.description)
            Divider()
        }
        if (!loading) {
            TrophiesRow(product?.trophies ?: emptyMap(), product?.trophyGuideUrl)
            Divider()
        }
        if (!loading && product?.guides?.isNotEmpty() == true) {
            OtherGuidesSection(product.guides)
            Divider()
        }
        if (!loading && product != null) {
            DetailsSection(product)
        }
    }
}

@ExperimentalCoilApi
@Composable
private fun Cover(
    coverArtUrl: String,
    coverHeight: Dp = 200.dp,
    scrollState: ScrollState
) {
    var coverSize by remember { mutableStateOf(Size.Zero) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(coverHeight)
            .onGloballyPositioned { coverSize = it.size.toSize() }
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = min(1f, 1 - (scrollState.value / 600f))
                    translationY = -scrollState.value * 0.1f
                },
            painter = // placeholder(R.drawable.image_placeholder)
            rememberAsyncImagePainter(ImageRequest.Builder(LocalContext.current).data(data = coverArtUrl).apply(block = fun ImageRequest.Builder.() {
                crossfade(true)
                // placeholder(R.drawable.image_placeholder)
            }).build()),
            contentDescription = stringResource(R.string.cover_image),
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, MaterialTheme.colors.surface),
                        startY = coverSize.height * 0.5f,
                    )
                )
        ) {}
    }
}

@ExperimentalAnimationApi
@ExperimentalPagerApi
@ExperimentalUnitApi
@ExperimentalCoilApi
@Composable
@Preview(showBackground = true)
fun PreviewProductContent() {
    PlatsNPricesTheme {
        ProductContent(
            product = tempGame,
            openCriticGame = tempOpenCriticGame,
        )
    }
}

@ExperimentalAnimationApi
@ExperimentalPagerApi
@ExperimentalUnitApi
@ExperimentalCoilApi
@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun PreviewProductContentDark() {
    PlatsNPricesTheme {
        ProductContent(
            product = tempGame,
            openCriticGame = tempOpenCriticGame,
        )
    }
}