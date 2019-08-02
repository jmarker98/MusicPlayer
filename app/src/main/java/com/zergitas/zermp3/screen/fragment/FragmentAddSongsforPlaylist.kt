package com.zergitas.zermp3.screen.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.zergitas.zermp3.R
import com.zergitas.zermp3.data.model.Song
import com.zergitas.zermp3.data.source.local.song.db.DatabaseHelper
import com.zergitas.zermp3.screen.main.MainActivity
import com.zergitas.zermp3.screen.main.adapter.AddSongforPlaylistAdapter
import com.zergitas.zermp3.widgets.ItemDecoration
import kotlinx.android.synthetic.main.fragment_fragment_add_songsfor_playlist.*
import kotlinx.android.synthetic.main.fragment_fragment_play_song.*
import kotlinx.android.synthetic.main.item_songs_playlist.*
import kotlin.math.log


class FragmentAddSongsforPlaylist : Fragment() {
    private lateinit var songplaylistadapter: AddSongforPlaylistAdapter
    var listIdsongsAdded: ArrayList<Long>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fragment_add_songsfor_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        img_done.visibility = View.GONE
        onClick()
        initRecyclerViewSongs()
    }

    private fun initRecyclerViewSongs() {
        listIdsongsAdded = ArrayList()

        rc_songAddforPlaylist.layoutManager =
            LinearLayoutManager(activity?.baseContext, LinearLayoutManager.VERTICAL, false)
        rc_songAddforPlaylist.addItemDecoration(
            ItemDecoration(
                resources.getDimensionPixelSize(com.zergitas.zermp3.R.dimen._3sdp),
                0,
                resources.getDimensionPixelSize(com.zergitas.zermp3.R.dimen._1sdp),
                resources.getDimensionPixelSize(R.dimen._1sdp)
            )
        )
        songplaylistadapter = AddSongforPlaylistAdapter((activity as MainActivity).songs!!, activity!!,
            object : AddSongforPlaylistAdapter.ItemSongListener {
                override fun onUnCheck(
                    holder: AddSongforPlaylistAdapter.PlaylistSongViewHolder,
                    item: Int,
                    song: Song
                ) {
                    listIdsongsAdded!!.remove(song.id)
                }

                override fun onCheck(holder: AddSongforPlaylistAdapter.PlaylistSongViewHolder, item: Int, song: Song) {
                    listIdsongsAdded!!.add(song.id)
                    img_done.visibility = View.VISIBLE
                    Log.d("addedforplaylist", song.id.toString())
                }

                override fun onClick(pos: Int) {
                }
            })
        rc_songAddforPlaylist.adapter = songplaylistadapter
    }

    fun addtoDb() {
        for (i in listIdsongsAdded!!) {
            if ((activity as MainActivity).tempPlaylistId > 0) {
                (activity as MainActivity).insertIdsong(i, (activity as MainActivity).tempPlaylistId)
                Log.d("checkidSong", i.toString())
            } else {
                return
            }
        }

    }

    fun onClick() {
        img_cancle.setOnClickListener({
            val playlistFragment = FragmentPlaylist()
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.frame_View, playlistFragment).commit()
        })
        img_done.setOnClickListener({
            addtoDb()
            val playlistFragment = FragmentPlaylist()
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.frame_View, playlistFragment).commit()

        })
    }


}
