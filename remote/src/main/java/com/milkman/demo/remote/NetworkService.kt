package com.milkman.demo.remote

import com.milkman.demo.model.BeerModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkService {

    @GET("beers")
    suspend fun getBeerList(@Query("page") page: Int): Response<List<BeerModel>>

    @GET("beers/{id}")
    suspend fun getBeerDetail(@Path("id") id : String) : Response<BeerModel>
}