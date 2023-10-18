package com.example.natifetesttask.domain.network

import androidx.compose.ui.unit.IntOffset
import com.example.natifetesttask.data.Data
import com.example.natifetesttask.data.GiphyResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface NetworkRepository {
    suspend fun getImages(search: String, offset: Int): List<Data>
}

class NetworkRepositoryImpl(private val httpClient: HttpClient) : NetworkRepository {
    val API_KEY = "DOdqj0cV0EKTKH9hkQvHxqfloK4WfVlB"
    override suspend fun getImages(search: String, offset: Int): List<Data> {

        val imagesResponse = httpClient
            .get("https://api.giphy.com/v1/gifs/search?api_key=$API_KEY&q=$search&limit=25&offset=$offset&rating=g&lang=en&bundle=messaging_non_clips")
            .body<GiphyResponse>()

        return imagesResponse.data
    }

}