package com.zergitas.zermp3.screen.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
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
import kotlinx.android.synthetic.main.fragment_fragment_favourite_songs.*

private const val Tag = "FRAGMENT_FAVOURITE"

class FragmentFavouriteSongs : Fragment() {
    private lateinit var favouriteadapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fragment_favourite_songs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRecyclerViewSongs()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initRecyclerViewSongs() {
        rc_songs_favourite.layoutManager =
            LinearLayoutManager(activity?.baseContext, LinearLayoutManager.VERTICAL, false)
        rc_songs_favourite.addItemDecoration(
            ItemDecoration(
                resources.getDimensionPixelSize(R.dimen._3sdp),
                0,
                resources.getDimensionPixelSize(R.dimen._1sdp),
                resources.getDimensionPixelSize(R.dimen._1sdp)
            )
        )
        favouriteadapter = SongAdapter((activity as MainActivity).favouriteList!!, activity!!,

            object : SongAdapter.ItemSongListener {
                override fun onClick(pos: Int, id: Long) {
                    activity!!.intent.putParcelableArrayListExtra(
                        Contants.EXTRA_SONGS,
                        (activity as MainActivity).favouriteList as ArrayList<Song>
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
        rc_songs_favourite.adapter = favouriteadapter
    }

}

