package com.example.playlistmaker.ui.track

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.track.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val artistName = itemView.findViewById<TextView>(R.id.trackArtist)
    private val trackName = itemView.findViewById<TextView>(R.id.trackTitle)
    private val trackTime = itemView.findViewById<TextView>(R.id.trackLength)
    private val trackImage = itemView.findViewById<ImageView>(R.id.trackCover)

    fun bind(model: Track) {
        artistName.text = model.artistName
        trackName.text = model.trackName
        trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)

        Glide.with(itemView.context)
            .load(model.artworkUrl60)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.radius_2dp)))
            .into(trackImage)
    }

}