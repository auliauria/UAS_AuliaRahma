package com.example.uas.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.uas.model.Recipe
import com.example.uas.model.User

@Database(entities = [User::class, Recipe::class,], version = 1, exportSchema = false)
abstract class DatabaseInstance : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao?
//    abstract fun recipeImageDao(): RecipeImageDao?

    companion object{
        @Volatile
        private var INSTANCE:DatabaseInstance? =null
        fun getDatabase(context: Context):DatabaseInstance?{
            if(INSTANCE == null){
                synchronized(DatabaseInstance::class.java){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseInstance::class.java, "Recipe"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}
