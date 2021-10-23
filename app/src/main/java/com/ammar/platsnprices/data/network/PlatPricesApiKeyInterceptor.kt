package com.ammar.platsnprices.data.network

import com.ammar.platsnprices.utils.ApiKeyProvider
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response


class PlatPricesApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestUrl = chain.request().url
        if (requestUrl.host != PLAT_PRICES_HOST) {
            return chain.proceed(chain.request())
        }
        val url: HttpUrl = requestUrl
            .newBuilder()
            .addQueryParameter("key", ApiKeyProvider.platPricesApiKey)
            .build()
        return chain.proceed(chain.request().newBuilder().url(url).build())
    }
}