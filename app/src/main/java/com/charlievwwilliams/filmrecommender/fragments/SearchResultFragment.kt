package com.charlievwwilliams.filmrecommender.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.charlievwwilliams.filmrecommender.R
import com.charlievwwilliams.filmrecommender.databinding.FragmentSearchResultBinding
import com.charlievwwilliams.filmrecommender.extensions.observeEvent
import com.charlievwwilliams.filmrecommender.model.movies.details.Details
import com.charlievwwilliams.filmrecommender.viewmodels.SearchResultViewModel
import com.charlievwwilliams.filmrecommender.viewstates.SearchResultNavigationEffect.NavigateToRecommendationsEffect
import com.charlievwwilliams.filmrecommender.viewstates.SearchResultViewEffect.FilmSearchedEffect
import com.charlievwwilliams.filmrecommender.viewstates.SearchResultViewEvent.*
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.getViewModel

class SearchResultFragment : Fragment() {

    private var _binding: FragmentSearchResultBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: SearchResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
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
            if (viewState.isLoading) binding.loading.show() else binding.loading.hide()
            binding.getRecommendationsButton.setOnClickListener {
                viewModel.onEvent(
                    GetRecommendationsPressedEvent(viewModel.viewState().value?.imdbID ?: "")
                )
            }
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
                is FilmSearchedEffect -> setupLayout(it.results)
            }
        })
    }

    private fun setupNavigationEffects() {
        viewModel.getNavigationEffect().observeEvent(viewLifecycleOwner, {
            when (it) {
                is NavigateToRecommendationsEffect -> navigateToRecommendations(it.id)
            }
        })
    }


    private fun setupLayout(input: Details) {
        binding.titleTextView.text = input.title
        binding.descriptionTextView.text = input.overview
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + input.poster_path)
            .into(binding.posterImageView)
        Picasso.get().load("https://image.tmdb.org/t/p/original" + input.backdrop_path)
            .into(binding.backdropImageView)
        viewModel.onEvent(ScreenLoadEvent)
    }

    private fun navigateToRecommendations(id: String) {
        // TODO: Navigate to recommendations screen
    }
}