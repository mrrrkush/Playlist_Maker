package com.example.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(var tracksList: List<Track>) : RecyclerView.Adapter<TrackViewHolder>() {

    var filteredTracksList: List<Track> = listOf()
    fun filter(query: String) {
        filteredTracksList = if (query.isEmpty()) {
            tracksList
        } else {
            tracksList.filter {
                it.artistName.contains(query, ignoreCase = true) || it.trackName.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder.create(parent)
    }

    override fun getItemCount(): Int = filteredTracksList.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(filteredTracksList[position])
    }
    fun submitList(tracks: List<Track>) {
        tracksList = tracks
        filteredTracksList = tracks
        notifyDataSetChanged()
    }
}
