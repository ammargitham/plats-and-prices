package com.ammar.platsnprices.ui.composables

import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.annotation.ExperimentalCoilApi
import com.ammar.platsnprices.ui.screens.product.tempGame
import com.ammar.platsnprices.ui.theme.PlatsNPricesTheme
import com.ammar.platsnprices.ui.views.ZoomableImageView
import com.ammar.platsnprices.ui.views.adapters.ZoomableImageViewHolder
import com.ammar.platsnprices.ui.views.adapters.ZoomableImagesPagerAdapter
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalComposeUiApi
@ExperimentalCoilApi
@Composable
fun ImagesPager(
    modifier: Modifier = Modifier,
    urls: List<String>,
    initialPage: Int = 0
) {
    var pager: ViewPager2? by remember { mutableStateOf(null) }
    var currentImageView: ZoomableImageView? by remember { mutableStateOf(null) }

    AndroidView(
        modifier = modifier
            .fillMaxSize()
            .pointerInteropFilter {
                when (currentImageView?.isZooming) {
                    true -> {
                        // dispatch event to image only
                        currentImageView?.dispatchTouchEvent(it)
                        return@pointerInteropFilter true
                    }
                    else -> false
                }
            },
        factory = {
            ViewPager2(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                adapter = ZoomableImagesPagerAdapter(urls)
                setCurrentItem(initialPage, false)
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        val recyclerView = pager?.get(0) as? RecyclerView ?: return
                        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position) as? ZoomableImageViewHolder ?: return
                        currentImageView = viewHolder.itemView as ZoomableImageView
                    }
                })
            }.also { viewPager -> pager = viewPager }
        },
        update = {}
    )
}

@ExperimentalComposeUiApi
@ExperimentalCoilApi
@ExperimentalPagerApi
@Composable
@Preview
fun ImagesPagerPreview() {
    PlatsNPricesTheme {
        ImagesPager(urls = tempGame.screenShotUrls)
    }
}