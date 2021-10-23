package com.ammar.platsnprices.utils

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.staticCompositionLocalOf

val LocalBackPressedDispatcher = staticCompositionLocalOf<OnBackPressedDispatcher> { error("No Back Dispatcher provided") }
