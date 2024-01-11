package com.milkman.demo.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.milkman.demo.data.BeerRepositoryInterface
import com.milkman.demo.model.BeerModel
import com.milkman.demo.model.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repositoryInterface: BeerRepositoryInterface
) : ViewModel() {

    private var page = 1
    private var _isLastPage = false
    private var _isLoading = false

    private val _beerResultLiveData = MutableLiveData<ResultState<List<BeerModel>>>()
    val beerResultLiveData get() = _beerResultLiveData
    val isLastPage get() = _isLastPage
    val isLoading get() = _isLoading


    fun getBeerList() {
        _isLoading = true
        viewModelScope.launch {
            when (val result = repositoryInterface.getBeerList(page)) {

                is ResultState.Empty -> _isLastPage = true

                else -> {
                    _beerResultLiveData.value = result
                    _isLoading = false
                    page++
                }
            }

        }
    }


}