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
import kotlinx.android.synthetic.main.fragment_fragment_list_for_playlist.*
import kotlinx.android.synthetic.main.fragment_fragment_listfor_singer.*

class FragmentListforSinger : Fragment() {
    private lateinit var listsongforSingeradapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fragment_listfor_singer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        img_leftback.setOnClickListener({
            val artistsFragment = FragmentArtists()
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.frame_View, artistsFragment).commit()

        })

        super.onViewCreated(view, savedInstanceState)
    }

    fun initView() {
        txt_singerName.text = (activity as MainActivity).temSingername
        txt_number_song_of_singer.text = (activity as MainActivity).temNumberSongofSinger.toString() + " song(s)"
        rc_songs_singer.layoutManager =
            LinearLayoutManager(activity?.baseContext, LinearLayoutManager.VERTICAL, false)
        rc_songs_singer.addItemDecoration(
            ItemDecoration(
                resources.getDimensionPixelSize(R.dimen._3sdp),
                0,
                resources.getDimensionPixelSize(R.dimen._1sdp),
                resources.getDimensionPixelSize(R.dimen._1sdp)
            )
        )
        listsongforSingeradapter = SongAdapter((activity as MainActivity).listSongBySinger!!, activity!!,
            object : SongAdapter.ItemSongListener {
                override fun onClick(pos: Int, id: Long) {
                    activity!!.intent.putParcelableArrayListExtra(
                        Contants.EXTRA_SONGS,
                        (activity as MainActivity).listSongBySinger as ArrayList<Song>
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

        rc_songs_singer.adapter = listsongforSingeradapter

    }


}
