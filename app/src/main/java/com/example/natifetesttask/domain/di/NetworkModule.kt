package com.example.natifetesttask.domain.di

import com.example.natifetesttask.domain.network.NetworkRepository
import com.example.natifetesttask.domain.network.NetworkRepositoryImpl
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
    fun provideNetworkRepository(httpClient: HttpClient) : NetworkRepository = NetworkRepositoryImpl(httpClient = httpClient)

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