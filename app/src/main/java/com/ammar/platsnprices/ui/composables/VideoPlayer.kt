package com.ammar.platsnprices.ui.composables

import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.ammar.platsnprices.R
import com.ammar.platsnprices.utils.getExoPlayerDataSourceFactory
import com.ammar.platsnprices.utils.inflate
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView

@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    url: String,
    setOrientation: ((Int) -> Unit)? = null,
    setImmersive: ((Boolean) -> Unit)? = null,
) {
    val context = LocalContext.current
    var isPlayWhenReady by rememberSaveable { mutableStateOf(true) }
    var position by rememberSaveable { mutableStateOf(0L) }
    var isFullScreen by rememberSaveable { mutableStateOf(false) }
    val exoPlayer = remember { SimpleExoPlayer.Builder(context).build() }

    fun toggleFullScreen() {
        isFullScreen = !isFullScreen
        setOrientation?.invoke(if (isFullScreen) SCREEN_ORIENTATION_SENSOR_LANDSCAPE else SCREEN_ORIENTATION_USER)
    }

    BackHandler(enabled = isFullScreen) { toggleFullScreen() }

    LaunchedEffect(url) {
        exoPlayer.apply {
            val source = ProgressiveMediaSource
                .Factory(context.getExoPlayerDataSourceFactory())
                .createMediaSource(MediaItem.fromUri(url))
            setMediaSource(source)
            playWhenReady = isPlayWhenReady
            seekTo(position)
            prepare()
        }
    }

    LaunchedEffect(Unit) {
        setImmersive?.invoke(isFullScreen)
    }

    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> exoPlayer.pause()
                Lifecycle.Event.ON_RESUME -> exoPlayer.play()
                Lifecycle.Event.ON_DESTROY -> exoPlayer.run {
                    position = exoPlayer.currentPosition
                    stop()
                    release()
                }
                else -> Unit
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { ctx ->
            val playerView = ctx.inflate<PlayerView>(R.layout.layout_video_player).apply {
                player = exoPlayer
                controllerShowTimeoutMs = 1500
            }
            val controllerView = playerView.findViewById<FrameLayout>(R.id.controller_view_parent)

            // play pause
            val playPause = controllerView.findViewById<ImageButton>(R.id.exo_play_pause)
            setPlayPauseImage(playPause, isPlayWhenReady)
            exoPlayer.addListener(object : Player.Listener {
                override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {
                    isPlayWhenReady = playWhenReady
                    setPlayPauseImage(playPause, playWhenReady)
                }
            })
            playPause.setOnClickListener { exoPlayer.playWhenReady = !exoPlayer.playWhenReady }

            // full screen
            if (setOrientation != null) {
                val fullscreenButton = controllerView.findViewById<ImageButton>(R.id.exo_fullscreen)
                setFullscreenButtonImage(fullscreenButton, isFullScreen)
                fullscreenButton.setOnClickListener { toggleFullScreen() }
            }
            playerView
        },
    )
}

fun setFullscreenButtonImage(fullscreenButton: ImageButton, isFullScreen: Boolean) {
    fullscreenButton.setImageResource(if (isFullScreen) R.drawable.exo_controls_fullscreen_exit else R.drawable.exo_controls_fullscreen_enter)
}

private fun setPlayPauseImage(playPause: ImageButton, playWhenReady: Boolean) {
    playPause.setImageResource(if (playWhenReady) R.drawable.exo_controls_pause else R.drawable.exo_controls_play)
}