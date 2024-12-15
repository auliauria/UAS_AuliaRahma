package com.example.uas.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas.database.DatabaseInstance
import com.example.uas.database.RecipeDao
import com.example.uas.databinding.FragmentHomeBinding
import com.example.uas.model.Recipe
import com.example.uas.network.ApiClient
import com.example.uas.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), UserOnClick {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentHomeBinding
    private lateinit var recipeDao: RecipeDao

    private lateinit var executorService: ExecutorService
    private lateinit var client: ApiService
    private lateinit var adapterRecipes: ItemUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        var recipeList = ArrayList<Recipe>()
        var recipeFavList = ArrayList<Recipe>()

        executorService = Executors.newSingleThreadExecutor()
        val db = DatabaseInstance.getDatabase(requireContext())
        recipeDao = db!!.recipeDao()!!

        adapterRecipes = ItemUserAdapter(recipeList, recipeFavList, this) { recipes ->
            Toast.makeText(requireContext(), "klik", Toast.LENGTH_SHORT).show()
            val intentToDetail = Intent(requireActivity(), RecipeDetail::class.java)
            intentToDetail.putExtra("id", recipes.id)
            intentToDetail.putExtra("judul", recipes.title)
            intentToDetail.putExtra("penulis", recipes.penulis)
            intentToDetail.putExtra("detail", recipes.steps)
            startActivity(intentToDetail)
        }

        binding.rvUser.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUser.adapter = adapterRecipes

        recipeDao.allRecipes.observe(requireActivity()){ arrayFav ->
            adapterRecipes.updateDataFav(ArrayList(arrayFav))
        }

        fetchRecipeFromApi()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun favRecipe(recipes: Recipe) {
        executorService.execute{ recipeDao.insert(recipes)}
        Toast.makeText(requireContext(), "Resep ${recipes.title} berhasil ditambahkan ke bookmark", Toast.LENGTH_SHORT).show()
    }

    private fun fetchRecipeFromApi(){
        client = ApiClient.getInstance()
        val response = client.getAllRecipes()

        response.enqueue(object : Callback<List<Recipe>> {
            override fun onResponse(call: Call<List<Recipe>>, response: Response<List<Recipe>>) {
                if (response.isSuccessful && response.body() != null) {
                    binding.progressBar.visibility = View.GONE
                    response.body()?.forEach { i ->
                        val updatedRecipeList = response.body() ?: emptyList()
                        adapterRecipes.updateData(updatedRecipeList)
                    }
                }
            }

            override fun onFailure(call: Call<List<Recipe>>, t: Throwable) {
                Log.e("cannot get API", t.message.toString())
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}