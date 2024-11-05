package com.example.playlistmaker.ui.mediateka.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.BottomSheetPlaylistsItemBinding
import com.example.playlistmaker.domain.model.playlist.Playlist

class BottomSheetPlaylistsAdapter(
    private val onPlaylistClicked: PlaylistClickListener
) : RecyclerView.Adapter<BottomSheetPlaylistsViewHolder>() {

    private var playlists = emptyList<Playlist>()

    fun setPlaylists(content: List<Playlist>) {
        playlists = content
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BottomSheetPlaylistsViewHolder {
        val binding = BottomSheetPlaylistsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BottomSheetPlaylistsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BottomSheetPlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { onPlaylistClicked.onPlaylistClick(playlists[position]) }
    }

    override fun getItemCount() = playlists.size

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}

class BottomSheetPlaylistsViewHolder(private val binding: BottomSheetPlaylistsItemBinding) :
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
                        RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.search_radius_padding_8dp)),
                    )
                    .into(playlistImage)
            }
        }
    }
}