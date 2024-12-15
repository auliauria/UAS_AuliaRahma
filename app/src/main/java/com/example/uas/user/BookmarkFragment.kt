package com.example.uas.user

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uas.database.DatabaseInstance
import com.example.uas.database.RecipeDao
import com.example.uas.databinding.FragmentBookmarkBinding
import com.example.uas.model.Recipe
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BookmarkFragment : Fragment(), UserOnClick {
    private lateinit var binding: FragmentBookmarkBinding
    private lateinit var recipeDao: RecipeDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        val view = binding.root

        executorService = Executors.newSingleThreadExecutor()
        val db = DatabaseInstance.getDatabase(requireContext())
        recipeDao = db!!.recipeDao()!!

        setupRecyclerView()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllRecipes()
    }

    private fun setupRecyclerView() {
        binding.rvUser.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getAllRecipes(){
        recipeDao.allRecipes.observe(viewLifecycleOwner){ listRecipe ->
            val adapter:ItemUserAdapter = ItemUserAdapter(
                listRecipes = ArrayList(listRecipe),
                listFavRecipe = ArrayList(listRecipe),
                userOnClick = requireActivity() as HomeFragment,
                onClickRecipe = { recipe ->
                val intentToDetail = Intent(requireContext(), RecipeDetail::class.java)
                intentToDetail.putExtra("id", recipe.id)
                intentToDetail.putExtra("judul", recipe.title)
                intentToDetail.putExtra("penulis", recipe.penulis)
                intentToDetail.putExtra("detail", recipe.steps)
                startActivity(intentToDetail)
            })
            binding.rvUser.adapter = adapter
        }
    }

    override fun favRecipe(recipe: Recipe) {
        executorService.execute{recipeDao.delete(recipe)}
    }
}