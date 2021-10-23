package com.ammar.platsnprices.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.Normalizer
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeParseException
import java.util.*
import kotlin.math.*

val Any.TAG: String
    get() {
        return if (!javaClass.isAnonymousClass) {
            val name = javaClass.simpleName
            if (name.length <= 23) name else name.substring(0, 23) // first 23 chars
        } else {
            val name = javaClass.name
            if (name.length <= 23) name else name.substring(name.length - 23, name.length) // last 23 chars
        }
    }

@Composable
fun Int.dpToPx(): Float = with(LocalDensity.current) { this@dpToPx.dp.toPx() }

@Composable
fun Int.pxToDp(): Dp = with(LocalDensity.current) { this@pxToDp.toDp() }

@Composable
fun Float.pxToDp(): Dp = with(LocalDensity.current) { this@pxToDp.toDp() }

fun Float.format(maxFractionDigits: Int = 1): String {
    val df: DecimalFormat = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH)).also {
        it.maximumFractionDigits = maxFractionDigits
    }
    return df.format(this)
}

fun String.trimAll() = this.trim { it <= ' ' }

fun String.slugify(replacement: String = "-") = Normalizer
    .normalize(this, Normalizer.Form.NFD)
    .replace("[^\\p{ASCII}]".toRegex(), "")
    .replace("[^a-zA-Z0-9\\s]+".toRegex(), "").trim()
    .replace("\\s+".toRegex(), replacement)
    .lowercase()

fun String.parsePlatPricesDateTime(): LocalDateTime? = try {
    LocalDateTime.parse(this, pnpDateTimeFormatter)
} catch (e: DateTimeParseException) {
    Log.e(TAG, "Error parsing date time", e)
    null
}

fun Context.openUrl(url: String, singleInstance: Boolean = false) {
    val customTabsIntent = CustomTabsIntent.Builder().build()
    if (singleInstance) {
        customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }
    customTabsIntent.launchUrl(this, Uri.parse(url))
}

@Suppress("UNCHECKED_CAST")
fun <T> Context.inflate(@LayoutRes layout: Int): T where T : View = LayoutInflater
    .from(this)
    .inflate(layout, null, false) as T

fun Context.getExoPlayerDataSourceFactory(): DataSource.Factory {
    val dataSourceFactory = DefaultDataSourceFactory(this)
    val cache = ExoPlayerCache.getInstance(this)
    return CacheDataSource.Factory().apply {
        setCache(cache)
        setUpstreamDataSourceFactory(dataSourceFactory)
    }
}

fun Modifier.gradientBackground(colors: List<Color>, angle: Float) = this.then(
    Modifier.drawBehind {
        val angleRad = angle / 180f * PI
        val x = cos(angleRad).toFloat() //Fractional x
        val y = sin(angleRad).toFloat() //Fractional y

        val radius = sqrt(size.width.pow(2) + size.height.pow(2)) / 2f
        val offset = center + Offset(x * radius, y * radius)

        val exactOffset = Offset(
            x = offset.x.coerceIn(0f, size.width),
            y = size.height - offset.y.coerceIn(0f, size.height)
        )

        drawRect(
            brush = Brush.linearGradient(
                colors = colors,
                start = Offset(size.width, size.height) - exactOffset,
                end = exactOffset
            ),
            size = size
        )
    }
)

fun LocalDate.toDate(): Date = Date.from(
    this.atStartOfDay()
        .atZone(ZoneId.systemDefault())
        .toInstant()
)

/**
 * Returns a [StateFlow] that access data associated with the given key.
 *
 * @param scope The scope used to synchronize the [StateFlow] and [SavedStateHandle]
 * @param key The identifier for the value
 * @param initialValue If no value exists with the given [key], a new one is created
 *  with the given [initialValue].
 *
 * @see SavedStateHandle.getLiveData
 */
fun <T> SavedStateHandle.getStateFlow(
    scope: CoroutineScope,
    key: String,
    initialValue: T,
): MutableStateFlow<T> {
    val liveData = getLiveData(key, initialValue)
    val stateFlow = MutableStateFlow(initialValue)

    // Synchronize the LiveData with the StateFlow
    val observer = Observer<T> { value ->
        if (stateFlow.value != value) {
            stateFlow.value = value
        }
    }
    liveData.observeForever(observer)

    stateFlow.onCompletion {
        // Stop observing the LiveData if the StateFlow completes
        withContext(Dispatchers.Main.immediate) {
            liveData.removeObserver(observer)
        }
    }.onEach { value ->
        // Synchronize the StateFlow with the LiveData
        withContext(Dispatchers.Main.immediate) {
            if (liveData.value != value) {
                liveData.value = value
            }
        }
    }.launchIn(scope)

    return stateFlow
}