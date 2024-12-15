package com.example.uas.admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas.PrefManager
import com.example.uas.R
import com.example.uas.databinding.ActivityAdminHomePageBinding
import com.example.uas.model.Recipe
import com.example.uas.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminHomePage : AppCompatActivity() {

    private lateinit var binding: ActivityAdminHomePageBinding
    private lateinit var prefmanager: PrefManager
    private lateinit var adapterRecipe: RecipeAdapter
    private val recipeList = ArrayList<Recipe>()
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityAdminHomePageBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        prefmanager = PrefManager.getInstance(this)

        adapterRecipe = RecipeAdapter(recipeList, ApiClient.getInstance(),
            onEditRecipe = {intent ->
                activityResultLauncher.launch(intent)
            })

        with(binding) {
            btnAdd.setOnClickListener{
                val intentToAdd = Intent(this@AdminHomePage, AddActivity::class.java)
                activityResultLauncher.launch(intentToAdd)
            }

            rvAdmin.apply {
                adapter = adapterRecipe
                layoutManager = LinearLayoutManager(this@AdminHomePage)
            }


        }

        fetchRecipeFromApi()

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                fetchRecipeFromApi()
            }
        }
    }

    private fun fetchRecipeFromApi() {
        binding.progressBar.visibility = View.VISIBLE

        val client = ApiClient.getInstance()
        val response = client.getAllRecipes()

        response.enqueue(object : Callback<List<Recipe>> {
            override fun onResponse(call: Call<List<Recipe>>, response: Response<List<Recipe>>) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body() != null) {
                    val updatedRecipeList = response.body() ?: emptyList()
                    updatedRecipeList.forEach { recipe ->
                        if (recipe.id.isNullOrEmpty()) {
                            Log.e("Data Error", "Recipe with title ${recipe.title} has null ID")
                        }
                    }
                    adapterRecipe.updateData(updatedRecipeList)
                } else {
                    Log.e("API Error", "Response not successful or body is null")
                }
            }

            override fun onFailure(call: Call<List<Recipe>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@AdminHomePage, "Koneksi error: ${t.message}", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
}