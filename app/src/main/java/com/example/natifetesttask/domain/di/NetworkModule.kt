package com.example.natifetesttask.domain.di

import com.example.natifetesttask.data.local.images.ImageDao
import com.example.natifetesttask.domain.network.NetworkImageRepository
import com.example.natifetesttask.domain.network.NetworkImageRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideNetworkRepository(
        httpClient: HttpClient,
        imageDao: ImageDao
    ) : NetworkImageRepository =
        NetworkImageRepositoryImpl(
            httpClient = httpClient,
            imageDao = imageDao
        )

    @Singleton
    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json()
            }
        }
    }
}