package com.zergitas.zermp3.screen.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zergitas.zermp3.R
import com.zergitas.zermp3.screen.main.MainActivity
import com.zergitas.zermp3.screen.main.adapter.AlbumAdapter
import com.zergitas.zermp3.widgets.ItemDecoration
import kotlinx.android.synthetic.main.fragment_fragment_album.*
import kotlinx.android.synthetic.main.fragment_fragment_artists.*
import kotlinx.android.synthetic.main.fragment_fragment_list_song.*
import kotlinx.android.synthetic.main.fragment_fragment_list_song.img_leftback
private const val Tag = "FRAGMENT_ALBUMS"

class FragmentAlbum : Fragment() {
    lateinit var adapter: AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fragment_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        img_leftback.setOnClickListener({
            val fm = activity!!.supportFragmentManager
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        })
        initView()
        super.onViewCreated(view, savedInstanceState)
    }

    fun initView() {
        rc_albums.addItemDecoration(
            ItemDecoration(
                resources.getDimensionPixelSize(R.dimen._4sdp),
                0,
                0,
                0
            )
        )
        adapter = AlbumAdapter((activity as MainActivity).getAlbumfromDb()!!, activity!!,
            object : AlbumAdapter.Albumlistener {
                override fun onItemClick(id: Long, name: String) {
                    (activity as MainActivity).getSongByIdAlbum(id)
                    (activity as MainActivity).temAlbumName = name

                    val listSongs = FragmentListForAlbums()
                    val transaction = fragmentManager?.beginTransaction()
                    if (transaction != null) {
                        transaction.addToBackStack(null)
                        transaction.replace(R.id.frame_View, listSongs)
                        transaction.commit()
                    }
                }

            })
        rc_albums.adapter = adapter
    }


}
