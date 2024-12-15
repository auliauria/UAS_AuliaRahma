package com.example.uas.network

import com.example.uas.model.Recipe
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("recipes")
    fun getAllRecipes(): Call<List<Recipe>>

    @POST("recipes")
    fun postNewRecipes(@Body rawJson: RequestBody) : Call<Recipe>

    @POST("recipes/{id}")
    fun updateRecipes(@Path("id") recipeId: String, @Body rawJson: RequestBody) : Call<Recipe>

    @DELETE("recipes/{id}")
    fun deleteRecipes(@Path("id") recipeId: String): Call<Recipe>
}