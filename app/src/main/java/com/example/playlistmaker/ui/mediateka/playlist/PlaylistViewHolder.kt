package com.example.playlistmaker.ui.mediateka.playlist

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.domain.model.playlist.Playlist

class PlaylistViewHolder(private val binding: PlaylistItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        with(binding) {
            with(playlist) {
                playlistTitle.text = name
                val numOfTracks = itemView.resources.getQuantityString(
                    R.plurals.plural_tracks,
                    numberOfTracks,
                    numberOfTracks
                )
                playlistSize.text = numOfTracks
                Glide.with(itemView)
                    .load(coverUri)
                    .placeholder(R.drawable.placeholder_playlist)
                    .transform(
                        CenterCrop(),
                        RoundedCorners(
                            itemView.resources.getDimensionPixelSize(R.dimen.search_radius_padding_8dp)
                        ),
                    )
                    .into(playlistImage)
            }
        }
    }
}