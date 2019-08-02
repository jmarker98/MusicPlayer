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
import kotlinx.android.synthetic.main.fragment_fragment_favourite_songs.*
import kotlinx.android.synthetic.main.fragment_fragment_list_for_playlist.*
import kotlinx.android.synthetic.main.fragment_fragment_listfor_singer.*
import kotlinx.android.synthetic.main.fragment_fragment_playlist.*

private const val Tag = "FRAGMENT_LISTSONG_FOR_PLAYLIST"

class FragmentListForPlaylist : Fragment() {
    private lateinit var listsongforplaylistadapter: SongAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fragment_list_for_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()

        img_back2.setOnClickListener({
            val libraryFragment = FragmentLibrary()
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.frame_View, libraryFragment).commit()

        })
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView() {
        txt_name_of_playlist.text = (activity as MainActivity).temPlaylistName
        txt_number.text = (activity as MainActivity).temNumberSong.toString() + " song(s)"
        rc_songs_playlist.layoutManager =
            LinearLayoutManager(activity?.baseContext, LinearLayoutManager.VERTICAL, false)
        rc_songs_playlist.addItemDecoration(
            ItemDecoration(
                resources.getDimensionPixelSize(R.dimen._3sdp),
                0,
                resources.getDimensionPixelSize(R.dimen._1sdp),
                resources.getDimensionPixelSize(R.dimen._1sdp)
            )
        )
        listsongforplaylistadapter = SongAdapter((activity as MainActivity).listSongById!!, activity!!,
            object : SongAdapter.ItemSongListener {
                override fun onClick(pos: Int, id: Long) {
                    activity!!.intent.putParcelableArrayListExtra(
                        Contants.EXTRA_SONGS,
                        (activity as MainActivity).listSongById as ArrayList<Song>
                    )
                    activity!!.intent.putExtra(Contants.EXTRA_POSITION, pos)

                    val playSong = FragmentPlaySong()
                    val transaction = fragmentManager?.beginTransaction()
                    if (transaction != null) {
                        transaction.replace(R.id.frame_View, playSong).addToBackStack(null).commit()
                    }
                }

            })

        rc_songs_playlist.adapter = listsongforplaylistadapter
    }


}
