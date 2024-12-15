package com.example.uas.admin

import android.content.Intent
import android.icu.text.Transliterator.Position
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.uas.databinding.ItemRecipeAdminBinding
import com.example.uas.model.Recipe
import com.example.uas.network.ApiService
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeAdapter(
    private val listRecipes: ArrayList<Recipe>,
    private val client: ApiService,
    private val onEditRecipe: (Intent) -> Unit,
) :
    RecyclerView.Adapter<RecipeAdapter.ItemRecipeViewHolder>() {
    inner class ItemRecipeViewHolder(private val binding: ItemRecipeAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Recipe) {
            with(binding) {
                judulTxt.text = data.title
                penulisTxt.text = data.penulis

                if (data.id.isNullOrEmpty()) {
                    Log.e("Data Error", "Recipe ID is null for title: ${data.title}")
                }

                btnEdit.setOnClickListener {
                    val intent = Intent(itemView.context, UpdateActivity::class.java).apply {
                        putExtra("id", data.id)
                        putExtra("title", data.title)
                        putExtra("penulis", data.penulis)
                        putExtra("steps", data.steps)
                    }
                    itemView.context.startActivity(intent)
                    Log.d("onEditRecipe", "Navigating to UpdateActivity")
                }
                btnDelete.setOnClickListener {
                    deleteItem(data, itemView, adapterPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ItemRecipeViewHolder {
        val binding =
            ItemRecipeAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemRecipeViewHolder(binding)
    }

    override fun getItemCount(): Int = listRecipes.size

    override fun onBindViewHolder(holder: ItemRecipeViewHolder, position: Int) {
        holder.bind(listRecipes[position])
    }

    private fun updateRecipeOnServer(updatedRecipe: Recipe, itemView: View) {
        // Convert the updated recipe to a JSON request body
        val jsonBody = """
            {
                "title": "${updatedRecipe.title}",
                "penulis": "${updatedRecipe.penulis}",
                "steps": "${updatedRecipe.steps}"
            }
        """.trimIndent()

        val requestBody = jsonBody.toRequestBody("application/json".toMediaType())

        // Call the API to update the recipe
        updatedRecipe.id?.let { id ->
            client.updateRecipes(id, requestBody).enqueue(object : Callback<Recipe> {
                override fun onResponse(call: Call<Recipe>, response: Response<Recipe>) {
                    if (response.isSuccessful && response.body() != null) {
                        // Update the recipe in the adapter list
                        updateRecipe(response.body()!!)
                        Toast.makeText(
                            itemView.context,
                            "Resep berhasil diperbarui",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Log.e("API Error", "Gagal memperbarui resep: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<Recipe>, t: Throwable) {
                    Log.e("API Error", "Error: ${t.message}")
                    Toast.makeText(itemView.context, "Koneksi error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun deleteItem(data: Recipe, itemView: View, adapterPosition: Int){
        val recipeId = data.id
        if (recipeId.isNullOrEmpty()) {
            Log.e("deleteItem", "Cannot delete recipe because ID is null")
            Toast.makeText(itemView.context, "Gagal menghapus, ID tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        client.deleteRecipes(recipeId).enqueue(object : Callback<Recipe> {
            override fun onResponse(call: Call<Recipe>, response: Response<Recipe>) {
                if (response.isSuccessful) {
                    Log.d("deleteItem", "Successfully deleted recipe with ID: $recipeId")
                    Toast.makeText(
                        itemView.context,
                        "Data resep ${data.title} berhasil dihapus",
                        Toast.LENGTH_SHORT
                    ).show()
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        removeItem(adapterPosition)
                    }
                } else {
                    Log.e("API Error", "Failed to delete recipe: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Recipe>, t: Throwable) {
                Log.e("deleteItem", "Failure: ${t.message}")
                Toast.makeText(itemView.context, "Koneksi error", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun updateData(newList: List<Recipe>) {
        listRecipes.clear()
        listRecipes.addAll(newList)
        notifyDataSetChanged()
    }

    fun updateRecipe(updatedRecipe: Recipe) {
        val index = listRecipes.indexOfFirst { it.id == updatedRecipe.id }
        if (index != -1) {
            listRecipes[index] = updatedRecipe
            notifyItemChanged(index)
        }
    }

    fun removeItem(position: Int) {
        listRecipes.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, listRecipes.size)
    }
}