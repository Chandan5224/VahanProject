package com.example.vahanproject

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.ActivityManager
import android.app.SearchManager
import android.app.SearchManager.APP_DATA
import android.app.SearchableInfo
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.vahanproject.databinding.ActivityMainBinding
import com.example.vahanproject.service.DataRefreshService
import com.example.vahanproject.util.ConnectivityObserver
import com.example.vahanproject.util.MyApplication
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    private lateinit var searchView: SearchView
    private lateinit var moreOptionsItem: MenuItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        onClickRequestPermission(binding.root)
        val app = application as MyApplication
        viewModel = app.getSharedViewModel()
        binding.toolbar.title = ""
        setSupportActionBar(binding.toolbar)

        viewModel._connectivityStatus.observe(this, Observer { status ->
            if (status != ConnectivityObserver.Status.Available) {
                Snackbar.make(binding.root, "No internet connection", Toast.LENGTH_SHORT).show()
            }
        })


        if (!foregroundServiceRunning()) {
            val serviceIntent = Intent(this, DataRefreshService::class.java)
            startService(serviceIntent)
        }
    }

    /// toolbar set menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu);
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option1 -> {
                // Handle the search action here
                if (!foregroundServiceRunning()) {
                    Log.d("TAG", "Start foreground services by user")
                    val serviceIntent = Intent(this, DataRefreshService::class.java)
                    startService(serviceIntent)
                }
                return true
            }
            R.id.option2 -> {
                if (foregroundServiceRunning()) {
                    Log.d("TAG", "Stop foreground services by user")
                    val serviceIntent = Intent(this, DataRefreshService::class.java)
                    stopService(serviceIntent)
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    private fun foregroundServiceRunning(): Boolean {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
            if (DataRefreshService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(binding.navHostFragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("Permission: ", "Granted")
        } else {
            Log.d("Permission: ", "Denied")
        }
    }

    private fun onClickRequestPermission(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i("Permission: ", "Granted")
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this, POST_NOTIFICATIONS
            ) -> {
                Snackbar.make(
                    view,
                    "Notification access is required to receive notification.",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("OK") {
                        requestPermissionLauncher.launch(
                            POST_NOTIFICATIONS
                        )
                    }.setActionTextColor(resources.getColor(android.R.color.holo_red_light)).show()
            }
            else -> {
                requestPermissionLauncher.launch(
                    POST_NOTIFICATIONS
                )
            }
        }

    }
}