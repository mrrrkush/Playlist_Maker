package com.example.playlistmaker.ui.mediateka.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.SearchTrackBinding
import com.example.playlistmaker.domain.model.track.Track
import java.text.SimpleDateFormat
import java.util.Locale

class DetailPlaylistAdapter(private val clickListener: TrackClickListener) :
    RecyclerView.Adapter<TrackViewHolder>() {

    private var tracks = emptyList<Track>()

    fun setTracks(content: List<Track>) {
        tracks = content
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = SearchTrackBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(tracks[position]) }
        holder.itemView.setOnLongClickListener { clickListener.onTrackLongClick(tracks[position]) }
    }

    override fun getItemCount() = tracks.size

    interface TrackClickListener {
        fun onTrackClick(track: Track)

        fun onTrackLongClick(track: Track): Boolean
    }
}

class TrackViewHolder(private val binding: SearchTrackBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {
        with(binding) {
            with(track) {
                trackTitle.text = track.trackName
                trackArtist.text = track.artistName
                trackLength.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
                Glide.with(itemView)
                    .load(getCoverArtwork(track.artworkUrl100))
                    .placeholder(R.drawable.placeholder)
                    .transform(
                        CenterCrop(),
                        RoundedCorners(
                            itemView.resources.getDimensionPixelSize(R.dimen.radius_2dp)
                        ),
                    )
                    .into(trackCover)
            }
        }
    }

    private fun getCoverArtwork(artworkUrl: String?) =
        artworkUrl?.replaceAfterLast('/', "60x60bb.jpg")
}