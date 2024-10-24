package com.example.playlistmaker.ui.mediateka.activity

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentBasePlaylistBinding
import com.example.playlistmaker.domain.model.playlist.Playlist
import java.io.Serializable

class EditPlaylistFragment : BasePlaylistFragment() {

    var playlist: Playlist? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBasePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonEnable()
        initListeners()

        playlist = requireArguments().getSerializableExtra(EDIT_PLAYLIST, Playlist::class.java)

        showPlaylist(playlist!!)

    }

    private fun showPlaylist(playlist: Playlist) {
        with(binding) {
            editTextPlaylistTitle.setText(playlist.title)
            editTextPlaylistDescription.setText(playlist.description)

            imageUri = playlist.imageUri.toString()

            if (playlist.imageUri.toString() != "null") {
                binding.pickImage.setImageURI(playlist.imageUri?.toUri())
            } else {
                context?.let {
                    Glide
                        .with(it)
                        .load(R.drawable.placeholder)
                        .transform(CenterInside())
                        .into(binding.pickImage)
                }
            }
        }

        binding.buttonCreatePlaylist.text = getString(R.string.save)
        binding.newPlaylistToolbar.title = getString(R.string.editing)
    }

    private fun initListeners() {
        binding.pickImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.newPlaylistToolbar.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonCreatePlaylist.setOnClickListener {
            if (playlist != null) {
                val updatedPlaylist = Playlist(
                    id = playlist!!.id,
                    title = binding.editTextPlaylistTitle.text.toString(),
                    description = binding.editTextPlaylistDescription.text.toString(),
                    imageUri = imageUri,
                    trackList = playlist?.trackList!!,
                    size = playlist?.size!!
                )
                viewModel.updatePlaylist(updatedPlaylist)
            }
            findNavController().popBackStack()
        }
    }

    private fun buttonEnable() {
        binding.editTextPlaylistTitle.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                binding.buttonCreatePlaylist.isEnabled = false
                context?.let {
                    ContextCompat.getColor(
                        it,
                        R.color.grey_icons
                    )
                }?.let {
                    binding.buttonCreatePlaylist.setBackgroundColor(
                        it
                    )
                }
            } else {
                binding.buttonCreatePlaylist.isEnabled = true
                context?.let {
                    ContextCompat.getColor(
                        it,
                        R.color.blue
                    )
                }?.let {
                    binding.buttonCreatePlaylist.setBackgroundColor(
                        it
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun <T : Serializable?> Bundle.getSerializableExtra(key: String, m_class: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            this.getSerializable(key, m_class)!!
        else
            this.getSerializable(key) as T
    }

    companion object {
        const val EDIT_PLAYLIST = "EDIT_PLAYLIST"

        fun createArgs(playlist: Playlist): Bundle {
            return bundleOf(EDIT_PLAYLIST to playlist)
        }
    }
}