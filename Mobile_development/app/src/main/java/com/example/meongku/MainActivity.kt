package com.example.meongku

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.meongku.databinding.ActivityMainBinding
import com.example.meongku.preference.UserPreferences
import com.example.meongku.ui.login.LoginActivity
import android.Manifest
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.meongku.ui.main.article.ArticleDetailFragment
import com.example.meongku.ui.main.article.ArticleFragment
import com.example.meongku.ui.main.cat.CatDetailFragment
import com.example.meongku.ui.main.cat.CatListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userPreferences: UserPreferences


    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userPreferences = UserPreferences(this)
        if(!userPreferences.isLoggedIn) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_food, R.id.navigation_scan,R.id.navigation_cat,R.id.navigation_user
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(
                        R.id.navigation_home,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.navigation_home, true)
                            .build()
                    )
                    true
                }
                R.id.navigation_food -> {
                    navController.navigate(
                        R.id.navigation_food,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.navigation_food, true)
                            .build()
                    )
                    true
                }
                R.id.navigation_scan -> {
                    navController.navigate(
                        R.id.navigation_scan,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.navigation_scan, true)
                            .build()
                    )
                    true
                }
                R.id.navigation_cat -> {
                    navController.navigate(
                        R.id.navigation_cat,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.navigation_cat, true)
                            .build()
                    )
                    true
                }
                R.id.navigation_user -> {
                    navController.navigate(
                        R.id.navigation_user,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.navigation_user, true)
                            .build()
                    )
                    true
                }
                else -> false
            }
        }
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        supportActionBar?.hide()
    }
    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val currentFragment = navHostFragment.childFragmentManager.fragments[0]

        when (currentFragment) {
            is CatListFragment -> {
                findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_home)
            }
            is CatDetailFragment, is ArticleDetailFragment -> {
                super.onBackPressed()
            }
            is ArticleFragment -> {
                findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_home)
            }
            else -> {
                AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Are you sure you want to exit the app and logout?")
                    .setPositiveButton("Yes") { _, _ ->
                        userPreferences.clear()
                        finishAffinity()
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        }
    }
}