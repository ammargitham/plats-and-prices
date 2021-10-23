package com.ammar.platsnprices

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import java.lang.RuntimeException

@HiltAndroidApp
class PlatNPricesApplication : Application() {
    init {
        if (BuildConfig.DEBUG) {
            try {
                Class.forName("dalvik.system.CloseGuard")
                    .getMethod("setEnabled", Boolean::class.javaPrimitiveType)
                    .invoke(null, true)
            } catch (e: ReflectiveOperationException) {
                throw RuntimeException(e)
            }
        }
    }
}