package com.milkman.demo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.milkman.demo.model.BeerModel
import com.milkman.demo.ui.viewmodels.DetailViewModel
import com.milkman.demo.ui.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    var beerId : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
    }

    private fun setup(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.getBundleExtra(Constants.BUNDLE_NAME)
        bundle?.let {
            beerId = it.getString(Constants.BEER_ID, null)
        }

        viewModel.beerDetail.observe(this){
           populateUI(it)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}