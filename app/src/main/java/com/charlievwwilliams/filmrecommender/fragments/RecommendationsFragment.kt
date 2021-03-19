package com.charlievwwilliams.filmrecommender.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.charlievwwilliams.filmrecommender.databinding.FragmentRecommendationsBinding
import com.charlievwwilliams.filmrecommender.extensions.MyAdapter
import com.charlievwwilliams.filmrecommender.extensions.observeEvent
import com.charlievwwilliams.filmrecommender.model.django.DjangoItem
import com.charlievwwilliams.filmrecommender.viewmodels.RecommendationsViewModel
import com.charlievwwilliams.filmrecommender.viewstates.RecommendationsViewEffect.ScreenLoadedEffect
import com.charlievwwilliams.filmrecommender.viewstates.RecommendationsViewEvent.ScreenLoadingEvent
import org.koin.androidx.viewmodel.ext.android.getViewModel

class RecommendationsFragment : Fragment(), MyAdapter.OnItemClickListener {

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
            if (viewState.isLoading) binding.loading.show() else binding.loading.hide()
        })
    }

    private fun setupViewEvents() {
        arguments?.let {
            val safeArgs = RecommendationsFragmentArgs.fromBundle(it)
            viewModel.onEvent(
                ScreenLoadingEvent(
                    safeArgs.id,
                    safeArgs.useTitle,
                    safeArgs.useGenres,
                    safeArgs.useProductionCompanies,
                    safeArgs.useSpokenLanguages,
                    safeArgs.useKeywords,
                    safeArgs.useCredits
                )
            )
        }
    }

    private fun setupViewEffects() {
        viewModel.getViewEffect().observeEvent(viewLifecycleOwner, {
            when (it) {
                is ScreenLoadedEffect -> setupRecyclerView(it.recommendedFilms)
            }
        })
    }

    private fun setupNavigationEffects() {
        viewModel.getNavigationEffect().observeEvent(viewLifecycleOwner, {

        })
    }

    private fun setupRecyclerView(results: ArrayList<DjangoItem>) {
        val pageSize = results.size
        val titleArray = Array(pageSize) { "" }
        val descriptionArray = Array(pageSize) { "" }
        val imageArray = Array(pageSize) { "" }
        val idArray = Array(pageSize) { "" }
        for (i in 0 until pageSize) {
            titleArray[i] = results[i].title
            descriptionArray[i] = results[i].overview
            imageArray[i] = results[i].poster_path
            idArray[i] = results[i].id
        }
        val adapter = MyAdapter(titleArray, descriptionArray, imageArray, idArray, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onItemClick(id: String) {
        TODO("Not yet implemented")
    }
}