package com.example.vahanproject.util

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.example.vahanproject.MainViewModel
import com.example.vahanproject.ViewModelProviderFactory
import com.example.vahanproject.repository.UniversityRepository

class MyApplication : Application() {
    private val appViewModelStore: ViewModelStore by lazy { ViewModelStore() }
    private lateinit var sharedViewModel: MainViewModel

    override fun onCreate() {
        super.onCreate()
        // Create your repository instance here
        val repository = UniversityRepository()
        sharedViewModel =
            ViewModelProvider(
                appViewModelStore,
                ViewModelProviderFactory(repository, applicationContext)
            )[MainViewModel::class.java]
    }

    fun getSharedViewModel(): MainViewModel {
        return sharedViewModel
    }
}
