package com.example.myfaith.view.activity


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
//import com.example.chatlib.ChatLibrary
import com.example.myfaith.NamazUpdateWorker
import com.example.myfaith.model.utils.LocaleHelper
import com.example.myfaith.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalTime


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)

        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_nav_view)

        val testRequest = OneTimeWorkRequestBuilder<NamazUpdateWorker>().build()
        WorkManager.getInstance(this).enqueue(testRequest)

        val navController = findNavController(R.id.nav_host_fragment)

        setSupportActionBar(toolbar)

        createNotificationChannel()

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.notificationFragment, R.id.profileFragment, R.id.settingsFragment,
                R.id.nav_events, R.id.nav_communities, R.id.nav_map, R.id.nav_settings, R.id.nav_logout, R.id.lessons
            ),
            drawerLayout
        )

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profile -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                R.id.home -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.lessons -> {
                    navController.navigate(R.id.LessonsListFragment)
                    true
                }
                else -> false
            }
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                    navHostFragment.findNavController().navigate(R.id.homeFragment)
                    true
                }
                R.id.nav_events -> {
                    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                    navHostFragment.findNavController().navigate(R.id.eventsFragment)
                    true
                }
                R.id.nav_communities -> {
//                    ChatLibrary.start(this)
                    true
                }

                R.id.nav_settings -> {
                    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                    navHostFragment.findNavController().navigate(R.id.settingsFragment)
                    true
                }
                R.id.nav_map -> {
                    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                    navHostFragment.findNavController().navigate(R.id.mapsFragment)
                    true
                }
                R.id.nav_logout -> {
                    val sharedPrefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    sharedPrefs.edit().clear().apply()

                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }

                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mapsFragment -> {
                    setBottomNavigationVisibility(false)
                }
                else -> {
                    setBottomNavigationVisibility(true)
                }
            }
        }
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "azan_channel",
                "Azan Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Канал для проигрывания азана"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setBottomNavigationVisibility(isVisible: Boolean) {
        findViewById<BottomNavigationView>(R.id.bottom_nav_view)?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
    override fun attachBaseContext(newBase: Context) {
        val lang = LocaleHelper.getLanguage(newBase)
        val context = LocaleHelper.setLocale(newBase, lang)
        super.attachBaseContext(context)
    }
}