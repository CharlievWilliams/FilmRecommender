package com.charlievwwilliams.filmrecommender.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.charlievwwilliams.filmrecommender.databinding.FragmentMainBinding
import com.charlievwwilliams.filmrecommender.extensions.MyAdapter
import com.charlievwwilliams.filmrecommender.extensions.observeEvent
import com.charlievwwilliams.filmrecommender.model.search.Search
import com.charlievwwilliams.filmrecommender.viewmodels.MainViewModel
import com.charlievwwilliams.filmrecommender.viewstates.MainViewEffect.FilmSearchedEffect
import com.charlievwwilliams.filmrecommender.viewstates.MainViewEvent.ScreenLoadEvent
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
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
        viewModel.viewState().observe(viewLifecycleOwner, {
            binding.button.setOnClickListener { dispatchTakePictureIntent() } // TODO: MVVM this
        })
    }

    private fun setupViewEvents() {
        viewModel.onEvent(ScreenLoadEvent)
    }

    private fun setupViewEffects() {
        viewModel.getViewEffect().observeEvent(viewLifecycleOwner, {
            when (it) {
                is FilmSearchedEffect -> {
                    setupRecyclerView(it.results)
                }
            }
        })
    }

    private fun setupNavigationEffects() {
        viewModel.getNavigationEffect().observeEvent(viewLifecycleOwner, {
            when (it) {
                // TODO: Add to this
            }
        })
    }

    private fun setupRecyclerView(results: Search) {
        val pageSize = results.results.size
        val filmArray = Array(pageSize) { "" }
        for (i in 0 until pageSize) {
            filmArray[i] = results.results[i].title
        }
        val adapter = MyAdapter(filmArray)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
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

    lateinit var currentPhotoPath: String

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
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
            binding.imageView.setImageBitmap(imageBitmap)
            extractTextFromImage(imageBitmap)
        }
    }

    private fun extractTextFromImage(bitmap: Bitmap) {

        val image = FirebaseVisionImage.fromBitmap(bitmap)

        val detector = FirebaseVision.getInstance().onDeviceTextRecognizer

        detector.processImage(image).addOnSuccessListener { firebaseVisionText ->
            // Get Each block of text i.e (Paragraph)
            for (block in firebaseVisionText.textBlocks) {
                val blockText = block.text
                binding.textView.append(
                    """
                $blockText
                
                
                """.trimIndent()
                ) //Separate each paragraph by new line
            }
            viewModel.tempFunction(firebaseVisionText.text)
        }.addOnFailureListener { }
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }
}