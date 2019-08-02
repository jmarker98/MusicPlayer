package com.zergitas.zermp3.screen.main.adapter

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zergitas.zermp3.R
import com.zergitas.zermp3.data.model.Song
import kotlinx.android.synthetic.main.item_recently_added.view.*
import kotlinx.android.synthetic.main.item_song.view.*

class RecentlySongAdapter(
    var songs: List<Song>,
    private val context: Context,
    private val listener: ItemSongListener

) : RecyclerView.Adapter<RecentlySongAdapter.SongViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SongViewHolder {
        return SongViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recently_added, p0, false))
    }

    override fun getItemCount(): Int {
        return 6
    }

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

    inner class SongViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        fun bindData() {
            val song = getItemSong(adapterPosition)
            if (song.thumb != null) bindData(song.thumb!!)
            itemView.txt_name.text = song.name
            itemView.item_recently.setOnClickListener {
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