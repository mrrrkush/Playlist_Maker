package com.example.playlistmaker.ui.mediateka.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.domain.model.playlist.Playlist

class PlaylistAdapter(private val clickListener: PlaylistClickListener) :
    RecyclerView.Adapter<PlaylistViewHolder>() {

    private var playlists = emptyList<Playlist>()

    fun setPlaylists(content: List<Playlist>) {
        playlists = content
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = PlaylistItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PlaylistViewHolder(binding)
    }

    override fun getItemCount() = playlists.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { clickListener.onClick(playlists[position]) }
    }

    fun interface PlaylistClickListener {
        fun onClick(playlist: Playlist)
    }
}

