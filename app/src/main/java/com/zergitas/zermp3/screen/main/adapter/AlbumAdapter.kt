package com.zergitas.zermp3.screen.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zergitas.zermp3.R
import com.zergitas.zermp3.data.model.Album
import kotlinx.android.synthetic.main.item_albums.view.*

class AlbumAdapter(
    val listAlbum: ArrayList<Album>,
    var context: Context,
    var listener: Albumlistener
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AlbumViewHolder {
        return AlbumViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_albums, p0, false))

    }

    override fun getItemCount(): Int {
        return listAlbum.size
    }

    override fun onBindViewHolder(p0: AlbumViewHolder, p1: Int) {
        var album: Album = listAlbum[p1]
        p0.itemView.txt_Album_name.text = album.album
        p0.itemView.item_album.setOnClickListener({
            listener.onItemClick(album.idAlbum, album.album)
        })

    }

    class AlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

    interface Albumlistener {
        fun onItemClick(id: Long, name: String)
    }
}