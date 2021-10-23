package com.ammar.platsnprices.data.repositories

import com.ammar.platsnprices.data.entities.OpenCriticGame
import com.ammar.platsnprices.data.network.OpenCriticService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OpenCriticRepository @Inject constructor(
    private val openCriticService: OpenCriticService,
) {
    suspend fun getGameData(id: Long): OpenCriticGame = withContext(Dispatchers.IO) { openCriticService.fetchGameData(id) }
}