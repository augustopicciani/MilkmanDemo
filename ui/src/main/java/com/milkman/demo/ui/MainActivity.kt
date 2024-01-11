package com.milkman.demo.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.milkman.demo.model.BeerModel
import com.milkman.demo.model.ResultState
import com.milkman.demo.ui.adapters.BeerListAdapter
import com.milkman.demo.ui.databinding.ActivityMainBinding
import com.milkman.demo.ui.listeners.PaginationScrollListener
import com.milkman.demo.ui.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val viewModel: HomeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
        viewModel.getBeerList()
    }

    private fun setup() {
        val beerList = ArrayList<BeerModel>()
        binding.beerList.layoutManager = LinearLayoutManager(this)


        val adapter = BeerListAdapter{
            openDetailActivity(it.id.toString())
        }
        binding.beerList.adapter = adapter


        binding.beerList.addOnScrollListener(object :
            PaginationScrollListener(binding.beerList.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                viewModel.getBeerList()
            }

            override fun isLastPage(): Boolean {
                return viewModel.isLastPage
            }

            override fun isLoading(): Boolean {
                return viewModel.isLoading
            }
        })



        viewModel.beerResultLiveData.observe(this){ result->
            when(result){
                is ResultState.Success->{
                    beerList.addAll(result.data)
                    adapter.submitList(ArrayList(beerList))
                }
                is ResultState.Failure->{
                    Snackbar.make(binding.root,result.exception.message.toString(), Snackbar.LENGTH_LONG).show()
                }
                is ResultState.Empty->{}
            }
        }

    }

    private fun openDetailActivity(beerId : String){
        val intent = Intent(this, DetailActivity::class.java)
        val bundle = Bundle()
        bundle.putString(Constants.BEER_ID,beerId)
        intent.putExtra(Constants.BUNDLE_NAME, bundle)
        startActivity(intent)
    }
}