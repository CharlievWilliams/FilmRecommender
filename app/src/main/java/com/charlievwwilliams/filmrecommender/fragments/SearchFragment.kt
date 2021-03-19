package com.charlievwwilliams.filmrecommender.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.FileProvider
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.charlievwwilliams.filmrecommender.R
import com.charlievwwilliams.filmrecommender.databinding.FragmentSearchBinding
import com.charlievwwilliams.filmrecommender.extensions.MyAdapter
import com.charlievwwilliams.filmrecommender.extensions.observeEvent
import com.charlievwwilliams.filmrecommender.model.search.Search
import com.charlievwwilliams.filmrecommender.viewmodels.SearchViewModel
import com.charlievwwilliams.filmrecommender.viewstates.MainNavigationEffect.NavigateToResultEffect
import com.charlievwwilliams.filmrecommender.viewstates.MainViewEffect.FilmSearchedEffect
import com.charlievwwilliams.filmrecommender.viewstates.MainViewEffect.OpenCameraEffect
import com.charlievwwilliams.filmrecommender.viewstates.MainViewEvent.*
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class SearchFragment : Fragment(), MyAdapter.OnItemClickListener {

    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: SearchViewModel

    private lateinit var currentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
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
        binding.titleEditText.requestFocus()
        viewModel.viewState().observe(viewLifecycleOwner, {
            binding.searchButton.setOnClickListener {
                viewModel.onEvent(TakePicturePressedEvent)
            }
            binding.titleEditText.setOnEditorActionListener { _, action, _ ->
                if (action == EditorInfo.IME_ACTION_DONE) {
                    viewModel.onEvent(SubmitPressedEvent(binding.titleEditText.text.toString()))
                    binding.motionLayout.transitionToEnd()
                }
                false
            }
            if (it.isLoading) binding.loading.show() else binding.loading.hide()
        })
    }

    private fun setupViewEvents() {
        viewModel.onEvent(ScreenLoadEvent)
    }

    private fun setupViewEffects() {
        viewModel.getViewEffect().observeEvent(viewLifecycleOwner, {
            when (it) {
                is OpenCameraEffect -> dispatchTakePictureIntent()
                is FilmSearchedEffect -> setupRecyclerView(it.results)
            }
        })
    }

    private fun setupNavigationEffects() {
        viewModel.getNavigationEffect().observeEvent(viewLifecycleOwner, {
            when (it) {
                is NavigateToResultEffect -> navigateToResult(it.id)
            }
        })
    }

    private fun navigateToResult(id: String) {
        findNavController().navigate(
            R.id.action_mainFragment_to_searchResultFragment,
            SearchResultFragmentArgs(id).toBundle()
        )
    }

    private fun setupRecyclerView(results: Search) {
        val pageSize = results.results.size
        val titleArray = Array(pageSize) { "" }
        val descriptionArray = Array(pageSize) { "" }
        val imageArray = Array(pageSize) { "" }
        val idArray = Array(pageSize) { "" }
        for (i in 0 until pageSize) {
            titleArray[i] = results.results[i].title
            descriptionArray[i] = results.results[i].overview
            imageArray[i] = results.results[i].poster_path
            idArray[i] = results.results[i].id.toString()
        }
        val adapter = MyAdapter(titleArray, descriptionArray, imageArray, idArray, this)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onItemClick(id: String) {
        viewModel.onEvent(ItemPressedEvent(id))
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.charlievwwilliams.filmrecommender.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = BitmapFactory.decodeFile(currentPhotoPath)
            extractTextFromImage(imageBitmap)
        }
    }

    private fun extractTextFromImage(bitmap: Bitmap) {
        // Rotate image for portrait text recognition
        val matrix = Matrix();
        matrix.postRotate(90F);
        val rotatedImg = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true);
        bitmap.recycle();

        val image = FirebaseVisionImage.fromBitmap(rotatedImg)
        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer

        detector.processImage(image).addOnSuccessListener { firebaseVisionText ->
            viewModel.onEvent(SubmitPressedEvent(firebaseVisionText.text))
            binding.motionLayout.transitionToEnd()
            binding.titleEditText.setText(firebaseVisionText.text)
        }.addOnFailureListener { }
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }
}