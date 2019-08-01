package com.zergitas.zermp3.screen.main.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.zergitas.zermp3.R
import com.zergitas.zermp3.data.model.Song
import kotlinx.android.synthetic.main.fragment_fragment_play_song.view.*
import kotlinx.android.synthetic.main.item_song.view.*

class SongAdapter(

    var songs: List<Song>,
    private val context: Context,
    private val listener: ItemSongListener

) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SongViewHolder {
        return SongViewHolder(LayoutInflater.from(context).inflate(R.layout.item_song, p0, false))
    }

    override fun getItemCount(): Int = songs.size

    override fun onBindViewHolder(p0: SongViewHolder, p1: Int) {
        p0.bindData()
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty() && payloads[0] is Bitmap) {
            holder.bindData(payloads[0] as Bitmap)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    fun getItemSong(pos: Int) = songs[pos]

    fun updateSongs(songs: List<Song>) {
        this.songs = songs
        notifyDataSetChanged()
    }

    fun updateSongs(index: Int, song: Song) {
        this.songs[index].thumb = song.thumb
        notifyItemChanged(index, song.thumb)
    }

    fun filterList(filterlist: ArrayList<Song>) {
        songs = filterlist
        notifyDataSetChanged()
    }

    inner class SongViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        fun bindData() {
            val song = getItemSong(adapterPosition)
            if (song.thumb != null) bindData(song.thumb!!)
            itemView.tv_name.text = song.name
            itemView.tv_singerN.text = song.singer
            itemView.item_song.setOnClickListener {
                listener.onClick(adapterPosition, song.id)
            }
        }

        fun bindData(thumb: Bitmap) {
            itemView.img_thumb.setImageBitmap(thumb)
        }
    }

    interface ItemSongListener {
        fun onClick(pos: Int, id: Long)
    }

}