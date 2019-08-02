package com.zergitas.zermp3.screen.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.zergitas.zermp3.R
import com.zergitas.zermp3.data.model.Song
import com.zergitas.zermp3.screen.main.MainActivity
import com.zergitas.zermp3.screen.main.adapter.SongAdapter
import com.zergitas.zermp3.utils.Contants
import com.zergitas.zermp3.widgets.ItemDecoration
import kotlinx.android.synthetic.main.fragment_fragment_list_for_albums.*
import kotlinx.android.synthetic.main.fragment_fragment_listfor_singer.*
import kotlinx.android.synthetic.main.item_albums.*

class FragmentListForAlbums : Fragment() {
    lateinit var listsongforalbumAdapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fragment_list_for_albums, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initView()
        img_Back.setOnClickListener({
            val albumsFragment = FragmentAlbum()
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.frame_View, albumsFragment).commit()

        })
        super.onViewCreated(view, savedInstanceState)
    }

    fun initView() {
        txt_albumName.text = (activity as MainActivity).temAlbumName
        txt_number_song_of_album.text = (activity as MainActivity).temNumberSongofALbum.toString() + " song(s)"
        rc_songs_album.layoutManager =
            LinearLayoutManager(activity?.baseContext, LinearLayoutManager.VERTICAL, false)
        rc_songs_album.addItemDecoration(
            ItemDecoration(
                resources.getDimensionPixelSize(R.dimen._3sdp),
                0,
                resources.getDimensionPixelSize(R.dimen._1sdp),
                resources.getDimensionPixelSize(R.dimen._1sdp)
            )
        )

        listsongforalbumAdapter = SongAdapter((activity as MainActivity).listSongByIdAlbum!!, activity!!,
            object : SongAdapter.ItemSongListener {
                override fun onClick(pos: Int, id: Long) {
                    activity!!.intent.putParcelableArrayListExtra(
                        Contants.EXTRA_SONGS,
                        (activity as MainActivity).listSongByIdAlbum as ArrayList<Song>
                    )
                    activity!!.intent.putExtra(Contants.EXTRA_POSITION, pos)

                    val playSong = FragmentPlaySong()
                    val transaction = fragmentManager?.beginTransaction()
                    if (transaction != null) {
                        transaction.addToBackStack(null)
                        transaction.replace(R.id.frame_View, playSong)
                        transaction.commit()
                    }

                }
            })
        rc_songs_album.adapter = listsongforalbumAdapter
    }


}
