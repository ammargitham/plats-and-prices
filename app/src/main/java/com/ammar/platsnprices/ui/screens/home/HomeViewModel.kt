package com.ammar.platsnprices.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ammar.platsnprices.data.entities.RecentGameDiscount
import com.ammar.platsnprices.data.entities.Region
import com.ammar.platsnprices.data.entities.Resource
import com.ammar.platsnprices.data.entities.Sale
import com.ammar.platsnprices.data.repositories.AppPreferencesRepository
import com.ammar.platsnprices.data.repositories.PlatPricesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val platPricesRepository: PlatPricesRepository,
    private val appPreferencesRepository: AppPreferencesRepository,
) : ViewModel() {
    private val appPreferencesFlow = appPreferencesRepository.appPreferencesFlow

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState(
            sales = Resource.Loading(emptyList()),
            recentDiscounts = Resource.Loading(emptyList()),
        )
    )
    val uiState: StateFlow<UiState> = _uiState

    init {
        viewModelScope.launch {
            appPreferencesFlow.distinctUntilChanged().collectLatest { prefs ->
                _uiState.update { it.copy(region = prefs.region) }
                combine(
                    platPricesRepository.getSales(prefs.region),
                    platPricesRepository.getRecentDiscounts(prefs.region)
                ) { sales, discounts ->
                    _uiState.update { it.copy(sales = sales, recentDiscounts = discounts) }
                }.collect()
            }
        }
    }

    fun updateRegion(region: Region) = viewModelScope.launch {
        appPreferencesRepository.updateRegion(region)
    }
}

data class UiState(
    val sales: Resource<List<Sale>> = Resource.Success(emptyList()),
    val recentDiscounts: Resource<List<RecentGameDiscount>> = Resource.Success(emptyList()),
    val region: Region? = null,
)