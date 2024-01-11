package com.milkman.demo.data

import com.milkman.demo.augusto.model.mapper.ModelMapper
import com.milkman.demo.local.BeerDao
import com.milkman.demo.model.BeerModel
import com.milkman.demo.model.ResultState
import com.milkman.demo.remote.NetworkService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

import javax.inject.Inject

interface BeerRepositoryInterface {
    suspend fun getBeerList(page: Int): Flow<ResultState<List<BeerModel>>>
    suspend fun getBeerDetail(id: String): Flow<ResultState<BeerModel>>
}

class BeerRepository @Inject constructor(
    private val networkService: NetworkService,
    private val cacheService: BeerDao
) : BeerRepositoryInterface {

    override suspend fun getBeerList(page: Int): Flow<ResultState<List<BeerModel>>> = flow {
        val response  = networkService.getBeerList(page)
                when {
                    response.isSuccessful -> {
                        val beerListFromNetwork = response.body()
                        beerListFromNetwork?.takeIf { it.isNotEmpty() }?.let { beerList ->
                            cacheService.insert(beerList.map { ModelMapper.beerToCacheBeerMapper(it) })
                            emit(ResultState.Success(beerList))
                        } ?: emit(ResultState.Empty)
                    }
                else-> emit(fetchBeerListFromDbOrFailure(response.code(), response.message()))
                }

    } .catch {
        emit(fetchBeerListFromDbOrFailure())
    }

    override suspend fun getBeerDetail(id: String): Flow<ResultState<BeerModel>> = flow {
        val response = networkService.getBeerDetail(id)
                when{
                    response.isSuccessful->{
                        val beerFromNetwork = response.body()
                        beerFromNetwork?.let {
                            emit(ResultState.Success(it))
                        } ?: kotlin.run {
                            emit(fetchBeerDetailFromDbOrFailure(id))
                        }
                    }
                    else -> {
                        emit(fetchBeerDetailFromDbOrFailure(id))
                    }
                }


    }.catch {
        emit(fetchBeerDetailFromDbOrFailure(id))
    }

    private suspend fun fetchBeerDetailFromDbOrFailure(id: String): ResultState<BeerModel> {
        return try {
            val beerFromCache = cacheService.getBeerById(id)
            ResultState.Success(ModelMapper.cacheBeerToBeerMapper(beerFromCache))
        } catch (e: Exception) {
            ResultState.Failure(Exception("Exception while getting beer detail. Message:  ${e.message}"))
        }
    }

    private suspend fun fetchBeerListFromDbOrFailure(code: Int? = null, message: String? = null): ResultState<List<BeerModel>> {
        val baseExceptionString = "Failed to get beer list"
        val exceptionStringWithDetails = baseExceptionString + code?.let { codeRes ->
            " with code: $codeRes"
        }?.let { exceptionString ->
            message?.let { msg ->
                "$exceptionString and message: $msg"
            } ?: exceptionString
        }

        return try {
            val cachedBeerList = cacheService.getAllBeers()
            cachedBeerList.takeIf { it.isNotEmpty() }?.let {
                ResultState.Success(cachedBeerList.map { ModelMapper.cacheBeerToBeerMapper(it) })
            } ?: ResultState.Failure(Exception(exceptionStringWithDetails))

        } catch (e: Exception) {
            ResultState.Failure(Exception("Failed to get beer list. ${e.message}"))
        }
    }
}