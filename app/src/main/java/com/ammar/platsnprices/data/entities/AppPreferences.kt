package com.ammar.platsnprices.data.entities

import com.ammar.platsnprices.ui.screens.sale.Filters
import com.ammar.platsnprices.ui.screens.sale.ListMode
import com.ammar.platsnprices.ui.screens.sale.Sort

data class AppPreferences(
    val region: Region,
    val discountsListMode: ListMode,
    val discountFilters: Filters,
    val discountsSort: Sort,
)