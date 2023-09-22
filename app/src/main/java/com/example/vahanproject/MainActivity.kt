package com.example.vahanproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.vahanproject.databinding.ActivityMainBinding
import com.example.vahanproject.repository.UniversityRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepository = UniversityRepository()
        val viewModelProviderFactory = ViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[MainViewModel::class.java]

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(binding.navHostFragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}