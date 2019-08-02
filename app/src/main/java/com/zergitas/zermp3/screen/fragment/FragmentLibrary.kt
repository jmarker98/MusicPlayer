package com.zergitas.zermp3.screen.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.zergitas.zermp3.R
import com.zergitas.zermp3.screen.main.MainActivity
import com.zergitas.zermp3.screen.main.adapter.RecentlySongAdapter
import com.zergitas.zermp3.widgets.ItemDecoration
import kotlinx.android.synthetic.main.fragment_fragment_library.*

class FragmentLibrary : Fragment() {
    lateinit var madapter: RecentlySongAdapter
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
                (activity as MainActivity).supportFragmentManager.beginTransaction().addToBackStack("playlist")
                    .replace(R.id.frame_View, playlistFragment).commit()
            }
            if (position == 1) {
                val artistsFragment = FragmentArtists()
                (activity as MainActivity).supportFragmentManager.beginTransaction().addToBackStack("artists")
                    .replace(R.id.frame_View, artistsFragment).commit()

            }
            if (position == 2) {
                val albumsFragment = FragmentAlbum()
                (activity as MainActivity).supportFragmentManager.beginTransaction().addToBackStack("album")
                    .replace(R.id.frame_View, albumsFragment).commit()
            }
            if (position == 3) {
                val listFragment = FragmentListSong()
                (activity as MainActivity).supportFragmentManager.beginTransaction().addToBackStack("list")
                    .replace(R.id.frame_View, listFragment).commit()

            }

        }
        super.onActivityCreated(savedInstanceState)
    }

    fun initView() {
        rc_recently.addItemDecoration(
            ItemDecoration(
                resources.getDimensionPixelSize(R.dimen._4sdp),
                0,
                0,
                0
            )
        )
        madapter = RecentlySongAdapter((activity as MainActivity).songs!!, activity!!,
            object : RecentlySongAdapter.ItemSongListener {
                override fun onClick(pos: Int, id: Long) {

                }


            })
        rc_recently.adapter = madapter
    }
}


