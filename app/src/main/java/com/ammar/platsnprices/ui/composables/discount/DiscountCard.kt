package com.ammar.platsnprices.ui.composables.discount

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ammar.platsnprices.R
import com.ammar.platsnprices.data.entities.Discount
import com.ammar.platsnprices.data.entities.GameDiscount
import com.ammar.platsnprices.ui.composables.ChipSize
import com.ammar.platsnprices.ui.composables.SimpleChip
import com.ammar.platsnprices.ui.theme.PlatsNPricesTheme
import java.time.LocalDateTime

@ExperimentalCoilApi
@Composable
fun DiscountCard(
    discount: Discount,
    onClick: () -> Unit = {}
) {
    val surfaceColor = MaterialTheme.colors.surface
    val cardColor by remember { mutableStateOf(surfaceColor) }
    val textColor by remember { mutableStateOf(Color.Unspecified) }
    val painter =
        rememberAsyncImagePainter(ImageRequest.Builder(LocalContext.current).data(data = discount.imgUrl).apply(block = fun ImageRequest.Builder.() {
            crossfade(true)
            placeholder(R.drawable.image_placeholder)
        }).build())

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(4.dp),
        elevation = 2.dp,
        color = cardColor,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
        ) {
            Image(
                modifier = Modifier.size(120.dp),
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier
                    .weight(1F, true)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = discount.name,
                        style = MaterialTheme.typography.body1,
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        softWrap = false,
                        color = textColor,
                    )
                    if (discount is GameDiscount) {
                        if (discount.isPs4) {
                            SimpleChip(
                                label = "PS4",
                                elevation = 0.dp,
                                size = ChipSize.SMALL
                            )
                        }
                        if (discount.isPs5) {
                            SimpleChip(
                                label = "PS5",
                                elevation = 0.dp,
                                size = ChipSize.SMALL
                            )
                        }
                    } else {
                        SimpleChip(
                            label = "DLC",
                            elevation = 0.dp,
                            size = ChipSize.SMALL
                        )
                    }
                }
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

private val tempGameDiscount = GameDiscount(
    id = 1L,
    ppId = 1L,
    saleDbId = 1L,
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

@ExperimentalCoilApi
@Preview
@Composable
fun GameDiscountCardPreview() {
    PlatsNPricesTheme {
        DiscountCard(tempGameDiscount)
    }
}

@ExperimentalCoilApi
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun GameDiscountCardPreviewDark() {
    PlatsNPricesTheme {
        DiscountCard(tempGameDiscount)
    }
}

@ExperimentalCoilApi
@Preview(widthDp = 320)
@Composable
fun GameDiscountCardSmallPreview() {
    PlatsNPricesTheme {
        DiscountCard(tempGameDiscount)
    }
}