package com.ammar.platsnprices.ui.screens.product

import android.content.Context
import android.text.format.Formatter
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import coil.compose.rememberAsyncImagePainter
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import com.ammar.platsnprices.R
import com.ammar.platsnprices.data.entities.EsrbRating
import com.ammar.platsnprices.data.entities.Genre
import com.ammar.platsnprices.data.entities.GuideSource
import com.ammar.platsnprices.data.entities.OpenCriticGame
import com.ammar.platsnprices.data.entities.Product
import com.ammar.platsnprices.data.entities.Tier
import com.ammar.platsnprices.data.entities.Trophy
import com.ammar.platsnprices.ui.composables.HtmlText
import com.ammar.platsnprices.ui.theme.OpenCriticFair
import com.ammar.platsnprices.ui.theme.OpenCriticFairStart
import com.ammar.platsnprices.ui.theme.OpenCriticFairStop
import com.ammar.platsnprices.ui.theme.OpenCriticMighty
import com.ammar.platsnprices.ui.theme.OpenCriticMightyStart
import com.ammar.platsnprices.ui.theme.OpenCriticMightyStop
import com.ammar.platsnprices.ui.theme.OpenCriticStrong
import com.ammar.platsnprices.ui.theme.OpenCriticStrongStart
import com.ammar.platsnprices.ui.theme.OpenCriticStrongStop
import com.ammar.platsnprices.ui.theme.OpenCriticWeak
import com.ammar.platsnprices.ui.theme.OpenCriticWeakStart
import com.ammar.platsnprices.ui.theme.OpenCriticWeakStop
import com.ammar.platsnprices.ui.theme.PSBlue
import com.ammar.platsnprices.ui.theme.PSPlus
import com.ammar.platsnprices.utils.dpToPx
import com.ammar.platsnprices.utils.format
import com.ammar.platsnprices.utils.genreStringRes
import com.ammar.platsnprices.utils.openUrl
import com.ammar.platsnprices.utils.pxToDp
import com.ammar.platsnprices.utils.toDate
import java.text.DateFormat
import kotlin.math.roundToInt

@Composable
internal fun HeaderRow(
    headerScrollProgress: Float = 0f,
    headerOffsetX: Float = 0f,
    headerOffsetY: Float = 0f,
    imageSize: Dp = 80.dp,
    imageCornerRadius: Float = 8.dpToPx(),
    imgUrl: String,
    title: String,
    developer: String,
    publisher: String,
) {
    val elevation = if (headerScrollProgress == 1f) AppBarDefaults.TopAppBarElevation else 0.dp
    val backgroundColor by animateColorAsState(
        if (headerScrollProgress == 1f) MaterialTheme.colors.primarySurface else Color.Transparent
    )

    val titleSizeMin = MaterialTheme.typography.h6.fontSize.value
    val titleSizeMax = MaterialTheme.typography.h5.fontSize.value
    val titleSize = (titleSizeMin + ((titleSizeMax - titleSizeMin) * (1 - headerScrollProgress))).sp

    val titleFontWeightMin = FontWeight.Medium.weight
    val titleFontWeightMax = FontWeight.Bold.weight
    val titleFontWeight = (titleFontWeightMin + ((titleFontWeightMax - titleFontWeightMin) * (1 - headerScrollProgress))).roundToInt()

    var titleLineCount by remember { mutableStateOf(1) }
    val showPublisher = when {
        publisher.isBlank() -> false
        titleLineCount > 1 -> developer.isBlank()
        else -> true
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .offset {
                IntOffset(
                    x = 0,
                    y = headerOffsetY.roundToInt()
                )
            },
        color = backgroundColor,
        elevation = elevation,
    ) {
        Row(
            modifier = Modifier
                .offset {
                    IntOffset(
                        x = headerOffsetX.roundToInt(),
                        y = 0
                    )
                }
                .padding(
                    horizontal = 16.dp,
                    vertical = 10.dp
                ),
            verticalAlignment = if (headerScrollProgress == 1f) Alignment.CenterVertically else Alignment.Top,
        ) {
            Image(
                modifier = Modifier
                    .size(imageSize)
                    .clip(RoundedCornerShape(imageCornerRadius)),
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = imgUrl).apply(block = fun ImageRequest.Builder.() {
                        crossfade(true)
                        placeholder(R.drawable.image_placeholder)
                    }).build()
                ),
                contentDescription = "Image",
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier.padding(start = 8.dp),
            ) {
                Text(
                    text = title,
                    fontSize = titleSize,
                    fontWeight = FontWeight(titleFontWeight),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { titleLineCount = it.lineCount }
                )
                if (developer.isNotBlank() && headerScrollProgress != 1f) {
                    Text(
                        modifier = Modifier.alpha(1 - headerScrollProgress),
                        text = developer,
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                if (showPublisher && headerScrollProgress != 1f) {
                    Text(
                        modifier = Modifier.alpha(1 - headerScrollProgress),
                        text = publisher,
                        style = MaterialTheme.typography.subtitle1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
internal fun GenresRow(
    genres: List<Genre> = emptyList(),
    isPs4: Boolean,
    isPs5: Boolean,
    isVr: Int,
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isPs4) {
            GenreChip {
                Image(
                    modifier = Modifier
                        .padding(
                            horizontal = 12.dp,
                            vertical = (4.5).dp
                        )
                        .size(26.dp),
                    painter = painterResource(R.drawable.ic_ps4),
                    contentDescription = "PS4",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.secondaryVariant),
                )
            }
        }
        if (isPs5) {
            GenreChip {
                Image(
                    modifier = Modifier
                        .padding(
                            horizontal = 12.dp,
                            vertical = (4.5).dp
                        )
                        .size(26.dp),
                    painter = painterResource(R.drawable.ic_ps5),
                    contentDescription = "PS5",
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.secondaryVariant),
                )
            }
        }
        if (isVr == 1 || isVr == 2) {
            GenreChip {
                Text(
                    modifier = Modifier.padding(16.dp, 8.dp),
                    text = if (isVr == 1) stringResource(R.string.vr_optional) else "VR",
                    color = MaterialTheme.colors.secondaryVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    fontSize = 14.sp,
                )
            }
        }
        genres.map {
            GenreChip {
                Text(
                    modifier = Modifier.padding(16.dp, 8.dp),
                    text = stringResource(genreStringRes(genre = it)),
                    color = MaterialTheme.colors.secondaryVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    fontSize = 14.sp,
                )
            }
        }
    }
}

@Composable
private fun GenreChip(
    content: @Composable () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colors.secondaryVariant.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, MaterialTheme.colors.secondaryVariant)
    ) {
        content()
    }
}

@Composable
internal fun OpenCriticRow(openCriticGame: OpenCriticGame) {
    val size = 55.dp
    var topCriticScore by remember { mutableStateOf(0f) }
    var percentRecommended by remember { mutableStateOf(0f) }
    val animationSpec = remember {
        tween<Float>(
            durationMillis = 1200,
            delayMillis = 300,
            easing = LinearOutSlowInEasing
        )
    }
    val animatedTopCriticScore by animateFloatAsState(
        targetValue = topCriticScore,
        animationSpec = animationSpec,
    )
    val animatedPercentRecommended by animateFloatAsState(
        targetValue = percentRecommended,
        animationSpec = animationSpec,
    )
    val context = LocalContext.current

    SideEffect {
        topCriticScore = openCriticGame.topCriticScore
        percentRecommended = openCriticGame.percentRecommended
    }

    Column(
        modifier = Modifier.clickable { context.openUrl(openCriticGame.reviewsUrl) },
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Image(
                    modifier = Modifier.size(size),
                    painter = painterResource(
                        id = when (openCriticGame.tier) {
                            Tier.MIGHTY -> R.drawable.opencritic_mighty_man
                            Tier.STRONG -> R.drawable.opencritic_strong_man
                            Tier.FAIR -> R.drawable.opencritic_fair_man
                            Tier.WEAK -> R.drawable.opencritic_weak_man
                            Tier.UNKNOWN -> 0
                        }
                    ),
                    contentDescription = openCriticGame.tier.name,
                )
                Text(
                    text = stringResource(R.string.open_critic_rating),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2,
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = animatedTopCriticScore.format(0),
                        textAlign = TextAlign.Center,
                    )
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(size)
                            .graphicsLayer {
                                rotationZ = 90f + (1.8f * (100 - animatedTopCriticScore))
                            },
                        progress = animatedTopCriticScore / 100,
                        color = when (openCriticGame.tier) {
                            Tier.MIGHTY -> OpenCriticMighty
                            Tier.STRONG -> OpenCriticStrong
                            Tier.FAIR -> OpenCriticFair
                            Tier.WEAK -> OpenCriticWeak
                            Tier.UNKNOWN -> MaterialTheme.colors.primary
                        },
                    )
                }
                Text(
                    text = stringResource(R.string.top_critic_average),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2,
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "${animatedPercentRecommended.format(0)}%",
                        textAlign = TextAlign.Center,
                    )
                    CriticsRecommendCircularProgress(
                        modifier = Modifier.size(size),
                        progress = animatedPercentRecommended / 100,
                        startColor = when (openCriticGame.tier) {
                            Tier.MIGHTY -> OpenCriticMightyStart
                            Tier.STRONG -> OpenCriticStrongStart
                            Tier.FAIR -> OpenCriticFairStart
                            Tier.WEAK -> OpenCriticWeakStart
                            Tier.UNKNOWN -> MaterialTheme.colors.primary
                        },
                        stopColor = when (openCriticGame.tier) {
                            Tier.MIGHTY -> OpenCriticMightyStop
                            Tier.STRONG -> OpenCriticStrongStop
                            Tier.FAIR -> OpenCriticFairStop
                            Tier.WEAK -> OpenCriticWeakStop
                            Tier.UNKNOWN -> MaterialTheme.colors.primary
                        },
                    )
                }
                Text(
                    text = stringResource(R.string.critics_recommend),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body2,
                )
            }
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.based_on_n_reviews, openCriticGame.numReviews),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.caption,
        )
    }
}

@Composable
fun CriticsRecommendCircularProgress(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = ProgressIndicatorDefaults.StrokeWidth,
    progress: Float = 0.7f,
    startColor: Color = OpenCriticMightyStart,
    stopColor: Color = OpenCriticMightyStop,
) {
    val offset = strokeWidth / 2
    val circularIndicatorDiameter = 40.dp
    val correctedProgress = progress.coerceIn(0f, 1f)
    val sweepAngle = 360 * correctedProgress
    val startAngle = 360 - sweepAngle

    Canvas(
        modifier = modifier
            .progressSemantics(progress)
            .size(circularIndicatorDiameter)
            .focusable()
            .rotate(180 * (correctedProgress - 1))
    ) {
        val offsetPx = offset.toPx()
        val strokeWidthPx = strokeWidth.toPx()
        val outerCircleSize = size.width - strokeWidthPx

        drawArc(
            Brush.horizontalGradient(
                0f to startColor,
                0.22f to startColor,
                0.78f to stopColor,
                1f to stopColor,
            ),
            startAngle,
            sweepAngle,
            false,
            topLeft = Offset(offsetPx, offsetPx),
            size = Size(outerCircleSize, outerCircleSize),
            style = Stroke(strokeWidthPx, 0f)
        )
    }
}

@Composable
fun StoreButtons(
    formattedSalePrice: String = "",
    formattedBasePrice: String = "",
    formattedPlusPrice: String? = null,
    psStoreUrl: String? = null,
    platPricesUrl: String? = null,
) {
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = PSBlue,
                contentColor = Color.White,
            ),
            enabled = psStoreUrl != null,
            onClick = { context.openUrl(psStoreUrl ?: return@Button) },
        ) {
            Image(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = R.drawable.ic_playstation_logo),
                contentDescription = stringResource(R.string.playstation)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = stringResource(R.string.buy_on_ps_store),
                    style = MaterialTheme.typography.button
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    formattedPlusPrice?.let {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ps_plus),
                                contentDescription = stringResource(R.string.ps_plus)
                            )
                            Text(
                                text = it,
                                color = PSPlus,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                    Text(
                        text = formattedSalePrice,
                        fontWeight = FontWeight.Bold
                    )
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        Text(
                            text = formattedBasePrice,
                            textDecoration = TextDecoration.LineThrough,
                        )
                    }
                }
            }
        }
        platPricesUrl?.let {
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = PSBlue,
                    contentColor = Color.White,
                ),
                onClick = { context.openUrl(it) },
            ) {
                Image(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.platprices),
                    contentDescription = stringResource(R.string.platprices)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(R.string.view_on_pp),
                    style = MaterialTheme.typography.button
                )
            }
        }
    }
}

@Composable
internal fun ScreenshotsRow(
    videoUrl: String? = null,
    onVideoClick: (() -> Unit)? = null,
    screenshots: List<String>? = null,
    onScreenshotClick: ((Int) -> Unit)? = null,
) {
    if (videoUrl == null && screenshots.isNullOrEmpty()) return

    val modifier = Modifier
        .height(150.dp)
        .aspectRatio(1.78f)
        .clip(RoundedCornerShape(8.dp))

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        videoUrl?.let {
            item {
                Box {
                    Image(
                        modifier = modifier.clickable(enabled = onVideoClick != null) { onVideoClick?.invoke() },
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current).data(data = it).apply(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                                placeholder(R.drawable.image_placeholder)
                                decoderFactory { sourceResult, options, _ -> VideoFrameDecoder(sourceResult.source, options) }
                            }).build()
                        ),
                        contentDescription = stringResource(R.string.video),
                        contentScale = ContentScale.Crop,
                    )
                    Image(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(color = Color.Black.copy(alpha = 0.5f))
                            .size(30.dp)
                            .align(Alignment.Center),
                        painter = painterResource(id = R.drawable.ic_round_play_arrow_24),
                        colorFilter = ColorFilter.tint(color = Color.White),
                        contentDescription = stringResource(R.string.play)
                    )
                }
            }
        }
        screenshots?.let {
            items(it.size, key = { index -> index }) { index ->
                Image(
                    modifier = modifier.clickable(enabled = onScreenshotClick != null) {
                        onScreenshotClick?.invoke(index)
                    },
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current).data(data = it[index]).apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                            placeholder(R.drawable.image_placeholder)
                        }).build()
                    ),
                    contentDescription = stringResource(R.string.screenshot),
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

@Composable
internal fun DescriptionRow(description: String) {
    val lineCountThreshold = 5
    var lineCount by remember { mutableStateOf(14) }
    var lineHeight by remember { mutableStateOf(57) }
    var expanded by remember { mutableStateOf(false) }
    var readMoreButtonSize by remember { mutableStateOf(Size.Zero) }
    val expandable = lineCount > lineCountThreshold
    val height = animateDpAsState(
        if (expandable && !expanded) {
            (lineHeight * lineCountThreshold).pxToDp()
        } else {
            (lineHeight * lineCount).pxToDp() + if (expandable) {
                (readMoreButtonSize.height.pxToDp() + 10.dp)
            } else {
                0.dp
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(height.value)
    ) {
        // Text(text = "Description", style = MaterialTheme.typography.subtitle2)
        Column(
            modifier = Modifier.matchParentSize(),
        ) {
            HtmlText(
                text = description,
            ) {
                lineCount = it.lineCount
                lineHeight = it.lineHeight
            }
        }
        if (expandable) {
            var modifier = Modifier.matchParentSize()
            if (!expanded) {
                modifier = modifier.background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, MaterialTheme.colors.surface),
                    )
                )
            }
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Bottom,
            ) {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            readMoreButtonSize = it.size.toSize()
                        },
                    onClick = { expanded = !expanded },
                ) {
                    Text(text = stringResource(if (expanded) R.string.read_less else R.string.read_more))
                }
            }
        }
    }
}

@Composable
internal fun TrophiesRow(
    trophies: Map<Trophy, Int>,
    trophyGuideUrl: String?
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.clickable(
            enabled = trophyGuideUrl != null,
        ) {
            trophyGuideUrl?.let { context.openUrl(it) }
        },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.trophies),
                style = MaterialTheme.typography.subtitle1,
            )
            if (trophyGuideUrl != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.trophy_guide),
                        style = MaterialTheme.typography.caption,
                    )
                    Image(
                        painter = painterResource(R.drawable.ic_round_chevron_right_24),
                        contentDescription = stringResource(R.string.trophy_guide)
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Trophy(Trophy.PLATINUM, trophies[Trophy.PLATINUM] ?: 0)
            Trophy(Trophy.GOLD, trophies[Trophy.GOLD] ?: 0)
            Trophy(Trophy.SILVER, trophies[Trophy.SILVER] ?: 0)
            Trophy(Trophy.BRONZE, trophies[Trophy.BRONZE] ?: 0)
        }
    }
}

@Composable
private fun Trophy(trophy: Trophy, count: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            modifier = Modifier.size(30.dp),
            painter = painterResource(
                id = when (trophy) {
                    Trophy.BRONZE -> R.drawable.bronze
                    Trophy.SILVER -> R.drawable.silver
                    Trophy.GOLD -> R.drawable.gold
                    Trophy.PLATINUM -> R.drawable.platinum
                }
            ),
            contentDescription = stringResource(
                when (trophy) {
                    Trophy.BRONZE -> R.string.bronze_trophy
                    Trophy.SILVER -> R.string.silver_trophy
                    Trophy.GOLD -> R.string.gold_trophy
                    Trophy.PLATINUM -> R.string.platinum_trophy
                }
            ),
        )
        Text(text = count.toString())
    }
}

@Composable
internal fun OtherGuidesSection(guides: Map<GuideSource, String>) {
    Column {
        Text(
            text = stringResource(R.string.other_guides),
            style = MaterialTheme.typography.subtitle1,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val context = LocalContext.current
            guides.entries.map {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val url = if (it.key != GuideSource.CUSTOM) it.value else it.value.split("~~~~")[1]
                            context.openUrl(url)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    var modifier = Modifier.size(30.dp)
                    if (it.key != GuideSource.YOUTUBE) {
                        modifier = modifier.clip(CircleShape)
                    }
                    Image(
                        modifier = modifier,
                        painter = painterResource(
                            when (it.key) {
                                GuideSource.PS_TROPHIES -> R.drawable.ic_ps_trophies
                                GuideSource.PS3_IMPORTS -> R.drawable.ps3imports
                                GuideSource.PYX -> R.drawable.powerpyx
                                GuideSource.KNOEF -> R.drawable.knoef
                                GuideSource.YOUTUBE -> R.drawable.yt_icon_rgb
                                GuideSource.TROPHIES_DE -> R.drawable.trophies_de
                                GuideSource.DEX -> R.drawable.dex
                                GuideSource.CUSTOM -> R.drawable.image_placeholder
                            }
                        ),
                        contentDescription = stringResource(R.string.logo)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (it.key) {
                            GuideSource.PS_TROPHIES -> stringResource(R.string.playstation_trophies)
                            GuideSource.PS3_IMPORTS -> stringResource(R.string.ps3_imports)
                            GuideSource.PYX -> stringResource(R.string.powerpyx)
                            GuideSource.KNOEF -> stringResource(R.string.knoef)
                            GuideSource.YOUTUBE -> stringResource(R.string.youtube)
                            GuideSource.TROPHIES_DE -> stringResource(R.string.trophies_de)
                            GuideSource.DEX -> stringResource(R.string.dex)
                            GuideSource.CUSTOM -> it.value.split("~~~~")[0]
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun DetailsSection(product: Product) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (product.rating.isNotBlank()) {
            Rating(product.rating, product.ratingDescription)
            Divider()
        }
        DetailSection(stringResource(R.string.platform), product.platforms.joinToString(", "))
        if (product.offlinePlayers > 0) {
            DetailSection(stringResource(R.string.offline_players), product.offlinePlayers.toString())
        }
        if (product.onlinePlayers > 0) {
            var subtitle = product.onlinePlayers.toString()
            if (product.isPsPlusNeeded) {
                subtitle += "\n${stringResource(R.string.ps_plus_required)}"
            }
            DetailSection(stringResource(R.string.online_players), subtitle)
        }
        if (product.isMoveInt == 1 || product.isMoveInt == 2) {
            DetailSection(
                stringResource(R.string.move_controllers),
                stringResource(if (product.isMoveInt == 1) R.string.optional else R.string.required)
            )
        }
        DetailSection(stringResource(R.string.difficulty), "${product.difficulty}/10")
        DetailSection(
            stringResource(R.string.length),
            stringResource(R.string.length_range_hrs, product.hoursLow.format(), product.hoursHigh.format())
        )
        DetailSection(
            stringResource(R.string.released),
            if (product.releaseDate != null) {
                DateFormat.getDateInstance(DateFormat.LONG).format(product.releaseDate!!.toDate())
            } else product.releaseDateStr
        )
        DetailSection(stringResource(R.string.publisher), product.publisher)
        if (product.developer.isNotBlank()) {
            DetailSection(stringResource(R.string.developer), product.developer)
        }
        if (product.voiceLanguageLabels.isNotEmpty()) {
            DetailSection(stringResource(R.string.languages), product.voiceLanguageLabels.joinToString(", "))
        }
        if (product.subtitleLanguageLabels.isNotEmpty()) {
            DetailSection(stringResource(R.string.subtitles), product.subtitleLanguageLabels.joinToString(", "))
        }
        if (product.ps4SizeBytes > 0 || product.ps5SizeBytes > 0) {
            val context = LocalContext.current
            val sizeStrings = getSizeStrings(product, context)
            DetailSection(stringResource(R.string.size), sizeStrings.joinToString(", "))
        }
        DetailSection(stringResource(R.string.game_id), product.gameId)
    }
}

private fun getSizeStrings(
    product: Product,
    context: Context
): List<String> {
    fun getFormattedSize(size: Long): String = Formatter.formatShortFileSize(context, size)
    val sizeStrings = mutableListOf<String>()
    if (product.ps4SizeBytes > 0) {
        val suffix = if (product.isPs5) " (PS4)" else ""
        sizeStrings.add("${getFormattedSize(product.ps4SizeBytes)}$suffix")
    }
    if (product.ps5SizeBytes > 0) {
        val suffix = if (product.isPs4) " (PS5)" else ""
        sizeStrings.add("${getFormattedSize(product.ps5SizeBytes)}$suffix")
    }
    return sizeStrings
}

@Composable
private fun DetailSection(title: String, subtitle: String) {
    Row {
        Text(
            modifier = Modifier
                .alignByBaseline()
                .weight(1f),
            text = title,
            style = MaterialTheme.typography.subtitle2,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier
                .alignByBaseline()
                .weight(2f),
            text = subtitle,
            // textAlign = TextAlign.End,
            style = MaterialTheme.typography.body2,
        )
    }
}

@Composable
private fun Rating(
    rating: String,
    ratingDescription: List<String>
) {
    val esrbRating = remember { EsrbRating.match(rating) }

    Row {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.rating),
            style = MaterialTheme.typography.subtitle2,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(2f),
            // horizontalAlignment = Alignment.End,
        ) {
            if (esrbRating != null) {
                Text(
                    text = esrbRating.labels.first(),
                    style = MaterialTheme.typography.body2,
                )
                Image(
                    modifier = Modifier.height(60.dp),
                    painter = painterResource(esrbRating.resId),
                    contentDescription = esrbRating.labels.first(),
                )
            } else {
                Text(
                    text = rating,
                    style = MaterialTheme.typography.body2,
                )
            }
            if (ratingDescription.isNotEmpty()) {
                Text(
                    text = ratingDescription.joinToString(", "),
                    // textAlign = TextAlign.End,
                    style = MaterialTheme.typography.body2,
                )
            }
        }
    }
}
