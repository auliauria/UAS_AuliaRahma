package com.example.uas.admin

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uas.R
import com.example.uas.databinding.ActivityUpdateBinding
import com.example.uas.model.Recipe
import com.example.uas.network.ApiClient
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val id = intent.getStringExtra("id")
        val title = intent.getStringExtra("title")
        val penulis = intent.getStringExtra("penulis")
        val steps = intent.getStringExtra("steps")

        with(binding){
            judulResep.setText(title.toString())
            pemilikResep.setText(penulis.toString())
            detailResep.setText(steps.toString())

            btnBack.setOnClickListener{
                finish()
            }

            btnSimpan.setOnClickListener{
                val judulInp = judulResep.text.toString()
                val penulisInp = pemilikResep.text.toString()
                val stepsInp = detailResep.text.toString()

                EditDataRecipes(id, judulInp, penulisInp, stepsInp)
            }
        }
    }

    private fun EditDataRecipes(
        id: String?,
        judulInp: String,
        penulisInp: String,
        stepsInp: String
    ) {
        val jsonData = Gson().toJson(
            mapOf(
                "title" to judulInp,
                "penulis" to penulisInp,
                "steps" to stepsInp
            )
        )

        val requestBody = jsonData.toRequestBody("application/json".toMediaType())

        val client = ApiClient.getInstance()
        client.updateRecipes(id ?: return, requestBody).enqueue(object : Callback<Recipe> {
            override fun onResponse(call: Call<Recipe>, response: Response<Recipe>) {
                if (response.isSuccessful) {
                    setResult(Activity.RESULT_OK)
                    finish()
                    Toast.makeText(this@UpdateActivity, "berhasil mengubah data", Toast.LENGTH_SHORT).show()
                }
                else {
                    Log.e("API Error", "Response not successful or body is null")
                }
            }

            override fun onFailure(call: Call<Recipe>, t: Throwable) {
                Toast.makeText(this@UpdateActivity, "Koneksi Error", Toast.LENGTH_LONG).show()
            }
        })
    }
}