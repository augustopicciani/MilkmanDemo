package com.milkman.demo.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.milkman.demo.model.BeerModel
import com.milkman.demo.ui.databinding.BeerItemRowViewBinding


class BeerViewHolder(
    private val binding: BeerItemRowViewBinding,
    onItemClicked: (Int) -> Unit
) :  RecyclerView.ViewHolder(binding.root){

    init {
        binding.root.setOnClickListener {
            onItemClicked(adapterPosition)
        }
    }

    fun bind(item : BeerModel){


        binding.rowTitle.text = item.name

        Glide
            .with(binding.root.context)
            .load(item.image_url)
            .into(binding.rowIcon)
    }
}