package com.ammar.platsnprices.ui.composables

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Build
import android.util.Log
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.text.HtmlCompat
import com.ammar.platsnprices.R
import com.ammar.platsnprices.ui.theme.PlatsNPricesTheme

enum class ChipSize {
    SMALLER,
    SMALL,
    REGULAR,
}

@Composable
fun SimpleChip(
    modifier: Modifier = Modifier,
    size: ChipSize = ChipSize.REGULAR,
    label: String,
    icon: ImageVector? = null,
    iconContentDescription: String? = null,
    elevation: Dp = 1.dp,
    cornerRadius: CornerSize = CornerSize(4.dp),
    backgroundColor: Color = Color.LightGray,
    textColor: Color = Color.DarkGray,
    fontWeight: FontWeight? = null,
) {
    val textStyle = when (size) {
        ChipSize.SMALLER -> MaterialTheme.typography.caption.copy(fontSize = 10.sp)
        ChipSize.SMALL -> MaterialTheme.typography.caption
        ChipSize.REGULAR -> MaterialTheme.typography.body2
    }
    val textPadding = when (size) {
        ChipSize.SMALLER -> 2.dp
        ChipSize.SMALL -> 4.dp
        ChipSize.REGULAR -> 8.dp
    }

    Box(modifier = modifier) {
        Surface(
            elevation = elevation,
            shape = RoundedCornerShape(cornerRadius),
            color = backgroundColor
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Icon(
                        icon,
                        iconContentDescription,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = 4.dp)
                    )
                }
                Text(
                    label,
                    modifier = Modifier.padding(6.dp, textPadding),
                    style = textStyle.copy(color = textColor),
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                    softWrap = false,
                    fontWeight = fontWeight
                )
            }
        }
    }
}

@Composable
@Preview
fun PreviewSimpleChip() {
    SimpleChip(label = "Chip")
}

@Composable
@Preview
fun PreviewSimpleChipRounderColorful() {
    SimpleChip(
        label = "Chip",
        cornerRadius = CornerSize(16.dp),
        backgroundColor = Color.Yellow,
        textColor = Color.Red,
    )
}

@Composable
@Preview
fun PreviewSimpleChipSmall() {
    SimpleChip(label = "Chip", size = ChipSize.SMALL)
}

@Composable
@Preview
fun PreviewSimpleChipSmaller() {
    SimpleChip(label = "Chip", size = ChipSize.SMALLER)
}

data class ButtonToggleOption<T>(
    val value: T,
    val content: @Composable RowScope.() -> Unit,
)

@Composable
fun <T> ButtonToggleGroup(
    modifier: Modifier = Modifier,
    options: List<ButtonToggleOption<T>>,
    valueChecked: T,
    tint: Color = MaterialTheme.colors.onSurface,
    tintChecked: Color = MaterialTheme.colors.primary,
    cornerRadius: Dp = 4.dp,
    enabled: Boolean = true,
    onCheckedChange: ((T) -> Unit)? = null,
) {
    try {
        require(options.size > 1) { "IconToggleButtonGroup requires at-least 2 options" }
    } catch (e: Exception) {
        Log.w("IconToggleButtonGroup", "", e)
    }

    Row(
        modifier = modifier
    ) {
        options.mapIndexed { index, option ->
            val isChecked = valueChecked == option.value
            val tintState by animateColorAsState(
                if (isChecked) {
                    if (!enabled) tintChecked.copy(alpha = 0.5f) else tintChecked
                } else tint
            )

            val optionModifier = when (index) {
                0 -> {
                    if (isChecked) {
                        Modifier
                            .offset(0.dp, 0.dp)
                            .zIndex(1f)
                    } else {
                        Modifier
                            .offset(0.dp, 0.dp)
                            .zIndex(0f)
                    }
                }
                else -> {
                    val offset = -1 * index
                    if (isChecked) {
                        Modifier
                            .offset(offset.dp, 0.dp)
                            .zIndex(1f)
                    } else {
                        Modifier
                            .offset(offset.dp, 0.dp)
                            .zIndex(0f)
                    }
                }
            }

            val shape = when (index) {
                // left outer button
                0 -> RoundedCornerShape(
                    topStart = cornerRadius,
                    topEnd = 0.dp,
                    bottomStart = cornerRadius,
                    bottomEnd = 0.dp
                )
                // right outer button
                options.size - 1 -> RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = cornerRadius,
                    bottomStart = 0.dp,
                    bottomEnd = cornerRadius
                )
                // middle button
                else -> RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                )
            }

            val border = if (isChecked) {
                BorderStroke(
                    width = 1.dp,
                    color = tintState
                )
            } else {
                ButtonDefaults.outlinedBorder
            }

            val colors = if (isChecked) {
                // selected colors
                ButtonDefaults.outlinedButtonColors(
                    backgroundColor = tintState.copy(alpha = 0.1f),
                    contentColor = tintState
                )
            } else {
                // not selected colors
                ButtonDefaults.outlinedButtonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = tint
                )
            }

            OutlinedButton(
                modifier = optionModifier,
                shape = shape,
                border = border,
                colors = colors,
                enabled = enabled,
                onClick = { onCheckedChange?.invoke(option.value) },
            ) {
                option.content(this)
            }
        }
    }
}

private val tempToggleOptions: List<ButtonToggleOption<String>> = listOf(
    ButtonToggleOption(
        "First",
        {
            Icon(painter = painterResource(R.drawable.ic_round_view_list_24), contentDescription = "")
            Text(text = "First")
        },
    ),
    ButtonToggleOption(
        "Second",
        {
            Icon(painter = painterResource(R.drawable.ic_round_view_list_24), contentDescription = "")
            Text(text = "Second")
        },
    ),
    ButtonToggleOption(
        "Third",
        {
            Icon(painter = painterResource(R.drawable.ic_round_view_list_24), contentDescription = "")
        },
    ),
    ButtonToggleOption(
        "Fourth",
        {
            Icon(painter = painterResource(R.drawable.ic_round_view_list_24), contentDescription = "")
        },
    )
)

@Composable
@Preview(showBackground = true)
fun PreviewButtonToggleGroup() {
    PlatsNPricesTheme {
        ButtonToggleGroup(options = tempToggleOptions, valueChecked = "First")
    }
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun PreviewButtonToggleGroupDark() {
    PlatsNPricesTheme {
        ButtonToggleGroup(options = tempToggleOptions, valueChecked = "First")
    }
}

@Composable
fun ClickableChip(
    tint: Color = MaterialTheme.colors.onSurface,
    tintActive: Color = MaterialTheme.colors.primary,
    isActive: Boolean = false,
    shape: Shape = RoundedCornerShape(18.dp),
    onClick: () -> Unit = {},
    content: @Composable RowScope.() -> Unit,
) {
    val tintState by animateColorAsState(if (isActive) tintActive else tint)

    val colors = if (isActive) {
        ButtonDefaults.outlinedButtonColors(
            backgroundColor = tintState.copy(alpha = 0.1f),
            contentColor = tintState
        )
    } else {
        ButtonDefaults.outlinedButtonColors(
            backgroundColor = Color.Transparent,
            contentColor = tint
        )
    }

    val border = if (isActive) {
        BorderStroke(
            width = 1.dp,
            color = tintState
        )
    } else {
        ButtonDefaults.outlinedBorder
    }

    OutlinedButton(
        border = border,
        shape = shape,
        colors = colors,
        onClick = onClick,
    ) {
        content(this)
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewClickableChip() {
    val label = "Click here"
    ClickableChip {
        Icon(
            painterResource(id = R.drawable.ic_round_filter_alt_24),
            contentDescription = label
        )
        Text(text = label)
    }
}

@Composable
fun HtmlText(
    modifier: Modifier = Modifier,
    text: String,
    @StyleRes textAppearanceResId: Int = android.R.style.TextAppearance_Material_Body1,
    maxLines: Int = Int.MAX_VALUE,
    onPostUpdate: ((TextView) -> Unit)? = null,
) {
    // Remembers the HTML formatted description. Re-executes on a new description
    val htmlText = remember(text) {
        HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }
    AndroidView(
        modifier = modifier,
        factory = {
            TextView(it).apply {
                if (Build.VERSION.SDK_INT < 23) {
                    @Suppress("DEPRECATION")
                    setTextAppearance(context, textAppearanceResId)
                } else {
                    setTextAppearance(textAppearanceResId)
                }

            }
        },
        update = {
            it.text = htmlText
            it.maxLines = maxLines
            if (onPostUpdate != null) {
                it.post { onPostUpdate(it) }
            }
        }
    )
}
