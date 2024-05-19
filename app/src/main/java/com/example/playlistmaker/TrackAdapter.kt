package com.example.playlistmaker

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    private var tracksList: List<Track>,
    private val searchHistory: SearchHistory
) : RecyclerView.Adapter<TrackViewHolder>() {

    private var filteredTracksList: List<Track> = tracksList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = filteredTracksList[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            Log.d("TrackAdapter", "Clicked on position: $position, Track: ${track.trackName}")
            searchHistory.saveTrack(track)
        }
    }

    override fun getItemCount(): Int = filteredTracksList.size

    fun submitList(newTrackList: List<Track>) {
        tracksList = newTrackList
        filteredTracksList = newTrackList
        notifyDataSetChanged()
    }

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
}

