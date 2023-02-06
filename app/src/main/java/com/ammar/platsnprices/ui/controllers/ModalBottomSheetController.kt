package com.ammar.platsnprices.ui.controllers

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@ExperimentalMaterialApi
class BottomSheetCallback {
    private var onHide: (() -> Unit)? = null
    private var onExpand: (() -> Unit)? = null
    private var onHalfExpand: (() -> Unit)? = null

    fun performStateChange(value: ModalBottomSheetValue) {
        when (value) {
            ModalBottomSheetValue.Hidden -> onHide?.invoke()
            ModalBottomSheetValue.Expanded -> onExpand?.invoke()
            ModalBottomSheetValue.HalfExpanded -> onHalfExpand?.invoke()
        }
    }

    fun setCallbacks(
        onHide: (() -> Unit)?,
        onExpand: (() -> Unit)?,
        onHalfExpand: (() -> Unit)?,
    ) {
        this.onHide = onHide
        this.onExpand = onExpand
        this.onHalfExpand = onHalfExpand
    }
}

@ExperimentalMaterialApi
abstract class ModalBottomSheetController(private val sheetState: ModalBottomSheetState) {
    abstract fun setContent(sheetContent: (@Composable (ColumnScope.() -> Unit))?)

    abstract fun setCallbacks(
        onHide: (() -> Unit)? = null,
        onExpand: (() -> Unit)? = null,
        onHalfExpand: (() -> Unit)? = null,
    )

    suspend fun hide() = sheetState.hide()

    suspend fun show() = sheetState.show()
}

@ExperimentalMaterialApi
class DefaultModalBottomSheetController(
    sheetState: ModalBottomSheetState,
    private val bottomSheetCallback: BottomSheetCallback
) : ModalBottomSheetController(sheetState) {

    private var _modalBottomSheetContent: MutableState<@Composable (ColumnScope.() -> Unit)> = mutableStateOf({ Text(text = "") })
    val modalBottomSheetContent: State<@Composable (ColumnScope.() -> Unit)> = _modalBottomSheetContent

    override fun setContent(sheetContent: (@Composable ColumnScope.() -> Unit)?) {
        _modalBottomSheetContent.value = sheetContent ?: { Text(text = "") }
    }

    override fun setCallbacks(
        onHide: (() -> Unit)?,
        onExpand: (() -> Unit)?,
        onHalfExpand: (() -> Unit)?,
    ) = bottomSheetCallback.setCallbacks(onHide, onExpand, onHalfExpand)
}

@ExperimentalMaterialApi
@Composable
fun rememberModalBottomSheetController(
    sheetState: ModalBottomSheetState,
    bottomSheetCallback: BottomSheetCallback
): DefaultModalBottomSheetController = remember(sheetState) { DefaultModalBottomSheetController(sheetState, bottomSheetCallback) }