package com.example.uas.user

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uas.R
import com.example.uas.databinding.ItemRecipeBinding
import com.example.uas.model.Recipe
import java.util.ArrayList

typealias OnClickRecipe = (Recipe) -> Unit

class ItemUserAdapter(
    private val listRecipes: ArrayList<Recipe>,
    private val listFavRecipe: ArrayList<Recipe>,
    private val userOnClick: HomeFragment,
    private val onClickRecipe: OnClickRecipe) :

    RecyclerView.Adapter<ItemUserAdapter.ItemRecipeViewHolder>() {
    inner class ItemRecipeViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Recipe) {
            with(binding) {
                judulTxt.text = data.title
                penulisTxt.text = data.penulis
                if(listFavRecipe.contains(data)){
                    btnFav.setBackgroundResource(R.drawable.ic_star)
                }else{
                    btnFav.setBackgroundResource(R.drawable.ic_star_outline)
                }

                btnFav.setOnClickListener{
                    userOnClick.favRecipe(data)
                }

                itemView.setOnClickListener{
                    onClickRecipe(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ItemRecipeViewHolder {
        val binding =
            ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemRecipeViewHolder(binding)
    }

    override fun getItemCount(): Int = listRecipes.size

    override fun onBindViewHolder(holder: ItemRecipeViewHolder, position: Int) {
        holder.bind(listRecipes[position])
        Log.i("data", listRecipes[position].title)
    }

    fun updateDataFav(newData: ArrayList<Recipe>) {
        listFavRecipe.clear()
        listFavRecipe.addAll(newData)
        notifyDataSetChanged()
    }

    fun updateData(newList: List<Recipe>) {
        listRecipes.clear()
        listRecipes.addAll(newList)
        notifyDataSetChanged()
    }
}