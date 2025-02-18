package com.example.myfaith


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mynavigationapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toolbar: Toolbar = findViewById(R.id.toolbar) // Get the toolbar
        val navController = findNavController(R.id.nav_host_fragment) // Get the NavController

        setSupportActionBar(toolbar) // Set up the toolbar

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.notificationFragment, R.id.profileFragment, // Bottom Nav Destinations
                R.id.nav_events, R.id.nav_communities, R.id.nav_progress, R.id.nav_settings, R.id.nav_logout // Drawer Destinations
            ),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Handle Navigation Drawer item clicks
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_events -> {
                    // Handle Events click
                    true
                }
                R.id.nav_communities -> {
                    // Handle Communities click
                    true
                }
                R.id.nav_progress -> {
                    // Handle Progress click
                    true
                }
                R.id.nav_settings -> {
                    // Handle Settings click
                    true
                }
                R.id.nav_logout -> {
                    // Handle Logout click
                    true
                }
                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}