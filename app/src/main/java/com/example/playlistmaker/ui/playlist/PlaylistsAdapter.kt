package com.example.playlistmaker.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.playlist.Playlist

class PlaylistsAdapter(private val viewObject: ViewObjects) :
    RecyclerView.Adapter<PlaylistsViewHolder>() {

    var onPlayListClicked: ((playlist: Playlist) -> Unit)? = null

    var playlists = mutableListOf<Playlist>()
        set(newPlaylists) {
            val diffCallback = PlaylistsDiffCallback(field, newPlaylists)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newPlaylists
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        return if (viewObject == ViewObjects.Horizontal) {
            PlaylistsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false))
        } else {
            PlaylistsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bottom_sheet_playlists_item, parent, false))
        }
    }

    override fun getItemCount() = playlists.size

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            onPlayListClicked?.invoke(playlists[holder.adapterPosition])
        }
    }
}