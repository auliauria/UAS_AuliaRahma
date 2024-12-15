package com.example.uas

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uas.admin.AdminHomePage
import com.example.uas.databinding.ActivityLoginBinding
import com.example.uas.user.HomeFragment

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)

        with(binding) {
            btnLogin.setOnClickListener {
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Mohon isi semua data",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    when {
                        username == "admin" -> {
                            prefManager.setLoggedIn(true)
                            navigateToAdminActivity()
                        }
                        isValidUsernamePassword(username, password) -> {
                            prefManager.setLoggedIn(true)
                            navigateToUserHomeFragment()
                        }
                        else -> {
                            Toast.makeText(
                                this@LoginActivity,
                                "Username atau password salah",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            registerLink.setOnClickListener {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }
        }
    }

    private fun isValidUsernamePassword(username: String, password: String): Boolean {
        val storedUsername = prefManager.getUsername()
        val storedPassword = prefManager.getPassword()
        return username == storedUsername && password == storedPassword
    }

    private fun navigateToAdminActivity() {
        Toast.makeText(this, "Login sebagai Admin berhasil", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, AdminHomePage::class.java))
        finish()
    }

    private fun navigateToUserHomeFragment() {
        Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, homeFragment) // Ganti fragment tanpa perlu container ID
            .commit()
    }

    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (isLoggedIn) {
            navigateToUserHomeFragment()
        } else {
            Toast.makeText(this@LoginActivity, "Login gagal", Toast.LENGTH_SHORT).show()
        }
    }
}