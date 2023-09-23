package com.example.vahanproject

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vahanproject.models.University
import com.example.vahanproject.repository.UniversityRepository
import com.example.vahanproject.util.Resource
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

class MainViewModel(
    private val universityRepository: UniversityRepository
) :
    ViewModel() {
    var universityList: MutableLiveData<Resource<List<University>>> = MutableLiveData()

    // Define LiveData to handle errors
    val errorMessageLiveData = MutableLiveData<String>()

    // LiveData or State to hold the current connectivity status
//    private lateinit var connectivityObserver: ConnectivityObserver
//    val _connectivityStatus = MutableLiveData(ConnectivityObserver.Status.Idle)

    init {
        getUniversity()
    }

    fun getUniversity() = viewModelScope.launch {
            universityList.postValue(Resource.Loading())
            try {
                val response = universityRepository.getUniversity()
                if (response.isSuccessful) {
                    response.body()?.let { result ->
                        universityList.postValue(Resource.Success(result))
                        Log.d("TAG", result[0].name)
                    }
                } else {
                    // Handle unsuccessful response (e.g., server error)
                    errorMessageLiveData.postValue("Server error: ${response.code()}")
                }
            } catch (e: SocketTimeoutException) {
                // Handle SocketTimeoutException (timeout error)
                errorMessageLiveData.postValue("Timeout error: The request timed out")
                Log.d("TIMEOUT", "SocketTimeoutException")
            } catch (e: HttpException) {
                // Handle HTTP errors (e.g., 404, 500)
                errorMessageLiveData.postValue("HTTP error: ${e.code()}")
            } catch (e: Exception) {
                // Handle other exceptions
                errorMessageLiveData.postValue("An error occurred")
            }
    }

//    private fun observeConnectivity() {
//        connectivityObserver = NetworkConnectivityObserver(context)
//        viewModelScope.launch {
//            connectivityObserver.observe()
//                .collect { status ->
//                    // Update the connectivity status when it changes
//                    _connectivityStatus.postValue(status)
//                }
//        }
//    }

    companion object {
        // Singleton instance of the ViewModel
        private var instance: MainViewModel? = null

        fun getInstance(repository: UniversityRepository): MainViewModel {
            return instance ?: synchronized(this) {
                instance ?: MainViewModel(repository).also { instance = it }
            }
        }
    }
}