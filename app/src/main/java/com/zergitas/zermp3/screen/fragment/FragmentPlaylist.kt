package com.zergitas.zermp3.screen.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.zergitas.zermp3.screen.main.adapter.PlayListAdapter
import kotlinx.android.synthetic.main.fragment_fragment_playlist.*
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.DatePicker
import android.widget.PopupMenu
import com.zergitas.zermp3.R
import com.zergitas.zermp3.data.source.local.song.db.DatabaseHelper
import com.zergitas.zermp3.screen.main.MainActivity
import com.zergitas.zermp3.widgets.ItemDecoration
import kotlinx.android.synthetic.main.edit_dialog.view.*

private const val Tag = "FRAGMENT_PLAYLIST"

class FragmentPlaylist : Fragment() {
    lateinit var adapter: PlayListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fragment_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        onClick()
        super.onViewCreated(view, savedInstanceState)

    }

    private fun onClick() {
        val addSongFragment = FragmentAddSongsforPlaylist()
        val main = FragmentLibrary()
        val transaction = fragmentManager?.beginTransaction()
        img_addsong.setOnClickListener({
            if (edt_playlistName.text.toString() == "") {
                Toast.makeText(activity, "Input PlayList Name!", Toast.LENGTH_SHORT).show()
            } else {
                insertPlaylist()
                if (transaction != null) {
                    transaction.addToBackStack(null)
                    transaction.replace(R.id.frame_View, addSongFragment)
                    transaction.remove(this)
                    transaction.commit()
                }
            }
        })
        img_back.setOnClickListener({
           if (transaction!=null){
               transaction.addToBackStack(null)
               transaction.replace(R.id.frame_View, main)
               transaction.remove(this)
               transaction.commit()
           }

        })

    }

    fun insertPlaylist() {
        val id_playlist = (0..1000).random()
        (activity as MainActivity).insertPlaylist(id_playlist, edt_playlistName.text.toString())
        (activity as MainActivity).tempPlaylistId = id_playlist

        var fagmentmanager: FragmentManager = activity!!.supportFragmentManager
        var fagmenttransaction: FragmentTransaction = fagmentmanager.beginTransaction()
        var fragmentaddsongforplaylist = FragmentAddSongsforPlaylist()
        fagmenttransaction.replace(R.id.frame_View, fragmentaddsongforplaylist)
        fagmenttransaction.commit()

        Log.d("randomid", id_playlist.toString())

    }

    fun initView() {
        rc_list_Playlits.addItemDecoration(
            ItemDecoration(
                resources.getDimensionPixelSize(R.dimen._4sdp),
                0,
                0,
                1
            )
        )
        adapter = PlayListAdapter((activity as MainActivity).getAllPlaylist()!!, activity!!,
            object : PlayListAdapter.PlaylistListener {
                override fun onItemClick(id_playlist: Int, name: String) {

                    (activity as MainActivity).temPlaylistName = name
                    (activity as MainActivity).temNumberSong = (activity as MainActivity).getcountSongs(id_playlist)
                    (activity as MainActivity).getIdSong(id_playlist)
                    (activity as MainActivity).getSongById()

                    val listSongs = FragmentListForPlaylist()
                    val transaction = fragmentManager?.beginTransaction()
                    if (transaction != null) {
                        transaction.addToBackStack(null)
                        transaction.replace(R.id.frame_View, listSongs)
                        transaction.commit()
                    }
                }

                override fun onPlaylistOptionClick(id_playlist: Int, item: Int, view: View, name: String) {
                    var popup: PopupMenu? = null
                    popup = PopupMenu(activity, view)
                    popup.inflate(R.menu.popupmenu)
                    popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                        when (item!!.itemId) {
                            R.id.p_m_edit -> {
                                editPlaylistName(id_playlist)
                            }
                            R.id.p_m_delete -> {
                                (activity as MainActivity).deletePlaylistById(id_playlist)
                                Toast.makeText(activity, "Deleted Playlist " + name, Toast.LENGTH_SHORT).show()
                                initView()
                            }
                            R.id.p_m_delete_all -> {
                                (activity as MainActivity).deleteAllPlaylist()
                                Toast.makeText(activity, "Deleted All Playlist ", Toast.LENGTH_SHORT).show()
                                initView()
                            }
                        }
                        true
                    })
                    popup.show()
                }

            }
        )

        rc_list_Playlits.adapter = adapter
    }

    fun editPlaylistName(id_playlist: Int) {
        val dialog = LayoutInflater.from(activity).inflate(R.layout.edit_dialog, null)
        val builder = AlertDialog.Builder(activity!!).setView(dialog).setTitle("Edit Name")
        val alterDialog = builder.show()
        dialog.btn_cancle.setOnClickListener({
            alterDialog.dismiss()

        })
        dialog.btn_ok.setOnClickListener({
            (activity as MainActivity).temNewNameforPlaylist = dialog.edt_newName.text.toString()
            (activity as MainActivity).editPlaylistName(id_playlist)
            alterDialog.dismiss()
            initView()
        })

    }
}
