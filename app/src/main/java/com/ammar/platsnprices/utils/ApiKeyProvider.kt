package com.ammar.platsnprices.utils

import com.ammar.platsnprices.BuildConfig

object ApiKeyProvider {
    val platPricesApiKey: String by lazy {
        return@lazy BuildConfig.PLAT_PRICES_API_KEY
    }
}