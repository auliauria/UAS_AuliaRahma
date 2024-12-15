package com.example.uas.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.UUID

@Entity("recipes")
data class Recipe(
    @PrimaryKey
    @NonNull
    @SerializedName("_id")
    val id: String = UUID.randomUUID().toString(),

    @SerializedName("title")
    val title: String,

    @SerializedName("penulis")
    val penulis: String,

    @SerializedName("steps")
    val steps: String
)

