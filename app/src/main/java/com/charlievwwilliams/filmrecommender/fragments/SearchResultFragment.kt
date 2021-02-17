package com.charlievwwilliams.filmrecommender.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.charlievwwilliams.filmrecommender.databinding.FragmentSearchResultBinding
import com.charlievwwilliams.filmrecommender.extensions.observeEvent
import com.charlievwwilliams.filmrecommender.viewmodels.MainViewModel
import com.charlievwwilliams.filmrecommender.viewstates.MainViewEvent.ScreenLoadEvent
import org.koin.androidx.viewmodel.ext.android.getViewModel

class SearchResultFragment : Fragment() {

    private var _binding: FragmentSearchResultBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

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
        arguments?.let {
            val safeArgs = SearchResultFragmentArgs.fromBundle(it)
            viewModel.viewState().observe(viewLifecycleOwner, {
                Toast.makeText(requireContext(), safeArgs.id, Toast.LENGTH_LONG).show()
            })
        }
    }

    private fun setupViewEvents() {

    }

    private fun setupViewEffects() {
        viewModel.getViewEffect().observeEvent(viewLifecycleOwner, {

        })
    }

    private fun setupNavigationEffects() {
        viewModel.getNavigationEffect().observeEvent(viewLifecycleOwner, {

        })
    }
}