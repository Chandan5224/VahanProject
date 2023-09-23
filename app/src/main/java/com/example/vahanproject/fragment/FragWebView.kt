package com.example.vahanproject.fragment

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.lifecycle.withStarted
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.vahanproject.R
import com.example.vahanproject.databinding.FragmentFragWebViewBinding
import com.google.android.material.snackbar.Snackbar


class FragWebView : Fragment() {
    lateinit var binding: FragmentFragWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFragWebViewBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val url = arguments!!.getString("url").toString()

        binding.webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.progressBar.visibility = View.VISIBLE
                binding.webView.visibility = View.GONE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressBar.visibility = View.GONE
                binding.webView.visibility = View.VISIBLE
            }
        }
        // Allow HTTP traffic (not recommended for production)
        // Enable JavaScript (optional)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        binding.webView.loadUrl(url)
        return binding.root
    }


}