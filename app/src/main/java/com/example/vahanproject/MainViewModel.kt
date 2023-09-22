package com.example.vahanproject

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vahanproject.models.University
import com.example.vahanproject.repository.UniversityRepository
import com.example.vahanproject.util.Resource
import kotlinx.coroutines.launch

class MainViewModel(private val universityRepository: UniversityRepository) : ViewModel() {
    var universityList: MutableLiveData<Resource<List<University>>> = MutableLiveData()

    init {
        getUniversity()
    }

    fun getUniversity() = viewModelScope.launch {
        universityList.postValue(Resource.Loading())
        val response = universityRepository.getUniversity()
        if (response.isSuccessful) {
            response.body()?.let { result ->
                universityList.postValue(Resource.Success(result))
                Log.d("TAG", result[0].name)
            }
        }

    }
}