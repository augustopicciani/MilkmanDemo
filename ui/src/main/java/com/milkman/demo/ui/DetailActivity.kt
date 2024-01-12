package com.milkman.demo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.milkman.demo.model.BeerModel
import com.milkman.demo.model.ResultState
import com.milkman.demo.ui.viewmodels.DetailViewModel
import com.milkman.demo.ui.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private var beerId : String? = null
    private lateinit var  loader : LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
    }

    private fun setup(){

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loader = binding.loaderIncludeView.loader

        val bundle = intent.getBundleExtra(Constants.BUNDLE_NAME)
        bundle?.let {
            beerId = it.getString(Constants.BEER_ID, null)
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.beerResultStateFlow.collect{ result->
                    when (result) {
                        is ResultState.Success -> {
                            hideLoader()
                           populateUI(result.data)
                        }
                        is ResultState.Failure -> {
                            Snackbar.make(binding.root, result.exception.message.toString(), Snackbar.LENGTH_LONG).show()
                        }
                        is ResultState.Empty -> {}
                        is ResultState.Loading -> {showLoader()}
                    }


                }
            }
        }


        beerId?.let { id->
            viewModel.getBeerDetail(id)
        }
    }

    private fun populateUI(beerModel: BeerModel) {
        binding.beerTitle.text = beerModel.name?.trim()
        binding.beerDescription.text = beerModel.description
        Glide
            .with(this)
            .load(beerModel.image_url)
            .into(binding.beerImage)
    }

    private fun showLoader(){
        loader.visibility = View.VISIBLE
        loader.playAnimation()
    }

    private fun hideLoader(){
        loader.visibility = View.GONE
        loader.pauseAnimation()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}