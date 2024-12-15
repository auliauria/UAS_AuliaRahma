package com.example.uas.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    @SerializedName("username")
    val username: String,

    @SerializedName("password")
    val password: String,
)

