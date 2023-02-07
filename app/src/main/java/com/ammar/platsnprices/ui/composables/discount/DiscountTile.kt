package com.ammar.platsnprices.ui.composables.discount

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.ammar.platsnprices.R
import com.ammar.platsnprices.data.entities.Discount
import com.ammar.platsnprices.data.entities.GameDiscount
import com.ammar.platsnprices.ui.composables.ChipSize
import com.ammar.platsnprices.ui.composables.SimpleChip
import com.ammar.platsnprices.ui.theme.PlatsNPricesTheme
import java.time.LocalDateTime

@Composable
fun DiscountTile(
    modifier: Modifier = Modifier,
    discount: Discount,
    onClick: () -> Unit = {}
) {
    val painter =
        rememberAsyncImagePainter(ImageRequest.Builder(LocalContext.current).data(data = discount.imgUrl).apply(block = fun ImageRequest.Builder.() {
            crossfade(true)
            placeholder(R.drawable.image_placeholder)
        }).build())

    Surface(
        modifier = Modifier
            .clickable(onClick = onClick)
            .then(modifier),
        shape = RoundedCornerShape(4.dp),
        elevation = 2.dp,
        color = MaterialTheme.colors.surface,
    ) {
        Column {
            Box {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
                Column(
                    modifier = Modifier.padding(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
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
            }
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = discount.name,
                    style = MaterialTheme.typography.body1,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
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
                if (discount.salePrice != discount.plusPrice) {
                    DiscountPrice(
                        discount.formattedPlusPrice,
                        discount.saleDiscountPct,
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

@Composable
@Preview(widthDp = 150)
@Preview(widthDp = 150, uiMode = UI_MODE_NIGHT_YES)
fun GameDiscountTilePreview() {
    PlatsNPricesTheme {
        DiscountTile(discount = tempGameDiscount)
    }
}

@Composable
@Preview(widthDp = 120)
@Preview(widthDp = 120, uiMode = UI_MODE_NIGHT_YES)
fun GameDiscountTilePreviewNarrow() {
    PlatsNPricesTheme {
        DiscountTile(discount = tempGameDiscount)
    }
}