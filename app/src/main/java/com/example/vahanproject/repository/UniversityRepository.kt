package com.example.vahanproject.repository

import com.example.vahanproject.api.RetrofitInstance

class UniversityRepository() {
    suspend fun getUniversity() = RetrofitInstance.api.getUniversity()
}