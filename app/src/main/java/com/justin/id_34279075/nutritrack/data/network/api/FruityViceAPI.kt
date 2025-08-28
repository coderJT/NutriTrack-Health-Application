package com.justin.id_34279075.nutritrack.data.network.api

import com.justin.id_34279075.nutritrack.data.network.FruitResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interface for handling API requests to FruityViceAPI.
 */
interface FruityViceAPI {

    /**
     * Retrieves nutritional information about a fruit through its name.
     *
     * @param name Name of the fruit to be searched for.
     * @return A [FruitResponse] object containing the fruit details including nutritions.
     */
    @GET("fruit/{name}")
    suspend fun getFruitByName(@Path("name") name: String): FruitResponse
}