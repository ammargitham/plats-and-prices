package com.ammar.platsnprices.ui.composables

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import com.ammar.platsnprices.utils.LocalBackPressedDispatcher

/**
 * From https://developer.android.com/jetpack/compose/side-effects#disposableeffect
 */
@Composable
fun BackHandler(
    backDispatcher: OnBackPressedDispatcher = LocalBackPressedDispatcher.current,
    enabled: Boolean = true, // Whether back events should be intercepted or not
    onBack: () -> Unit
) {
    // Safely update the current `onBack` lambda when a new one is provided
    val currentOnBack by rememberUpdatedState(onBack)

    // Remember in Composition a back callback that calls the `onBack` lambda
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }

    // On every successful composition, update the callback with the `enabled` value
    // to tell `backCallback` whether back events should be intercepted or not
    SideEffect {
        backCallback.isEnabled = enabled
    }

    // If `backDispatcher` changes, dispose and reset the effect
    DisposableEffect(backDispatcher) {
        // Add callback to the backDispatcher
        backDispatcher.addCallback(backCallback)

        // When the effect leaves the Composition, remove the callback
        onDispose {
            backCallback.remove()
        }
    }
}
