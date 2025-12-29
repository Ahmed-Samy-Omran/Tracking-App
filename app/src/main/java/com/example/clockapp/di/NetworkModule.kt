package com.example.clockapp.di

import com.example.clockapp.domain.tracking.DirectionsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class )
object NetworkModule {

    private const val BASE_URL = "https://maps.googleapis.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient( ): OkHttpClient { // <-- Create an OkHttpClient provider
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY) // Log everything
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit { // <-- Inject the client
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // <-- Add the client to Retrofit
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideDirectionsApiService(retrofit: Retrofit): DirectionsApiService =
        retrofit.create(DirectionsApiService::class.java)
    // ... provideDirectionsApiService remains the same ...
}
