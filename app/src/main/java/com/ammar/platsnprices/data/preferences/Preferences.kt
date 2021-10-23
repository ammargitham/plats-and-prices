package com.ammar.platsnprices.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

private const val APP_PREFERENCES_NAME = "app_preferences"

val Context.dataStore by preferencesDataStore(name = APP_PREFERENCES_NAME)

object PreferencesKeys {
    val REGION = stringPreferencesKey("region")
    val DISCOUNTS_LIST_MODE = stringPreferencesKey("discounts_list_mode")
    val DISCOUNTS_FILTER_VERSION = stringPreferencesKey("discounts_filter_version")
    val DISCOUNTS_FILTER_TYPE = stringPreferencesKey("discounts_filter_type")
    val DISCOUNTS_SORT = stringPreferencesKey("discounts_sort")
}
