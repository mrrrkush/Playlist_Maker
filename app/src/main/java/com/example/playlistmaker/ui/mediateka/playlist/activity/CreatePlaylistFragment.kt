package com.example.playlistmaker.ui.mediateka.playlist.activity

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.example.playlistmaker.ui.mediateka.models.CreatePlaylistState
import com.example.playlistmaker.ui.mediateka.playlist.view_model.CreatePlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistFragment : Fragment() {

    private val viewModel: CreatePlaylistViewModel by viewModel()

    private lateinit var binding: FragmentCreatePlaylistBinding
    private var coverUri: Uri? = null
    private var textWatcher: TextWatcher? = null
    private var playlistId: Int = 0

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                setImageToPlaceHolder(uri)
                coverUri = uri
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setTextWatcher()
        binding.cardPlaceholder.setOnClickListener {
            imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.buttonCreatePlaylist.setOnClickListener {
            viewModel.createPlaylist(
                name = binding.editTextPlaylistTitle.text.toString(),
                description = binding.editTextPlaylistDescription.text.toString(),
                coverUri = coverUri
            )
        }

        binding.newPlaylistToolbar.setNavigationOnClickListener {
           if (playlistId == 0) {
               viewModel.checkBeforeCloseScreen(
                   name = binding.editTextPlaylistTitle.text.toString(),
                   description = binding.editTextPlaylistDescription.text.toString(),
                   uri = coverUri
               )
           } else {
               findNavController().navigateUp()
           }
       }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.checkBeforeCloseScreen(
                name = binding.editTextPlaylistTitle.text.toString(),
                description = binding.editTextPlaylistDescription.text.toString(),
                uri = coverUri
            )
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (playlistId == 0) {
                        viewModel.checkBeforeCloseScreen(
                            name = binding.editTextPlaylistTitle.text.toString(),
                            description = binding.editTextPlaylistDescription.text.toString(),
                            uri = coverUri
                        )
                    } else {
                        findNavController().navigateUp()
                    }
                }
            })
    }

    private fun initView() {
        requireArguments().let {
            playlistId = it.getInt(ARGS_PLAYLIST)

            if (playlistId == 0) {
                binding.buttonCreatePlaylist.text = requireContext().getString(R.string.to_create)
            } else {
                viewModel.getPlaylistById(playlistId)
                binding.buttonCreatePlaylist.text = requireContext().getString(R.string.save)
            }
        }
    }

    private fun renderState(state: CreatePlaylistState) {
        when (state) {
            is CreatePlaylistState.SaveSuccess -> {
                showToast(state.name)
                closePlaylistFragment()
            }

            is CreatePlaylistState.EditInProgress -> {
                if (state.isStarted) {
                    showAlertDialog()
                } else {
                    closePlaylistFragment()
                }
            }

            is CreatePlaylistState.LoadPlaylist -> {
                if (state.playlist.coverUri != null) {
                    setImageToPlaceHolder(state.playlist.coverUri)
                }
                binding.editTextPlaylistTitle.setText(state.playlist.name)
                binding.editTextPlaylistDescription.setText(state.playlist.description)
            }
        }
    }

    private fun showToast(name: String) {
        Toast.makeText(
            requireContext(),
            getString(R.string.playlist_created, name), Toast.LENGTH_SHORT
        ).show()
    }

    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(
            requireContext(),
            R.style.MaterialAlertDialog
        )
            .setTitle(getString(R.string.stop_creating_playlist))
            .setMessage(getString(R.string.unsaved_data_will_be_lost))
            .setPositiveButton(getString(R.string.finish))
            { _, _ -> closePlaylistFragment() }
            .setNegativeButton(getString(R.string.cancel))
            { _, _ -> }
            .show()
    }

    private fun closePlaylistFragment() {
        findNavController().navigateUp()
    }

    private fun setTextWatcher() {
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val isNotEmpty = !s.isNullOrBlank()
                binding.buttonCreatePlaylist.isEnabled = isNotEmpty
                binding.buttonCreatePlaylist.setBackgroundColor(
                    if (isNotEmpty) ContextCompat.getColor(requireContext(), R.color.blue)
                    else ContextCompat.getColor(requireContext(), R.color.grey_icons)
                )
            }
        }
        binding.editTextPlaylistTitle.addTextChangedListener(textWatcher)
    }

    private fun setImageToPlaceHolder(uri: Uri) {
        if (!uri.toString().isNullOrBlank()) {
            Glide.with(binding.root)
                .load(uri)
                .transform(
                    CenterCrop(),
                    RoundedCorners(
                        resources.getDimensionPixelSize(R.dimen.search_radius_padding_8dp)
                    ),
                )
                .into(binding.pickImage)
        }
    }

    companion object {
        private const val ARGS_PLAYLIST = "playlist"

        fun createArgs(playlistId: Int): Bundle =
            bundleOf(ARGS_PLAYLIST to playlistId)
    }
}