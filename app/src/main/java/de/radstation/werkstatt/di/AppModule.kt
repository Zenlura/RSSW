package de.radstation.werkstatt.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import de.radstation.werkstatt.data.remote.RadstationApi
import de.radstation.werkstatt.data.repository.AuftraegeRepository
import de.radstation.werkstatt.data.repository.TeileRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "http://192.168.178.53:5000/"

    @Provides
    @Singleton
    fun provideRadstationApi(): RadstationApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RadstationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuftraegeRepository(api: RadstationApi): AuftraegeRepository {
        return AuftraegeRepository(api)
    }

    @Provides
    @Singleton
    fun provideTeileRepository(api: RadstationApi): TeileRepository {
        return TeileRepository(api)
    }
}