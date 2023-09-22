package com.example.vahanproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vahanproject.repository.UniversityRepository

class ViewModelProviderFactory(private val universityRepository: UniversityRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(universityRepository) as T
    }
}