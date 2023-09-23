package com.example.vahanproject.fragment

import android.animation.Animator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.vahanproject.MainActivity
import com.example.vahanproject.R
import com.example.vahanproject.databinding.FragmentFragSplashScreenBinding

class FragSplashScreen : Fragment() {

    lateinit var binding: FragmentFragSplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFragSplashScreenBinding.inflate(layoutInflater)
        (activity as MainActivity).actionBar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding.splashLogo.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {

            }

            override fun onAnimationEnd(p0: Animator) {
                findNavController().navigate(R.id.action_fragSplashScreen_to_fragHome)
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {

            }

        })
        return binding.root
    }

}