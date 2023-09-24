package com.example.vahanproject

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vahanproject.models.University
import com.example.vahanproject.repository.UniversityRepository
import com.example.vahanproject.util.ConnectivityObserver
import com.example.vahanproject.util.NetworkConnectivityObserver
import com.example.vahanproject.util.Resource
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import java.net.SocketTimeoutException

class MainViewModel(
    private val universityRepository: UniversityRepository, val context: Context
) : ViewModel() {
    var universityList: MutableLiveData<Resource<List<University>>> = MutableLiveData()

    // Define LiveData to handle errors
    val errorMessageLiveData = MutableLiveData<String>()

    // LiveData or State to hold the current connectivity status
    private lateinit var connectivityObserver: ConnectivityObserver
    val _connectivityStatus = MutableLiveData(ConnectivityObserver.Status.Idle)

    init {
        observeConnectivity()
        getUniversity()
    }

    fun getUniversity() = viewModelScope.launch {
        universityList.postValue(Resource.Loading())
        if (_connectivityStatus.value == ConnectivityObserver.Status.Available || _connectivityStatus.value == ConnectivityObserver.Status.Idle) {
            try {
                val response = universityRepository.getUniversity()
                if (response.isSuccessful) {
                    response.body()?.let { result ->
                        universityList.postValue(Resource.Success(result))
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
                errorMessageLiveData.postValue("HTTP error: ${e.code()}")
                // Handle HTTP errors (e.g., 404, 500)
            } catch (e: Exception) {
                Log.e("ERROR", "An Error occur")
            }
        }
    }


    private fun observeConnectivity() {
        connectivityObserver = NetworkConnectivityObserver(context)
        viewModelScope.launch {
            connectivityObserver.observe()
                .collect { status ->
                    // Update the connectivity status when it changes
                    _connectivityStatus.postValue(status)
                }
        }
    }


}