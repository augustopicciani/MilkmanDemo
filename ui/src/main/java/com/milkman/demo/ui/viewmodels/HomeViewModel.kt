package com.milkman.demo.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.milkman.demo.data.BeerRepositoryInterface
import com.milkman.demo.model.BeerModel
import com.milkman.demo.model.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repositoryInterface: BeerRepositoryInterface
) : ViewModel() {

    private var page = 1
    private var _isLastPage = false
    private var _isLoading = false

    private val _beerResultStateFlow = MutableStateFlow<ResultState<List<BeerModel>>>(ResultState.Loading)
    val beerResultStateFlow: StateFlow<ResultState<List<BeerModel>>>
        get() =  _beerResultStateFlow

    val isLastPage get() = _isLastPage
    val isLoading get() = _isLoading


    fun setIsLastPage(flag : Boolean) {
        _isLastPage = flag
    }

    fun getBeerList() {
        viewModelScope.launch {
            try {
                repositoryInterface.getBeerList(page).collect { result ->
                    _beerResultStateFlow.emit(result)
                    page++
                }
            } catch (e: Exception) {
                _beerResultStateFlow.emit(ResultState.Failure(e))
            }
        }
    }


}