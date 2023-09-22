package com.example.vahanproject.api

import com.example.vahanproject.models.University
import retrofit2.Response
import retrofit2.http.GET

interface UniversityAPI {

    @GET("search")
    suspend fun getUniversity(): Response<List<University>>
}