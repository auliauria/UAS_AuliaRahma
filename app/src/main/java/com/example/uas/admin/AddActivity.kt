package com.example.uas.admin

import android.app.Activity
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uas.R
import com.example.uas.databinding.ActivityAddBinding
import com.example.uas.databinding.ActivityAdminHomePageBinding
import com.example.uas.model.Recipe
import com.example.uas.network.ApiClient
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding){
            btnBack.setOnClickListener{
                finish()
            }

            btnSimpan.setOnClickListener(){
                val judulInp = judulResep.text.toString()
                val penulisInp = pemilikResep.text.toString()
                val stepsInp = detailResep.text.toString()
                addDataRecipes(judulInp, penulisInp, stepsInp)
            }
        }
    }

    private fun addDataRecipes(
        title: String,
        penulis : String,
        steps : String
    ) {

        val jsonData = Gson().toJson(
            mapOf(
                "title" to title,
                "penulis" to penulis,
                "steps" to steps
            )
        )

        val requestBody = jsonData.toRequestBody("application/json".toMediaType())

        val client = ApiClient.getInstance()
        val response = client.postNewRecipes(requestBody)
        response.enqueue(object : Callback<Recipe> {
            override fun onResponse(call: Call<Recipe>, response: Response<Recipe>) {
                if (response.isSuccessful) {
                    setResult(Activity.RESULT_OK)
                    finish()
                    Toast.makeText(this@AddActivity, "berhasil menambah data", Toast.LENGTH_SHORT).show()
                }
                else {
                    Log.e("API Error", "Response not successful or body is null")
                }
            }

            override fun onFailure(call: Call<Recipe>, t: Throwable) {
                Toast.makeText(this@AddActivity, "Koneksi Error", Toast.LENGTH_LONG).show()
            }
        })
    }
}