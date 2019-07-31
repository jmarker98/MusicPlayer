package com.zergitas.zermp3.screen.main.adapter

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zergitas.zermp3.data.model.Song
import kotlinx.android.synthetic.main.item_song.view.*
import kotlinx.android.synthetic.main.item_songs_playlist.view.*
import java.io.ObjectInput
import kotlin.collections.List as List1

class AddSongforPlaylistAdapter(

    var addsongs: List1<Song>,
    private val context: Context,
    private val listener: ItemSongListener

) : RecyclerView.Adapter<AddSongforPlaylistAdapter.PlaylistSongViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PlaylistSongViewHolder {
        return PlaylistSongViewHolder(
            LayoutInflater.from(context).inflate(
                com.zergitas.zermp3.R.layout.item_songs_playlist,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int = addsongs.size

    override fun onBindViewHolder(p0: PlaylistSongViewHolder, p1: Int) {
        var song: Song = addsongs[p1]

        p0.itemView.cb_playlist.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked == true) {
                listener.onCheck(p0, p1, song)
            }
            if (isChecked==false){
                listener.onUnCheck(p0,p1,song)
            }
        }
        p0.bindData()
    }

    override fun onBindViewHolder(holder: PlaylistSongViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty() && payloads[0] is Bitmap) {
            holder.bindData(payloads[0] as Bitmap)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    fun getItemSong(pos: Int) = addsongs[pos]

    inner class PlaylistSongViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        fun bindData() {
            val song = getItemSong(adapterPosition)
            if (song.thumb != null) bindData(song.thumb!!)
            itemView.tv_namePl.text = song.name
            itemView.tv_singerPl.text = song.singer
            itemView.item_songPl.setOnClickListener {
                listener.onClick(adapterPosition)
            }


        }

        fun bindData(thumb: Bitmap) {
            itemView.img_thumb.setImageBitmap(thumb)
        }

    }

    interface ItemSongListener {
        fun onClick(pos: Int)
        fun onCheck(holder: PlaylistSongViewHolder, item: Int, song: Song)
        fun onUnCheck(holder: PlaylistSongViewHolder, item: Int, song: Song)
    }

}
