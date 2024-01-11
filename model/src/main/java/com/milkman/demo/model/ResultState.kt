package com.milkman.demo.model

sealed class ResultState<out T> {


    data class Success<T>(val data: T) : ResultState<T>()

    data class Failure(val exception : Exception) : ResultState<Nothing>()

    data object Empty : ResultState<Nothing>()
    data object Loading : ResultState<Nothing>()




}
