package com.milkman.demo.data

import com.milkman.demo.model.BeerModel
import com.milkman.demo.model.ResultState
import com.milkman.demo.augusto.model.mapper.ModelMapper
import com.milkman.demo.local.BeerDao
import com.milkman.demo.remote.NetworkService
import javax.inject.Inject
import kotlin.Exception

interface BeerRepositoryInterface{
    suspend fun getBeerList(page : Int) : ResultState<List<BeerModel>>
    suspend fun getBeerDetail(id : String) : ResultState<BeerModel>
}



class BeerRepository @Inject constructor(
    private val networkService: NetworkService,
    private val cacheService : BeerDao
) : BeerRepositoryInterface {



    override suspend fun getBeerList(page : Int): ResultState<List<BeerModel>> {
        return try {
            val response = networkService.getBeerList(page)

            when {
                response.isSuccessful -> {
                    val beerListFromNetwork = response.body()
                    beerListFromNetwork?.takeIf { it.isNotEmpty() }?.let { beerList->
                        cacheService.insert(beerList.map { ModelMapper.beerToCacheBeerMapper(it)})
                        ResultState.Success(beerList)
                    } ?: ResultState.Empty
                }
                else -> {
                    fetchBeerListFromDbOrFailure(response.code(), response.message())
                }
            }
        } catch (e: Exception) {
            fetchBeerListFromDbOrFailure()

        }
    }




    override suspend fun getBeerDetail(id: String): ResultState<BeerModel> {


        return try {
            val response = networkService.getBeerDetail(id)

            when {
                response.isSuccessful -> {
                    val beerFromNetwork = response.body()
                    beerFromNetwork?.let {
                        ResultState.Success(it)
                    } ?: kotlin.run {
                        fetchBeerDetailFromDbOrFailure(id)
                    }
                }

                else -> {
                    fetchBeerDetailFromDbOrFailure(id)
                }
            }
        } catch (e: Exception) {
            fetchBeerDetailFromDbOrFailure(id)
        }


    }

    private suspend fun fetchBeerDetailFromDbOrFailure(id: String): ResultState<BeerModel> {
        return try {
            val beerFromCache = cacheService.getBeerById(id)
            ResultState.Success(ModelMapper.cacheBeerToBeerMapper(beerFromCache))
        } catch (e: Exception) {
            ResultState.Failure(Exception("Exception while getting beer detail. Message:  ${e.message}"))
        }

    }

    private suspend fun fetchBeerListFromDbOrFailure(code : Int? = null, message : String? = null): ResultState<List<BeerModel>> {
        val baseExceptionString = "Failed to get beer list"
        val exceptionStringWithDetails = baseExceptionString + code?.let { codeRes ->
            " with code: $codeRes"
        }?.let {  exceptionString->
            message?.let { msg ->
                "$exceptionString and message: $msg"
            } ?: exceptionString
        }

        return try {
            val cachedBeerList = cacheService.getAllBeers()
            cachedBeerList.takeIf { it.isNotEmpty() }?.let {
                ResultState.Success(cachedBeerList.map { ModelMapper.cacheBeerToBeerMapper(it)})
            } ?:  ResultState.Failure(Exception(exceptionStringWithDetails))

        }catch (e : Exception){
            ResultState.Failure(Exception("Failed to get beer list. ${e.message}"))
        }

    }


}