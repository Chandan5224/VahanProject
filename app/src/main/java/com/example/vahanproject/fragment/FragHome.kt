package com.example.vahanproject.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vahanproject.MainActivity
import com.example.vahanproject.MainViewModel
import com.example.vahanproject.R
import com.example.vahanproject.adapter.OnItemsClick
import com.example.vahanproject.adapter.UniversityAdapter
import com.example.vahanproject.api.RetrofitInstance
import com.example.vahanproject.databinding.FragmentFragHomeBinding
import com.example.vahanproject.models.University
import com.example.vahanproject.repository.UniversityRepository
import com.example.vahanproject.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragHome : Fragment(), OnItemsClick {
    lateinit var binding: FragmentFragHomeBinding
    lateinit var viewModel: MainViewModel
    lateinit var mAdapter: UniversityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFragHomeBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        viewModel.universityList.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvUniversity.visibility = View.VISIBLE
                    response.data?.let { unResponse ->
                        mAdapter.differ.submitList(unResponse)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.rvUniversity.visibility = View.VISIBLE
                    response.message?.let { message ->
                        Log.e(TAG, "An error occurred : $message")

                    }
                }

                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rvUniversity.visibility = View.GONE
                }
                else -> {}
            }

        })
        return binding.root
    }

    private fun setupRecyclerView() {
        mAdapter = UniversityAdapter(this@FragHome)
        binding.rvUniversity.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onClickWebsite(link: String) {
        link.trim()
        val bundle = Bundle()
        bundle.putString("url", link)
        findNavController().navigate(R.id.action_fragHome_to_fragWebView, bundle)
    }
}