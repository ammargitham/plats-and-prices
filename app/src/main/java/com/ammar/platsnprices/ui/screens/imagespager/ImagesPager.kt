package com.ammar.platsnprices.ui.screens.imagespager

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ammar.platsnprices.ui.controllers.ToolbarController
import com.ammar.platsnprices.ui.controllers.ToolbarState
import com.ammar.platsnprices.ui.composables.ImagesPager as ImagesPagerContent

@Composable
fun ImagesPager(
    toolbarController: ToolbarController,
    urls: List<String>,
    initialIndex: Int,
) {
    LaunchedEffect(Unit) {
        toolbarController.updateToolbar(
            ToolbarState(
                title = {},
                backgroundColor = Color.Transparent,
                elevation = 0.dp,
            )
        )
    }

    ImagesPagerContent(
        modifier = Modifier.fillMaxSize(),
        urls = urls,
        initialPage = initialIndex
    )
}
