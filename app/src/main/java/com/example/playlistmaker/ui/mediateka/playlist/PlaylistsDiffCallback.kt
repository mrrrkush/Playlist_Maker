package com.example.playlistmaker.ui.mediateka.playlist

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.domain.model.playlist.Playlist

class PlaylistsDiffCallback(
    private val oldList: List<Playlist>,
    private val newList: List<Playlist>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPlaylist = oldList[oldItemPosition]
        val newPlaylist = newList[newItemPosition]
        return oldPlaylist.id == newPlaylist.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPlaylist = oldList[oldItemPosition]
        val newPlaylist = newList[newItemPosition]
        return oldPlaylist == newPlaylist
    }
}