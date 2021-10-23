package com.ammar.platsnprices.data.repositories

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.ammar.platsnprices.data.entities.AppPreferences
import com.ammar.platsnprices.data.entities.Region
import com.ammar.platsnprices.data.preferences.PreferencesKeys
import com.ammar.platsnprices.ui.screens.sale.*
import com.ammar.platsnprices.utils.TAG
import com.ammar.platsnprices.utils.valueOfOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferencesRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {

    val appPreferencesFlow: Flow<AppPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { mapAppPreferences(it) }

    suspend fun updateRegion(region: Region) = withContext(Dispatchers.IO) {
        dataStore.edit { it[PreferencesKeys.REGION] = region.code }
    }

    suspend fun updateDiscountsListMode(mode: ListMode) = withContext(Dispatchers.IO) {
        dataStore.edit { it[PreferencesKeys.DISCOUNTS_LIST_MODE] = mode.name }
    }

    suspend fun updateDiscountFilters(filters: Filters) = withContext(Dispatchers.IO) {
        dataStore.edit {
            it[PreferencesKeys.DISCOUNTS_FILTER_VERSION] = filters.version.name
            it[PreferencesKeys.DISCOUNTS_FILTER_TYPE] = filters.type.name
        }
    }

    suspend fun updateDiscountsSort(sort: Sort) = withContext(Dispatchers.IO) {
        dataStore.edit { it[PreferencesKeys.DISCOUNTS_SORT] = sort.name }
    }

    private fun mapAppPreferences(preferences: Preferences): AppPreferences = AppPreferences(
        region = Region.findByCode(preferences[PreferencesKeys.REGION] ?: Region.US.code) ?: Region.US,
        discountsListMode = valueOfOrNull(preferences[PreferencesKeys.DISCOUNTS_LIST_MODE]) ?: ListMode.LIST,
        discountFilters = Filters(
            version = valueOfOrNull(preferences[PreferencesKeys.DISCOUNTS_FILTER_VERSION]) ?: Version.ALL,
            type = valueOfOrNull(preferences[PreferencesKeys.DISCOUNTS_FILTER_TYPE]) ?: Type.ALL,
        ),
        discountsSort = valueOfOrNull(preferences[PreferencesKeys.DISCOUNTS_SORT]) ?: Sort.NAME,
    )
}