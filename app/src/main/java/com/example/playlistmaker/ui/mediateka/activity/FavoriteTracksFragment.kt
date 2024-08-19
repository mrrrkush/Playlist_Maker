package com.example.playlistmaker.ui.mediateka.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavouriteTracksBinding
import com.example.playlistmaker.ui.mediateka.view_model.FavouriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FavouriteTracksFragment : Fragment() {

    private lateinit var binding: FragmentFavouriteTracksBinding
    private val viewModel by activityViewModel<FavouriteTracksViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = FavouriteTracksFragment().apply {}
    }
}