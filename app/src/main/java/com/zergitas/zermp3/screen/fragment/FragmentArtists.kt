package com.zergitas.zermp3.screen.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.zergitas.zermp3.R
import com.zergitas.zermp3.screen.main.MainActivity
import com.zergitas.zermp3.screen.main.adapter.SingerAdapter
import com.zergitas.zermp3.widgets.ItemDecoration
import kotlinx.android.synthetic.main.fragment_fragment_artists.*
import kotlinx.android.synthetic.main.fragment_fragment_list_song.*
import kotlinx.android.synthetic.main.fragment_fragment_list_song.img_leftback
import kotlinx.android.synthetic.main.fragment_fragment_playlist.*
import kotlin.math.sin

private const val Tag = "FRAGMENT_ARTISTS"

class FragmentArtists : Fragment() {
    lateinit var singeradapter: SingerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fragment_artists, container, false)
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
        rc_singer.addItemDecoration(
            ItemDecoration(
                resources.getDimensionPixelSize(R.dimen._4sdp),
                0,
                0,
                0
            )
        )
        singeradapter = SingerAdapter((activity as MainActivity).getSingerfromDb()!!, activity!!,
            object : SingerAdapter.SingerListener {
                override fun onItemClick(id: Int, name: String) {

                    (activity as MainActivity).getSongBySinger(name)
                    (activity as MainActivity).temSingername = name

                    val listSongs = FragmentListforSinger()
                    val transaction = fragmentManager?.beginTransaction()
                    if (transaction != null) {
                        transaction.addToBackStack(null)
                        transaction.replace(R.id.frame_View, listSongs)
                        transaction.commit()
                    }

                }

            })

        rc_singer.adapter = singeradapter
    }
}
