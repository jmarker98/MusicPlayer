package com.zergitas.zermp3.screen.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.zergitas.zermp3.data.model.Song
import com.zergitas.zermp3.screen.main.MainActivity
import com.zergitas.zermp3.screen.main.adapter.SongAdapter
import com.zergitas.zermp3.screen.main.contract.MainContract
import com.zergitas.zermp3.utils.Contants
import com.zergitas.zermp3.widgets.ItemDecoration
import kotlinx.android.synthetic.main.bottomsheetplay.*
import kotlinx.android.synthetic.main.fragment_fragment_list_song.*


private const val Tag = "FRAGMENT_LIST_SONG"

class FragmentListSong : Fragment(), MainContract.View {

    private lateinit var adapter: SongAdapter
    var songs: ArrayList<Song>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.zergitas.zermp3.R.layout.fragment_fragment_list_song, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        img_leftback.setOnClickListener({
            val fm = activity!!.supportFragmentManager
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        })

        img_sort.setOnClickListener({
            createDialogSortListSong()
        })
        bottomSheetPlayingsong.visibility = View.GONE


        initRecyclerViewSongs()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun showProgressBar() {
    }

    override fun hiddenProgressBar() {
    }

    override fun addSongs(songs: List<Song>) {
        adapter.updateSongs(songs)
    }

    override fun updateSongs(pair: Pair<Int, Song>) {
        adapter.updateSongs(pair.first, pair.second)
    }

    override fun showErrorSongs() {
    }

    private fun initRecyclerViewSongs() {
        rc_songs.layoutManager = LinearLayoutManager(activity?.baseContext, LinearLayoutManager.VERTICAL, false)
        rc_songs.addItemDecoration(
            ItemDecoration(
                resources.getDimensionPixelSize(com.zergitas.zermp3.R.dimen._3sdp),
                0,
                resources.getDimensionPixelSize(com.zergitas.zermp3.R.dimen._1sdp),
                resources.getDimensionPixelSize(com.zergitas.zermp3.R.dimen._1sdp)
            )
        )
        adapter = SongAdapter((activity as MainActivity).songs!!, activity!!,
            object : SongAdapter.ItemSongListener {
                override fun onClick(pos: Int, id: Long) {

                    (activity as MainActivity).customBottomSheet()
                    activity!!.intent.putParcelableArrayListExtra(
                        Contants.EXTRA_SONGS,
                        (activity as MainActivity).songs as ArrayList<Song>
                    )
                    activity!!.intent.putExtra(Contants.EXTRA_POSITION, pos)
                    val playSong = FragmentPlaySong()
                    val transaction = fragmentManager?.beginTransaction()
                    if (transaction != null) {
                        transaction.addToBackStack(null)
                        transaction.replace(com.zergitas.zermp3.R.id.frame_View, playSong)
                        transaction.commit()

                    }
                }
            })
        rc_songs.adapter = adapter
    }

    fun createDialogSortListSong() {
        val colors = arrayOf("Name", "Singer")
        val builder = AlertDialog.Builder(activity!!).setTitle("Sort By:")
        builder.setItems(colors, DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                0 -> {
                    (activity as MainActivity).sortSongsByName()
                    adapter.notifyDataSetChanged()
                    Toast.makeText(activity, "Sorted By Name", Toast.LENGTH_LONG).show()
                }
                1 -> {
                    (activity as MainActivity).sortSongsBySinger()
                    adapter.notifyDataSetChanged()
                    Toast.makeText(activity, "Sorted By Singer", Toast.LENGTH_LONG).show()
                }
            }
        })
        builder.show()
    }

}
