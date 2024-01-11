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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repositoryInterface: BeerRepositoryInterface
) : ViewModel() {



    private val _beerResultStateFlow = MutableStateFlow<ResultState<BeerModel>>(ResultState.Loading)
    val beerResultStateFlow: StateFlow<ResultState<BeerModel>>
        get() =  _beerResultStateFlow


    fun getBeerDetail(id : String){
        viewModelScope.launch {
            try {
                repositoryInterface.getBeerDetail(id)
                    .collect{result->
                        _beerResultStateFlow.emit(result)

                    }
            }catch (e : Exception){
                _beerResultStateFlow.emit(ResultState.Failure(e))
            }

        }
    }
}