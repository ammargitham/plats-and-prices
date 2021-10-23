package com.ammar.platsnprices.ui.screens.product

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ammar.platsnprices.data.entities.OpenCriticGame
import com.ammar.platsnprices.data.entities.Product
import com.ammar.platsnprices.data.repositories.OpenCriticRepository
import com.ammar.platsnprices.data.repositories.PlatPricesRepository
import com.ammar.platsnprices.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val platPricesRepository: PlatPricesRepository,
    private val openCriticRepository: OpenCriticRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState(loading = true))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun getProduct(ppId: Long) {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            try {
                val product = platPricesRepository.getProductByPpId(ppId)
                _uiState.update { it.copy(loading = false, product = product) }
                if (product.openCriticId > 0) {
                    _uiState.update { it.copy(openCriticLoading = true) }
                    val openCriticGame = openCriticRepository.getGameData(product.openCriticId)
                    _uiState.update { it.copy(openCriticLoading = false, openCriticGame = openCriticGame) }
                }
            } catch (e: Exception) {
                Log.e(TAG, "getProduct: ", e)
            }
        }
    }
}

data class UiState(
    val product: Product? = null,
    val loading: Boolean = false,
    val openCriticGame: OpenCriticGame? = null,
    val openCriticLoading: Boolean = false,
) {
    val isInitialLoad: Boolean
        get() = product == null && loading
}