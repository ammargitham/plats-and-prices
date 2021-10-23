package com.ammar.platsnprices.ui.screens.product

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.ammar.platsnprices.ui.controllers.ToolbarController
import com.ammar.platsnprices.ui.controllers.ToolbarState

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalUnitApi
@ExperimentalCoilApi
@Composable
fun Product(
    toolbarController: ToolbarController,
    ppId: Long?,
    name: String?,
    imgUrl: String?,
    navigateToImagesPager: (urls: List<String>, index: Int) -> Unit,
    navigateToVideoPlayer: (url: String) -> Unit,
) {
    val viewModel: ProductViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(ppId) {
        val product = uiState.product
        if (product != null && product.ppId == ppId) {
            return@LaunchedEffect
        }
        viewModel.getProduct(ppId ?: return@LaunchedEffect)
    }

    LaunchedEffect(Unit) {
        toolbarController.updateToolbar(
            ToolbarState(
                title = {},
                backgroundColor = Color.Transparent,
                elevation = 0.dp,
            )
        )
    }

    ProductContent(
        loading = uiState.loading,
        name = name,
        imgUrl = imgUrl,
        product = uiState.product,
        openCriticLoading = uiState.openCriticLoading,
        openCriticGame = uiState.openCriticGame,
        onScreenshotClick = {
            navigateToImagesPager(uiState.product?.screenShotUrls ?: emptyList(), it)
        },
        onVideoClick = {
            uiState.product?.previewVideoUrl?.let { navigateToVideoPlayer(it) }
        }
    )
}