package com.ammar.platsnprices.ui.controllers

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

abstract class ToolbarController(val state: ToolbarState) {
    abstract fun updateToolbar(newState: ToolbarState = ToolbarState())
}

data class ToolbarState(
    val title: (@Composable () -> Unit)? = null,
    val backgroundColor: Color? = null,
    val elevation: Dp? = null,
    val actions: (@Composable (RowScope.() -> Unit))? = null,
    val visible: Boolean = true,
)

class DefaultToolbarController(state: ToolbarState) : ToolbarController(state) {
    private var _title: MutableState<@Composable (() -> Unit)?> = mutableStateOf(null)
    val title: State<(@Composable () -> Unit)?> = _title

    private var _backgroundColor: MutableState<Color?> = mutableStateOf(null)
    val backgroundColor: State<Color?> = _backgroundColor

    private var _elevation: MutableState<Dp?> = mutableStateOf(null)
    val elevation: State<Dp?> = _elevation

    private var _actions: MutableState<(@Composable RowScope.() -> Unit)?> = mutableStateOf(null)
    val actions: State<(@Composable RowScope.() -> Unit)?> = _actions

    private var _visible: MutableState<Boolean> = mutableStateOf(true)
    val visible: State<Boolean> = _visible

    override fun updateToolbar(newState: ToolbarState) {
        _title.value = newState.title
        _backgroundColor.value = newState.backgroundColor
        _elevation.value = newState.elevation
        _actions.value = newState.actions
        _visible.value = newState.visible
        // state.update(newState)
    }
}

@Composable
fun rememberToolbarController(
    initialState: ToolbarState = ToolbarState()
): DefaultToolbarController = remember { DefaultToolbarController(initialState) }
