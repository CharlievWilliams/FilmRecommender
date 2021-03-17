package com.charlievwwilliams.filmrecommender.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.charlievwwilliams.filmrecommender.databinding.FragmentRecommendationsBinding
import com.charlievwwilliams.filmrecommender.extensions.observeEvent
import com.charlievwwilliams.filmrecommender.model.movies.details.Details
import com.charlievwwilliams.filmrecommender.viewmodels.RecommendationsViewModel
import com.charlievwwilliams.filmrecommender.viewstates.RecommendationsViewEffect
import com.charlievwwilliams.filmrecommender.viewstates.RecommendationsViewEffect.ScreenLoadedEffect
import com.charlievwwilliams.filmrecommender.viewstates.RecommendationsViewEvent
import com.charlievwwilliams.filmrecommender.viewstates.RecommendationsViewEvent.ScreenLoadingEvent
import com.charlievwwilliams.filmrecommender.viewstates.SearchResultViewEffect
import org.koin.android.ext.android.bind
import org.koin.androidx.viewmodel.ext.android.getViewModel

class RecommendationsFragment : Fragment() {

    private var _binding: FragmentRecommendationsBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: RecommendationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecommendationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModel()
        setupViewState()
        setupViewEvents()
        setupViewEffects()
        setupNavigationEffects()
    }

    private fun setupViewState() {
        viewModel.viewState().observe(viewLifecycleOwner, { viewState ->

        })
    }

    private fun setupViewEvents() {
        arguments?.let {
            val safeArgs = SearchResultFragmentArgs.fromBundle(it)
            viewModel.onEvent(ScreenLoadingEvent(safeArgs.id))
        }
    }

    private fun setupViewEffects() {
        viewModel.getViewEffect().observeEvent(viewLifecycleOwner, {
            when (it) {
                is ScreenLoadedEffect -> setupLayout(it.title)
            }
        })
    }

    private fun setupNavigationEffects() {
        viewModel.getNavigationEffect().observeEvent(viewLifecycleOwner, {

        })
    }

    private fun setupLayout(input: String) {
        binding.textView.text = input
    }
}