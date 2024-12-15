package com.example.uas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.uas.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)

        if (binding.bottomNavigationView == null) {
            Log.e("DEBUG", "BottomNavigationView is null")
        }

        Log.d("DEBUG", "Activity created and BottomNavigationView setup started")

        val bottomNav = BottomNavigationView(this)
        bottomNav.layoutParams = CoordinatorLayout.LayoutParams(
            CoordinatorLayout.LayoutParams.MATCH_PARENT,
            CoordinatorLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.BOTTOM
        }
        bottomNav.menu.add(0, 1, 0, "Home").setIcon(R.drawable.ic_home)
        binding.root.addView(bottomNav)



        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Setup BottomNavigationView with NavController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        // Handle bottom navigation item selection
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment2 -> {
                    navController.navigate(R.id.homeFragment2)
                    true
                }
                R.id.bookmarkFragment2 -> {
                    navController.navigate(R.id.bookmarkFragment2)
                    true
                }
                R.id.profileFragment2 -> {
                    navController.navigate(R.id.profileFragment2)
                    true
                }
                else -> false
            }
        }
    }
}
