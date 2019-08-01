package com.zergitas.zermp3.screen.main.adapter

import android.content.Context
import android.provider.ContactsContract
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.zergitas.zermp3.R
import com.zergitas.zermp3.data.model.Playlist
import com.zergitas.zermp3.data.model.Song
import com.zergitas.zermp3.screen.main.MainActivity
import kotlinx.android.synthetic.main.item_playlist.view.*
import kotlin.math.acos

class PlayListAdapter(
    var playlistList: ArrayList<Playlist>,
    var context: Context,
    var listener: PlaylistListener

) : RecyclerView.Adapter<PlayListAdapter.PlaylistViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PlaylistViewHolder {
        return PlaylistViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_playlist, p0, false))
    }

    override fun getItemCount(): Int {
        return playlistList.size
    }

    override fun onBindViewHolder(p0: PlaylistViewHolder, p1: Int) {

        var playlist = playlistList[p1]
        p0.itemView.txt_playlistName.text = playlist.name

        p0.itemView.img_more_options.setOnClickListener {
            listener.onPlaylistOptionClick(playlist.id_playlist, p1, it, playlist.name)
        }
        p0.itemView.item_playlist.setOnClickListener({
            Log.d("iditem", playlist.id_playlist.toString())
            listener.onItemClick(playlist.id_playlist,playlist.name)
        })

    }

    class PlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

    interface PlaylistListener {
        fun onItemClick(id_playlist: Int,name:String)
        fun onPlaylistOptionClick(id_playlist: Int, item: Int, view: View, name: String)
    }
}