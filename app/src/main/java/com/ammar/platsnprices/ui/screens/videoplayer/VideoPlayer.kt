package com.ammar.platsnprices.ui.screens.videoplayer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ammar.platsnprices.ui.controllers.ToolbarController
import com.ammar.platsnprices.ui.controllers.ToolbarState
import com.ammar.platsnprices.ui.composables.VideoPlayer as VideoPlayerContent

@Composable
fun VideoPlayer(
    toolbarController: ToolbarController,
    url: String,
    setOrientation: ((Int) -> Unit)? = null,
    setImmersive: ((Boolean) -> Unit)? = null,
) {
    var toolbarState by remember {
        mutableStateOf(
            ToolbarState(
                title = {},
                backgroundColor = Color.Transparent,
                elevation = 0.dp,
            )
        )
    }

    LaunchedEffect(toolbarState) {
        toolbarController.updateToolbar(toolbarState)
    }

    VideoPlayerContent(
        url = url,
        setOrientation = setOrientation,
        setImmersive = {
            toolbarState = toolbarState.copy(visible = !it)
            setImmersive?.invoke(it)
        },
    )
}