package com.milkman.demo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.milkman.demo.model.BeerModel
import com.milkman.demo.ui.databinding.BeerItemRowViewBinding


class BeerListAdapter(
    private val onItemClick: (BeerModel) -> Unit

) : RecyclerView.Adapter<BeerViewHolder>() {

    private var beerList: List<BeerModel> = emptyList()

    fun submitList(newList: List<BeerModel>) {
        val diffResult = DiffUtil.calculateDiff(BeerDiffCallback(beerList, newList))
        beerList = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeerViewHolder {
        val binding = BeerItemRowViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BeerViewHolder(binding) { position ->
            onItemClick.invoke(beerList[position])
        }
    }

    override fun onBindViewHolder(holder: BeerViewHolder, position: Int) {
        val item = beerList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return beerList.size
    }

    private class BeerDiffCallback(
        private val oldList: List<BeerModel>,
        private val newList: List<BeerModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}