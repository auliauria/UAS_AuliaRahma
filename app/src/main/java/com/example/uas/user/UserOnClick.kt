package com.example.uas.user

import com.example.uas.model.Recipe

interface UserOnClick {
    fun favRecipe(recipe: Recipe);
}