package com.example.vahanproject

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.ActivityManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.MenuItem.OnActionExpandListener
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.vahanproject.databinding.ActivityMainBinding
import com.example.vahanproject.repository.UniversityRepository
import com.example.vahanproject.service.DataRefreshService
import com.example.vahanproject.util.ConnectivityObserver
import com.example.vahanproject.util.MyApplication
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    private var isSearchViewOpen = true


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

        // Customize the toolbar as needed
//        binding.toolbar.setNavigationOnClickListener {
//            if (isSearchViewOpen) {
//                // Handle the "Up" button behavior when the search view is open
//                binding.toolbarName.visibility = View.GONE
//                closeSearchView()
//            } else if (!isSearchViewOpen) {
//                binding.toolbarName.visibility = View.VISIBLE
//            } else {
//                binding.toolbarName.visibility = View.VISIBLE
//                onBackPressed()
//            }
//        }

        if (!foregroundServiceRunning()) {
            val serviceIntent = Intent(this, DataRefreshService::class.java)
            startService(serviceIntent)
        }
    }

//    private fun closeSearchView() {
//        val searchItem = binding.toolbar.menu.findItem(R.id.action_search)
//        val searchView = searchItem.actionView as SearchView
//        searchView.onActionViewCollapsed()
//        // Remove focus from the search view
//        searchView.clearFocus()
//    }

    /// toolbar set menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu);

//        Log.d("TAG", "Run visible check")
//
//        val searchItem: MenuItem = menu!!.findItem(R.id.action_search)
//        val searchView = searchItem.actionView as SearchView
//
//        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
//            isSearchViewOpen = hasFocus
//            // Show or hide the "Up" button based on whether the search view is open
//            supportActionBar?.setDisplayHomeAsUpEnabled(hasFocus)
//        }
//
//
//        // Configure the search view
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                // Handle search query submission
//                query?.let { performSearch(it) }
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                // Handle search query text change
//                // This method is called as the user types
//                return true
//            }
//        })

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
            // Handle other menu items here
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

    private val requestPermissionLauncher =
        registerForActivityResult(
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
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i("Permission: ", "Granted")
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                POST_NOTIFICATIONS
            ) -> {
                Snackbar.make(
                    view,
                    "Notification access is required to receive notification.",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("OK") {
                        requestPermissionLauncher.launch(
                            POST_NOTIFICATIONS
                        )
                    }
                    .setActionTextColor(resources.getColor(android.R.color.holo_red_light))
                    .show()
            }
            else -> {
                requestPermissionLauncher.launch(
                    POST_NOTIFICATIONS
                )
            }
        }

    }
}