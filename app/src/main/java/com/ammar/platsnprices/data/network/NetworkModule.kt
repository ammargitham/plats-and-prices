package com.ammar.platsnprices.data.network

import android.content.Context
import com.ammar.platsnprices.data.entities.NetworkRecentDiscountsStringListAdapter
import com.ammar.platsnprices.data.entities.ProductStringListAdapter
import com.ammar.platsnprices.data.entities.TierAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class PlatPricesRetrofit

@Qualifier
annotation class OpenCriticRetrofit

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesPlatPricesApiKeyInterceptor() = PlatPricesApiKeyInterceptor()

    @Singleton
    @Provides
    fun providesOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        platPricesApiKeyInterceptor: PlatPricesApiKeyInterceptor,
        @ApplicationContext context: Context
    ) = OkHttpClient
        .Builder()
        .cache(
            Cache(
                directory = File(context.cacheDir, "http_cache"),
                maxSize = 50L * 1024L * 1024L // 50 MiB
            )
        )
        .addInterceptor(platPricesApiKeyInterceptor)
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun providesMoshi(): Moshi = Moshi
        .Builder()
        .add(ProductStringListAdapter())
        .add(TierAdapter())
        .add(NetworkRecentDiscountsStringListAdapter())
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @PlatPricesRetrofit
    @Singleton
    @Provides
    fun providesPlatPricesRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(PLAT_PRICES_BASE_URL)
        .client(okHttpClient)
        .build()

    @OpenCriticRetrofit
    @Singleton
    @Provides
    fun providesOpenCriticRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(OPEN_CRITIC_BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun providesPlatPricesService(@PlatPricesRetrofit retrofit: Retrofit): PlatPricesService = retrofit.create(PlatPricesService::class.java)

    @Singleton
    @Provides
    fun providesOpenCriticService(@OpenCriticRetrofit retrofit: Retrofit): OpenCriticService = retrofit.create(OpenCriticService::class.java)
}