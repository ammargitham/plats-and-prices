package com.ammar.platsnprices.ui.composables.discount

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ammar.platsnprices.R
import com.ammar.platsnprices.data.entities.GameDiscount
import com.ammar.platsnprices.ui.composables.ChipSize
import com.ammar.platsnprices.ui.composables.SimpleChip
import com.ammar.platsnprices.ui.theme.PSPlus
import com.ammar.platsnprices.ui.theme.PlatsNPricesTheme
import java.time.LocalDateTime

@Composable
fun DiscountPrice(
    salePrice: String,
    salePct: Int,
    isPSPlus: Boolean = false,
) {
    val textColor = remember {
        if (isPSPlus) {
            PSPlus
        } else {
            Color.Unspecified
        }
    }
    val chipBackgroundColor = remember {
        if (isPSPlus) {
            PSPlus
        } else {
            Color(0xFF3F51B5)
        }
    }
    val chipTextColor = remember {
        if (isPSPlus) {
            Color.Black
        } else {
            Color.White
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = salePrice,
            style = MaterialTheme.typography.subtitle2,
            fontWeight = FontWeight.Bold,
            color = textColor,
        )
        SimpleChip(
            label = "${if (salePct > 0) "-" else ""}$salePct%",
            size = ChipSize.SMALL,
            elevation = 0.dp,
            backgroundColor = chipBackgroundColor,
            textColor = chipTextColor,
            fontWeight = FontWeight.Bold,
        )
        if (isPSPlus) {
            Image(
                modifier = Modifier.size(14.dp),
                painter = painterResource(id = R.drawable.ps_plus),
                contentDescription = stringResource(R.string.ps_plus),
            )
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
@Preview(showBackground = true)
fun DiscountPricePreview() {
    PlatsNPricesTheme {
        DiscountPrice(
            tempGameDiscount.formattedSalePrice,
            tempGameDiscount.saleDiscountPct,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun DiscountPricePreviewPlus() {
    PlatsNPricesTheme {
        DiscountPrice(
            tempGameDiscount.formattedPlusPrice,
            tempGameDiscount.plusDiscountPct,
            true,
        )
    }
}
