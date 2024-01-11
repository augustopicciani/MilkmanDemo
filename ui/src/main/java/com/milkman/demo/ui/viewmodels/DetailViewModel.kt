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
class DetailViewModel @Inject constructor(
    private val repositoryInterface: BeerRepositoryInterface
) : ViewModel() {

  private val _beerDetailLiveData = MutableLiveData<BeerModel>()
  val beerDetail get() = _beerDetailLiveData


    fun getBeerDetail(id : String){
        viewModelScope.launch {
            when (val result = repositoryInterface.getBeerDetail(id)){
                is ResultState.Success-> {
                    _beerDetailLiveData.value = result.data
                }
                is ResultState.Failure ->{}
                is ResultState.Empty->{}
            }
        }
    }
}