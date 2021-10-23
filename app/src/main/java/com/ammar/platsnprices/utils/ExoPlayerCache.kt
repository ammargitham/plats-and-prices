package com.ammar.platsnprices.utils

import android.content.Context
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File


object ExoPlayerCache {
    private const val MAX_VIDEO_CACHE_SIZE_IN_BYTES: Long = 100L * 1024 * 1024 // 100MB
    private lateinit var cache: SimpleCache

    fun getInstance(context: Context): SimpleCache {
        if (!this::cache.isInitialized) {
            cache = SimpleCache(
                File(context.cacheDir, "exoCache"),
                LeastRecentlyUsedCacheEvictor(MAX_VIDEO_CACHE_SIZE_IN_BYTES),
                ExoDatabaseProvider(context)
            )
        }
        return cache
    }
}