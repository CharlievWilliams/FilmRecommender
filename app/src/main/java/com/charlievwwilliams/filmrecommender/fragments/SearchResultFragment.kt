package com.charlievwwilliams.filmrecommender.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.charlievwwilliams.filmrecommender.R
import com.charlievwwilliams.filmrecommender.databinding.FragmentSearchResultBinding
import com.charlievwwilliams.filmrecommender.extensions.observeEvent
import com.charlievwwilliams.filmrecommender.model.movies.details.Details
import com.charlievwwilliams.filmrecommender.utils.Constants.Companion.FILM_IMAGE_ORIGINAL
import com.charlievwwilliams.filmrecommender.utils.Constants.Companion.FILM_IMAGE_SMALL
import com.charlievwwilliams.filmrecommender.viewmodels.SearchResultViewModel
import com.charlievwwilliams.filmrecommender.viewstates.SearchResultNavigationEffect.NavigateToRecommendationsEffect
import com.charlievwwilliams.filmrecommender.viewstates.SearchResultViewEffect.FilmSearchedEffect
import com.charlievwwilliams.filmrecommender.viewstates.SearchResultViewEvent.GetRecommendationsPressedEvent
import com.charlievwwilliams.filmrecommender.viewstates.SearchResultViewEvent.ScreenLoadingEvent
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
                    GetRecommendationsPressedEvent(viewModel.viewState().value?.id ?: "")
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
        with(binding) {
            titleTextView.text = input.title
            descriptionTextView.text = input.overview
            genreTextView.text = input.genres[0].name
            ratingsBar.rating =
                (input.vote_average / 2).toFloat() // 10 star system to 5 star system
            runtimeTextView.text = input.runtime.toString() + " Minutes"
            releaseDateTextView.text =
                "Released " + input.release_date.take(4) // We only want the year
            Picasso.get().load(FILM_IMAGE_SMALL + input.poster_path)
                .into(posterImageView)
            Picasso.get().load(FILM_IMAGE_ORIGINAL + input.backdrop_path)
                .into(backdropImageView)

            // Make all elements load in at the same time
            ratingsBar.isVisible = true
            recommendationTitleTextView.isVisible = true
            chipGroup.isVisible = true
            getRecommendationsButton.isVisible = true
        }
    }

    private fun navigateToRecommendations(id: String) {
        findNavController().navigate(
            R.id.action_searchResultFragment_to_recommendationsFragment,
            RecommendationsFragmentArgs(
                id,
                binding.chip1.isChecked,
                binding.chip2.isChecked,
                binding.chip3.isChecked,
                binding.chip4.isChecked,
                binding.chip5.isChecked,
                binding.chip6.isChecked
            ).toBundle()
        )
    }
}