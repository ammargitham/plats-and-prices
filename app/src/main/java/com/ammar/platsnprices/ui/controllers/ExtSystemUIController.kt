package com.ammar.platsnprices.ui.controllers

import android.view.View
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

abstract class ExtSystemUIController constructor(
    private val activity: ComponentActivity,
    private val window: Window,
    private val rootView: View,
) {
    fun setOrientation(orientation: Int) {
        activity.requestedOrientation = orientation
    }

    fun setImmersive(immersive: Boolean) {
        WindowCompat.setDecorFitsSystemWindows(window, !immersive)
        WindowInsetsControllerCompat(window, rootView).run {
            if (immersive) {
                hide(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            } else {
                show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }
}

class DefaultExtSystemUIController(
    activity: ComponentActivity,
    window: Window,
    rootView: View,
) : ExtSystemUIController(activity, window, rootView)

@Composable
fun rememberExtSystemUIController(
    activity: ComponentActivity,
    window: Window,
    rootView: View,
): DefaultExtSystemUIController = remember(activity) { DefaultExtSystemUIController(activity, window, rootView) }
