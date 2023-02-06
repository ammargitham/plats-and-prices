package com.ammar.platsnprices.data.entities

import android.util.Log
import com.ammar.platsnprices.utils.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

sealed class Resource<out R> {
    data class Loading<out T>(val data: T?) : Resource<T>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val throwable: Throwable) : Resource<Nothing>()
}

fun <T> Resource<T>.successOr(fallback: T): T {
    return (this as? Resource.Success<T>)?.data ?: fallback
}

inline fun <ResultType, NetworkResponseType> networkBoundResource(
    crossinline dbQuery: () -> Flow<ResultType>,
    crossinline fetch: suspend (ResultType) -> NetworkResponseType,
    crossinline saveFetchResult: suspend (NetworkResponseType) -> Unit,
    crossinline onFetchFailed: (Throwable) -> Unit = {},
    crossinline shouldFetch: suspend (ResultType) -> Boolean = { true }
) = flow {
    emit(Resource.Loading(null))
    val data = dbQuery().first()
    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(data))
        try {
            saveFetchResult(fetch(data))
            dbQuery().map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            Log.e(TAG, "networkBoundResource: ", throwable)
            onFetchFailed(throwable)
            dbQuery().map { Resource.Error(throwable) }
        }
    } else {
        dbQuery().map { Resource.Success(it) }
    }
    emitAll(flow)
}