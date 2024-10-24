package com.example.playlistmaker.ui.mediateka.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentBasePlaylistBinding
import com.example.playlistmaker.domain.model.playlist.Playlist

class NewPlaylistFragment : BasePlaylistFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBasePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {

        binding.pickImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.buttonCreatePlaylist.setOnClickListener {
            val playlist = Playlist(
                title = binding.editTextPlaylistTitle.text.toString(),
                description = binding.editTextPlaylistDescription.text.toString(),
                imageUri = imageUri,
                trackList = "",
                size = 0
            )

            viewModel.savePlaylist(playlist)
            imageUri?.let { viewModel.saveToLocalStorage(uri = it) }

            Toast.makeText(
                requireContext(),
                String.format(getString(R.string.playlist_created), playlist.title),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().popBackStack()
        }

        binding.newPlaylistToolbar.title = getString(R.string.new_playlist)
        binding.newPlaylistToolbar.setOnClickListener {
            if (binding.editTextPlaylistTitle.text.toString().isNotEmpty() ||
                binding.editTextPlaylistDescription.text.toString()
                    .isNotEmpty() || imageUri != null
            )
                showConfirmDialog()
            else {
                findNavController().navigateUp()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (imageUri != null ||
                        !binding.editTextPlaylistTitle.text.isNullOrEmpty() ||
                        !binding.editTextPlaylistDescription.text.isNullOrEmpty()
                    ) {
                        showConfirmDialog()
                    } else {
                        findNavController().navigateUp()
                    }
                }
            })

        binding.editTextPlaylistTitle.addTextChangedListener(textWatcher)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}