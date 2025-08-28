package com.justin.id_34279075.nutritrack.data.network.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

/**
 * Interface for fetching random images from the Picsum API.
 */
interface PicsumAPI {

    /**
     * Fetches a random image with a fixed width of 1000 pixels.
     *
     * @return A [Response] object containing raw image data as [ResponseBody].
     */
    @GET("1000")
    suspend fun getRandomImage(): Response<ResponseBody>
}