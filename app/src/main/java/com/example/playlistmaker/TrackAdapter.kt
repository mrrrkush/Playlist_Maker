package com.example.playlistmaker

import android.content.Intent
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
        Log.d("TrackAdapter", "Track country: ${track.country}")
        holder.bind(track)
        holder.itemView.setOnClickListener {
            Log.d("TrackAdapter", "Clicked on position: $position, Track: ${track.trackName}")
            searchHistory.saveTrack(track)

                val context = holder.itemView.context
                val intent = Intent(context, AudioPlayerActivity::class.java)
                intent.putExtra("trackId", track.trackId)
                intent.putExtra("trackName", track.trackName)
                intent.putExtra("artistName", track.artistName)
                intent.putExtra("trackTimeMillis", track.trackTimeMillis)
                intent.putExtra("artworkUrl100", track.artworkUrl100)
                intent.putExtra("collectionName", track.collectionName)
                intent.putExtra("releaseDate", track.releaseDate)
                intent.putExtra("primaryGenreName", track.primaryGenreName)
                intent.putExtra("country", track.country)
                context.startActivity(intent)
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

