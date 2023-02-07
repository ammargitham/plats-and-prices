package com.ammar.platsnprices.ui.screens.sale

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ammar.platsnprices.data.entities.AppPreferences
import com.ammar.platsnprices.data.entities.Discount
import com.ammar.platsnprices.data.entities.DlcDiscount
import com.ammar.platsnprices.data.entities.GameDiscount
import com.ammar.platsnprices.data.entities.Region
import com.ammar.platsnprices.data.entities.Resource
import com.ammar.platsnprices.data.entities.SaleWithDiscounts
import com.ammar.platsnprices.data.entities.successOr
import com.ammar.platsnprices.data.repositories.AppPreferencesRepository
import com.ammar.platsnprices.data.repositories.PlatPricesRepository
import com.ammar.platsnprices.utils.getStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SaleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val platPricesRepository: PlatPricesRepository,
    private val appPreferencesRepository: AppPreferencesRepository,
) : ViewModel() {
    private val appPreferencesFlow = appPreferencesRepository.appPreferencesFlow

    @OptIn(ExperimentalCoroutinesApi::class)
    val saleWithDiscounts: Flow<Resource<SaleWithDiscounts?>> = savedStateHandle.getStateFlow<Long?>(
        viewModelScope,
        "saleDbId",
        null
    ).flatMapLatest {
        val saleDbId = it ?: return@flatMapLatest emptyFlow()
        platPricesRepository.getSaleWithDiscounts(saleDbId)
    }

    val uiState: Flow<UiState> = combine(
        saleWithDiscounts,
        appPreferencesFlow
    ) { saleWithDiscountsResource: Resource<SaleWithDiscounts?>, appPreferences: AppPreferences ->
        val s = saleWithDiscountsResource.successOr(null)
        val sort = appPreferences.discountsSort
        val filters = appPreferences.discountFilters
        val discounts: List<Discount> = (s?.gameDiscounts ?: emptyList()) + (s?.dlcDiscounts ?: emptyList())
        UiState(
            loading = saleWithDiscountsResource is Resource.Loading,
            saleDbId = s?.sale?.id,
            saleName = s?.sale?.name ?: "",
            saleEndTime = s?.sale?.endTime,
            saleRegion = s?.sale?.region ?: Region.US,
            discounts = applyFiltersAndSort(discounts, filters, sort),
            listMode = appPreferences.discountsListMode,
            filters = filters,
            sort = sort,
        )
    }

    fun updateListMode(mode: ListMode) = viewModelScope.launch {
        appPreferencesRepository.updateDiscountsListMode(mode)
    }

    fun updateFilters(filters: Filters) = viewModelScope.launch {
        appPreferencesRepository.updateDiscountFilters(filters)
    }

    fun updateSort(sort: Sort) = viewModelScope.launch {
        appPreferencesRepository.updateDiscountsSort(sort)
    }

    private fun applyFiltersAndSort(
        discounts: List<Discount>,
        filters: Filters,
        sort: Sort,
    ): List<Discount> {
        var seq = discounts.asSequence()
        if (filters.type != Type.ALL) {
            seq = seq.filter { if (filters.type == Type.GAME) it is GameDiscount else it is DlcDiscount }
        }
        if (filters.type != Type.DLC && filters.version != Version.ALL) {
            // DLCs cannot be filtered by version
            seq = seq.filter {
                if (it is DlcDiscount) {
                    return@filter false
                }
                val gameDiscount = it as GameDiscount
                if (filters.version == Version.PS4) {
                    gameDiscount.isPs4
                } else {
                    gameDiscount.isPs5
                }
            }
        }
        seq = seq.sortedWith(getComparator(sort))
        return seq.toList()
    }

    private fun getComparator(sort: Sort): Comparator<in Discount> = Comparator { p0, p1 ->
        when (sort) {
            Sort.NAME -> p0?.name?.compareTo(p1?.name ?: "") ?: 0
            Sort.SALE_PRICE -> {
                val p0Price = if (p0?.plusPrice != null) p0.plusPrice else p0?.salePrice ?: 0
                val p1Price = if (p1?.plusPrice != null) p1.plusPrice else p1?.salePrice ?: 0
                p0Price.compareTo(p1Price)
            }
            Sort.DISCOUNT_PCT -> {
                val p0DiscPct = if (p0?.plusDiscountPct != null) p0.plusDiscountPct else p0?.saleDiscountPct ?: 0
                val p1DiscPct = if (p1?.plusDiscountPct != null) p1.plusDiscountPct else p1?.saleDiscountPct ?: 0
                p1DiscPct.compareTo(p0DiscPct)
            }
        }
    }
}

data class UiState(
    val loading: Boolean = false,
    val saleDbId: Long? = null,
    val saleName: String = "",
    val saleEndTime: LocalDateTime? = null,
    val saleRegion: Region? = Region.US,
    val discounts: List<Discount> = emptyList(),
    val listMode: ListMode = ListMode.LIST,
    val filters: Filters = Filters(),
    val sort: Sort = Sort.NAME,
)

enum class ListMode {
    LIST,
    GRID,
}

data class Filters(
    val version: Version = Version.ALL,
    val type: Type = Type.ALL,
) {
    val isActive: Boolean
        get() = version != Version.ALL || type != Type.ALL
}

enum class Version {
    ALL,
    PS4,
    PS5
}

enum class Type {
    ALL,
    GAME,
    DLC
}

enum class Sort {
    NAME,
    SALE_PRICE,
    DISCOUNT_PCT
}