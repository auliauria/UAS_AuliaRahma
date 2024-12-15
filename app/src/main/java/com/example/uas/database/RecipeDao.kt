package com.example.uas.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.uas.model.Recipe

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(recipes: Recipe)

    @Update
    fun update(recipes: Recipe)

    @Delete
    fun delete(recipes: Recipe)

    @get:Query("SELECT * FROM recipes")
    val allRecipes: LiveData<List<Recipe>>
}
