package com.charlievwwilliams.filmrecommender.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.charlievwwilliams.filmrecommender.R
import com.charlievwwilliams.filmrecommender.databinding.FragmentSplashBinding
import com.charlievwwilliams.filmrecommender.extensions.observeEvent
import com.charlievwwilliams.filmrecommender.viewmodels.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModel()
        setupViewState()
        setupViewEvents()
        setupNavigationEffects()
        setupAnimation()
    }

    private fun setupAnimation() {
        binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                navigateToApp()
            }
        })
    }

    private fun setupViewState() {
        viewModel.viewState().observe(viewLifecycleOwner, { viewState ->
            // TODO: Make all the splash stuff follow MVVM
        })
    }

    private fun setupViewEvents() {
        arguments?.let {

        }
    }

    private fun setupNavigationEffects() {
        viewModel.getNavigationEffect().observeEvent(viewLifecycleOwner, {
            when (it) {

            }
        })
    }

    private fun navigateToApp() {
        findNavController().navigate(
            R.id.action_splashFragment_to_mainFragment
        )
    }
}