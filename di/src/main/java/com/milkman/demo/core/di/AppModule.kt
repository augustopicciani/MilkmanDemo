package com.milkman.demo.core.di

import android.content.Context
import com.milkman.demo.data.BeerRepository
import com.milkman.demo.data.BeerRepositoryInterface
import com.milkman.demo.local.LocalDatabase
import com.milkman.demo.remote.NetworkService

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

   @Provides
   fun provideBeerRepository(
       networkService: NetworkService,
       @ApplicationContext context: Context
   ): BeerRepositoryInterface = BeerRepository(networkService,  LocalDatabase.getInstance(context).beerDao())

    @Provides
    fun provideNetworkService(okHttpClient: OkHttpClient): NetworkService =
        Retrofit.Builder()
            .baseUrl("https://api.punkapi.com/v2/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(NetworkService::class.java)



}