package com.example.vahanproject

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vahanproject.repository.UniversityRepository

class ViewModelProviderFactory(
    private val universityRepository: UniversityRepository, private val context: Context
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(universityRepository, context) as T
    }
}