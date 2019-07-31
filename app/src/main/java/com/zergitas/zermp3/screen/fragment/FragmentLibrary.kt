package com.zergitas.zermp3.screen.fragment


import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast

import com.zergitas.zermp3.R
import kotlinx.android.synthetic.main.fragment_fragment_library.*
import kotlinx.android.synthetic.main.fragment_fragment_list_song.*

class FragmentLibrary : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fragment_library, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        var item: List<String> = listOf("Playlist", "Artists", "Albums", "Songs")
        lv_library.adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, item)!!

        lv_library.setOnItemClickListener { parent, view, position, id ->
            if (position == 0) {
                val playlistFragment = FragmentPlaylist()
                val transaction = fragmentManager?.beginTransaction()
                if (transaction != null) {
                    transaction.addToBackStack(null)
                    transaction.replace(R.id.frame_View, playlistFragment)
                    transaction.commit()

                }
            }
            if (position == 1) {
                val artistsFragment = FragmentArtists()
                val transaction = fragmentManager?.beginTransaction()
                if (transaction != null) {
                    transaction.addToBackStack(null)
                    transaction.replace(R.id.frame_View, artistsFragment)
                    transaction.commit()
                }

            }
            if (position == 2) {
                val albumsFragment = FragmentAlbum()
                val transaction = fragmentManager?.beginTransaction()
                if (transaction != null) {
                    transaction.addToBackStack(null)
                    transaction.replace(R.id.frame_View, albumsFragment)
                    transaction.commit()
                }
            }
            if (position == 3) {
                val listFragment = FragmentListSong()
                val transaction = fragmentManager?.beginTransaction()
                if (transaction != null) {
                    transaction.addToBackStack(null)
                    transaction.replace(R.id.frame_View, listFragment)
                    transaction.commit()
                }

            }

        }
        super.onActivityCreated(savedInstanceState)
    }

}
